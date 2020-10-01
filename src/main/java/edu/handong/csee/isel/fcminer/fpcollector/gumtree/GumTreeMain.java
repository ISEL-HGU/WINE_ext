package edu.handong.csee.isel.fcminer.fpcollector.gumtree;

import java.util.ArrayList;

import org.eclipse.jdt.core.dom.MethodDeclaration;

import edu.handong.csee.isel.fcminer.fpcollector.graphbuilder.Info;

public class GumTreeMain {
	ArrayList<Info> infos = new ArrayList<>();
	ArrayList<String> mockClasses = new ArrayList<>();
	
	public GumTreeMain(ArrayList<Info> infos) {
		this.infos = infos;
	}
	
	public void run() {
		dataPreprocess(infos);
		codeCompare(mockClasses);
	}
	
	private void dataPreprocess(ArrayList<Info> infos) {
		int cnt =1;
		for(Info info: infos) {
			String mockClass = prepare4GumTree(info, cnt);
		    cnt++;
		    mockClasses.add(mockClass);
		}
	}
	
	private String prepare4GumTree(Info info, int cnt) {
		MethodFinder methodFinder = new MethodFinder(info);
	    MethodDeclaration violatedMethod;
	    
	    violatedMethod = methodFinder.findMethod();
	    String mockClass = method2Class(violatedMethod, cnt);
	    
	    return mockClass;
	}
	
	private String method2Class(MethodDeclaration violatedMethod, int cnt) {
		String method2Class = violatedMethod.toString();
		
		method2Class = "public class MockClass" + cnt + "{\n"
						+ method2Class
						+ "}";
		
		return method2Class;
	}
	
	private void codeCompare(ArrayList<String> mockClasses) {
		CodeComparator gumTreeComp = new CodeComparator();
		for(int i = 0 ; i < mockClasses.size(); i ++) {
			gumTreeComp.compare(mockClasses.get(i));
			for(String mockClass : mockClasses) {
				gumTreeComp.compare(mockClass);  
			}
			gumTreeComp.clear();
		}
	}
}
