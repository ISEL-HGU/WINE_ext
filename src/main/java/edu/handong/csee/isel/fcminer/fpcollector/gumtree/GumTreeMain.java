package edu.handong.csee.isel.fcminer.fpcollector.gumtree;

import java.util.ArrayList;

import org.eclipse.jdt.core.dom.MethodDeclaration;

public class GumTreeMain {
	ArrayList<Info> infos = new ArrayList<>();
	
	public GumTreeMain(ArrayList<Info> infos) {
		this.infos = infos;
	}
	
	public void run() {
		dataPreprocess();
		codeCompare();
	}
	
	private void dataPreprocess() {
		int cnt =1;
		for(Info info: infos) {
			info = prepare4GumTree(info, cnt);
		    cnt++;
		}
	}
	
	private Info prepare4GumTree(Info info, int cnt) {
		MethodFinder methodFinder = new MethodFinder(info);
	    
	    info = methodFinder.findMethod();
	    info.setMockClass(method2Class(info.getViolatingMethod(), cnt));

	    return info;
	}
	
	private String method2Class(MethodDeclaration violatedMethod, int cnt) {
		String method2Class = violatedMethod.toString();
		
		method2Class = "public class MockClass" + cnt + "{\n"
						+ method2Class
						+ "}";
		
		return method2Class;
	}
	
	private void codeCompare() { 
		CodeComparator gumTreeComp = new CodeComparator();
		PatternWriter patternWriter = new PatternWriter();
		for(int i = 0 ; i < infos.size(); i ++) {
			gumTreeComp.compare(infos.get(i));
			for(Info info : infos) {
				gumTreeComp.compare(info);  
			}
			patternWriter.writePatterns(gumTreeComp.getPatterns());
			gumTreeComp.clear();
		}
	}
}
