package edu.handong.csee.isel.fcminer.fpcollector.gumtree;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import org.eclipse.jdt.core.dom.ASTNode;

import edu.handong.csee.isel.fcminer.gumtree.client.Run;
import edu.handong.csee.isel.fcminer.gumtree.core.actions.ActionGenerator;
import edu.handong.csee.isel.fcminer.gumtree.core.actions.model.Action;
import edu.handong.csee.isel.fcminer.gumtree.core.matchers.MappingStore;
import edu.handong.csee.isel.fcminer.gumtree.core.matchers.Matcher;
import edu.handong.csee.isel.fcminer.gumtree.core.matchers.Matchers;
import edu.handong.csee.isel.fcminer.gumtree.core.tree.ITree;
import edu.handong.csee.isel.fcminer.gumtree.gen.jdt.JdtTreeGenerator;

public class CodeComparator {
	private Stack<Info> gumTreeStack = new Stack<>();
	private Patterns patterns = new Patterns();
	
	public void compare(Info info) { 
		if(gumTreeStack.contains(info)) return;
		
		gumTreeStack.add(info);
		
		if(gumTreeStack.size() == 1) return;
		
		runGumTree();
	}
	
	private void runGumTree() {		
		//real
		Info variableClass = gumTreeStack.pop();
		Info fixedClass = gumTreeStack.elementAt(0);
		patterns.setFixed(fixedClass);
		
		//toy
//		String variableClass = "public class MockClass1{"
//				+ "public void twice(int a){"
//				+ "return a + a;"
//				+ "}"
//				+ "}";
//		String fixedClass = "public class MockClass0{"
//				+ "public void identify(int a){"
//				+ "return a;"
//				+ "}"
//				+ "}";
		
		//test
//		String variableClass = "public class MockClass1{" +
//				"private void scheduleSpeculativeRead(final ScheduledExecutorService scheduler,final SpeculativeRequestExecutor requestExecutor,final int speculativeRequestTimeout){\r\n" + 
//				"  try {\r\n" + 
//				"  scheduler.schedule(new Runnable(){\r\n" + 
//				"  @Override public void run(){\r\n" + 
//				"  ListenableFuture<Boolean> issueNextRequest=requestExecutor.issueSpeculativeRequest();\r\n" + 
//				"  Futures.addCallback(issueNextRequest,new FutureCallback<Boolean>(){\r\n" + 
//				"  public void onSuccess( Boolean issueNextRequest){\r\n" + 
//				"  if (issueNextRequest) {\r\n" + 
//				"  scheduleSpeculativeRead(scheduler,requestExecutor,Math.min(maxSpeculativeRequestTimeout,Math.round((float)speculativeRequestTimeout * backoffMultiplier)));\r\n" + 
//				"  }\r\n" + 
//				"  else {\r\n" + 
//				"  if (LOG.isTraceEnabled()) {\r\n" + 
//				"  LOG.trace(\"Stopped issuing speculative requests for {}, \" + \"speculativeReadTimeout = {}\",requestExecutor,speculativeRequestTimeout);\r\n" + 
//				"  }\r\n" + 
//				"  }\r\n" + 
//				"  }\r\n" + 
//				"  public void onFailure( Throwable thrown){\r\n" + 
//				"  LOG.warn(\"Failed to issue speculative request for {}, speculativeReadTimeout = {} : \",new Object[]{requestExecutor,speculativeRequestTimeout,thrown});\r\n" + 
//				"  }\r\n" + 
//				"  }\r\n" + 
//				" );\r\n" + 
//				"  }\r\n" + 
//				"  }\r\n" + 
//				" ,speculativeRequestTimeout,TimeUnit.MILLISECONDS);\r\n" + 
//				"  }\r\n" + 
//				"  catch ( RejectedExecutionException re) {\r\n" + 
//				"  if (!scheduler.isShutdown()) {\r\n" + 
//				"  LOG.warn(\"Failed to schedule speculative request for {}, speculativeReadTimeout = {} : \",new Object[]{requestExecutor,speculativeRequestTimeout,re});\r\n" + 
//				"  }\r\n" + 
//				"  }\r\n" + 
//				" }" +
//				"}";
//		String fixedClass = "public class MockClass1{" + 
//				"/** \r\n" + 
//				" * Logs a trace message for newly inserted entries to the stats cache.\r\n" + 
//				" */\r\n" + 
//				"void traceStatsUpdate(GuidePostsKey key,GuidePostsInfo info){\r\n" + 
//				"  if (LOGGER.isTraceEnabled()) {\r\n" + 
//				"    LOGGER.trace(\"Updating local TableStats cache (id={}) for {}, size={}bytes\",new Object[]{Objects.hashCode(this),key,info.getEstimatedSize()});\r\n" + 
//				"  }\r\n" + 
//				"}\r\n" + 
//				"}";
		
		//GumTree pattern comparison
		//save patterns in File
		//ex) Pattern1.csv, Pattern2.csv ...
		
		//GumTree Init
		Run.initGenerators();
		ITree variable = null;
		ITree fixed = null;
		
		try {
			variable = new JdtTreeGenerator().generateFromString(variableClass.getMockClass()).getRoot();
			fixed = new JdtTreeGenerator().generateFromString(fixedClass.getMockClass()).getRoot();
		} catch (Exception e) {
			e.printStackTrace();
		}
			
		Matcher matchClass = Matchers.getInstance().getMatcher(fixed, variable);
		matchClass.match();
		
		ActionGenerator actionGen = new ActionGenerator(fixed, variable, matchClass.getMappings());
		actionGen.generate();
		
		Pattern pattern = new Pattern();
		
		for(ITree node: actionGen.getCommonNodes()) {
			if(fixedClass.getMockStart() <= node.getPos() && node.getPos() <= fixedClass.getMockEnd()) {
				pattern.addNode2Pattern(node);
			}
			
//			System.out.println(node.toShortString());
		}
		
		patterns.addPattern(pattern);
		
//		for(ITree node: actionGen.getMovedNodes()) {
//			System.out.println(node.getType());
//			System.out.println(node.toShortString());
//		}
//		
//		for(ITree node: actionGen.getUpdatedNodes()) {
//			System.out.println(node.toShortString());
//		}
//		
//		for(ITree node: actionGen.getMaintainedNodes()) {
//			System.out.println(node.toShortString());
//		}
	}
	
	public Patterns getPatterns(){
		return patterns;
	}
	
	public void clear() {
		gumTreeStack.clear();
		patterns = new Patterns();
	}
}
