package edu.handong.csee.isel.fcminer.fpcollector.gumtree;

import java.util.List;
import java.util.Stack;

import org.eclipse.jdt.core.dom.ASTNode;

import com.github.gumtreediff.actions.ActionGenerator;
import com.github.gumtreediff.actions.model.Action;
import com.github.gumtreediff.client.Run;
import com.github.gumtreediff.gen.jdt.JdtTreeGenerator;
import com.github.gumtreediff.matchers.MappingStore;
import com.github.gumtreediff.matchers.Matcher;
import com.github.gumtreediff.matchers.Matchers;
import com.github.gumtreediff.tree.ITree;

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
		//need to data preprocess
		String variableClass = "public class MockClass2{\r\n" + 
				"@Override protected void tearDown() throws Exception {\r\n" + 
				"  if (applicationContext != null) {\r\n" + 
				"    applicationContext.destroy();\r\n" + 
				"  }\r\n" + 
				"  if (server != null) {\r\n" + 
				"    server.stop();\r\n" + 
				"  }\r\n" + 
				"  if (endpoint != null) {\r\n" + 
				"    endpoint.stop();\r\n" + 
				"  }\r\n" + 
				"  Interceptor[] interceptors={mapVerifier,headerVerifier};\r\n" + 
				"  removeInterceptors(staticBus.getInInterceptors(),interceptors);\r\n" + 
				"  removeInterceptors(staticBus.getOutInterceptors(),interceptors);\r\n" + 
				"  removeInterceptors(staticBus.getOutFaultInterceptors(),interceptors);\r\n" + 
				"  removeInterceptors(staticBus.getInFaultInterceptors(),interceptors);\r\n" + 
				"  mapVerifier=null;\r\n" + 
				"  headerVerifier=null;\r\n" + 
				"  verified=null;\r\n" + 
				"  messageIDs.clear();\r\n" + 
				"  super.tearDown();\r\n" + 
				"  BusFactory.setDefaultBus(null);\r\n" + 
				"  Thread.sleep(5000);\r\n" + 
				"}\r\n" + 
				"}";
		String fixedClass = "public class MockClass1{\r\n" + 
				"private void verify(Message message){\r\n" + 
				"  boolean isOutbound=ContextUtils.isOutbound(message);\r\n" + 
				"  String mapProperty=(String)mapProperties.get(isOutbound ? WSAddressingTest.OUTBOUND_KEY : WSAddressingTest.INBOUND_KEY);\r\n" + 
				"  AddressingProperties maps=(AddressingPropertiesImpl)message.get(mapProperty);\r\n" + 
				"  if (ContextUtils.isRequestor(message)) {\r\n" + 
				"    if (isOutbound) {\r\n" + 
				"      String exposeAs=getExpectedExposeAs(false);\r\n" + 
				"      if (exposeAs != null) {\r\n" + 
				"        maps.exposeAs(exposeAs);\r\n" + 
				"      }\r\n" + 
				"    }\r\n" + 
				" else {\r\n" + 
				"      String exposeAs=getExpectedExposeAs(true);\r\n" + 
				"      String expected=exposeAs != null ? exposeAs : Names.WSA_NAMESPACE_NAME;\r\n" + 
				"      if (maps.getNamespaceURI() != expected) {\r\n" + 
				"        verificationCache.put(\"Incoming version mismatch\" + \" expected: \" + expected + \" got: \"+ maps.getNamespaceURI());\r\n" + 
				"      }\r\n" + 
				"      exposeAs=null;\r\n" + 
				"    }\r\n" + 
				"  }\r\n" + 
				"  verificationCache.put(WSAddressingTest.verifyMAPs(maps,this));\r\n" + 
				"}\r\n" + 
				"}";
		
		//GumTree pattern comparison
		//save patterns in File
		//ex) Pattern1.csv, Pattern2.csv ...
		
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
