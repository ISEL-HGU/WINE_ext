package edu.handong.csee.isel.fcminer.fpcollector.gumtree;

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
	private Stack<String> gumTreeStack = new Stack<>();
	private ASTNode pattern;
	
	public void compare(String mockClass) {
		if(gumTreeStack.contains(mockClass)) return;
		gumTreeStack.add(mockClass);
		if(gumTreeStack.size() == 1) return;
		runGumTree();
	}
	
	private void runGumTree() {		
//		String variableClass = gumTreeStack.pop();
//		String fixedClass = gumTreeStack.elementAt(0);
		String variableClass = "public class MockClass1{"
				+ "public void twice(int a){"
				+ "return a + a;"
				+ "}"
				+ "}";
		String fixedClass = "public class MockClass0{"
				+ "public void identify(int a){"
				+ "return a;"
				+ "}"
				+ "}";
		//need to data preprocess
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
//		String fixedClass = "public class MockClass0{" + 
//				"/** \r\n" + 
//				" * Creates an instance of the specified provider class using the no-arg constructor.\r\n" + 
//				" * @param typeName The provider type name used for log messages (e.g. \"authentication provider\")\r\n" + 
//				" * @param providerClass The provider class to instantiate\r\n" + 
//				" * @param < T > The provider type\r\n" + 
//				" * @return A provider instance or null if no instance was created due to error\r\n" + 
//				" */\r\n" + 
//				"static <T>T newInstance(String typeName,Class<? extends T> providerClass){\r\n" + 
//				"  T instance=null;\r\n" + 
//				"  try {\r\n" + 
//				"    instance=providerClass.getConstructor().newInstance();\r\n" + 
//				"  }\r\n" + 
//				" catch (  NoSuchMethodException e) {\r\n" + 
//				"    logger.error(\"The {} extension in use is not properly defined. \" + \"Please contact the developers of the extension or, if you \" + \"are the developer, turn on debug-level logging.\",typeName);\r\n" + 
//				"    logger.debug(\"{} is missing a default constructor.\",providerClass.getName(),e);\r\n" + 
//				"  }\r\n" + 
//				"catch (  SecurityException e) {\r\n" + 
//				"    logger.error(\"The Java security manager is preventing extensions \" + \"from being loaded. Please check the configuration of Java or your \" + \"servlet container.\");\r\n" + 
//				"    logger.debug(\"Creation of {} disallowed by security manager.\",providerClass.getName(),e);\r\n" + 
//				"  }\r\n" + 
//				"catch (  InstantiationException e) {\r\n" + 
//				"    logger.error(\"The {} extension in use is not properly defined. \" + \"Please contact the developers of the extension or, if you \" + \"are the developer, turn on debug-level logging.\",typeName);\r\n" + 
//				"    logger.debug(\"{} cannot be instantiated.\",providerClass.getName(),e);\r\n" + 
//				"  }\r\n" + 
//				"catch (  IllegalAccessException e) {\r\n" + 
//				"    logger.error(\"The {} extension in use is not properly defined. \" + \"Please contact the developers of the extension or, if you \" + \"are the developer, turn on debug-level logging.\");\r\n" + 
//				"    logger.debug(\"Default constructor of {} is not public.\",typeName,e);\r\n" + 
//				"  }\r\n" + 
//				"catch (  IllegalArgumentException e) {\r\n" + 
//				"    logger.error(\"The {} extension in use is not properly defined. \" + \"Please contact the developers of the extension or, if you \" + \"are the developer, turn on debug-level logging.\",typeName);\r\n" + 
//				"    logger.debug(\"Default constructor of {} cannot accept zero arguments.\",providerClass.getName(),e);\r\n" + 
//				"  }\r\n" + 
//				"catch (  InvocationTargetException e) {\r\n" + 
//				"    Throwable cause=e.getCause();\r\n" + 
//				"    if (cause == null)     cause=new GuacamoleException(\"Error encountered during initialization.\");\r\n" + 
//				"    logger.error(\"{} extension failed to start: {}\",typeName,cause.getMessage());\r\n" + 
//				"    logger.debug(\"{} instantiation failed.\",providerClass.getName(),e);\r\n" + 
//				"  }\r\n" + 
//				"  return instance;\r\n" + 
//				"}"+
//				"}";
		
		//GumTree pattern comparison
		//save patterns in File
		//ex) Pattern1.csv, Pattern2.csv ...
		
		//GumTree Init
		Run.initGenerators();
		ITree variable = null;
		ITree fixed = null;
		
		try {
			variable = new JdtTreeGenerator().generateFromString(variableClass).getRoot();
			fixed = new JdtTreeGenerator().generateFromString(fixedClass).getRoot();
		} catch (Exception e) {
			e.printStackTrace();
		}
			
		Matcher matchClass = Matchers.getInstance().getMatcher(fixed, variable);
		matchClass.match();
		
		ActionGenerator actionGen = new ActionGenerator(fixed, variable, matchClass.getMappings());
		actionGen.generate();

		List<Action> actions = actionGen.getActions();
		
		for(ITree node: actionGen.getMovedNodes()) {
			System.out.println(node.toShortString());
		}
		
		for(ITree node: actionGen.getUpdatedNodes()) {
			System.out.println(node.toShortString());
		}
		
		for(ITree node: actionGen.getMaintainedNodes()) {
			System.out.println(node.toShortString());
		}
		
		MappingStore mapStorage = matchClass.getMappings();
		ITree matchedTree = mapStorage.getSrc(variable);
		System.out.println(matchedTree.toTreeString());
		System.out.println(variableClass);
		System.out.println(fixedClass);
	}
	
	public void clear() {
		gumTreeStack.clear();
	}
}
