package edu.handong.csee.isel.fcminer.fpcollector.gumtree;

import java.util.ArrayList;

import org.eclipse.jdt.core.dom.MethodDeclaration;

public class GumTreeMain {
	ArrayList<Info> infos = new ArrayList<>();
	
	public GumTreeMain(ArrayList<Info> infos) {
		this.infos = infos;
	}
	
	public void run() {
		System.out.println("INFO: Data pre-processing is started");
		dataPreprocess();
		System.out.println("INFO: Data pre-processing is finished");
		System.out.println("INFO: Code Comparison is started");
		codeCompare();
		System.out.println("INFO: Code Comparison is finished");
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
		System.out.println("INFO: Start to get and Write FC-Miner Result File");
		long start = System.currentTimeMillis();
		for(int i = 0 ; i < infos.size(); i ++) {
			if(i==0) System.out.print("Start...");
			else if((i / infos.size()) * 100 >= 80) System.out.print("80%...");
			else if((i / infos.size()) * 100 >= 60) System.out.print("60%...");
			else if((i / infos.size()) * 100 >= 40) System.out.print("40%...");
			else if((i / infos.size()) * 100 >= 20) System.out.print("20%...\n\r");
			
			gumTreeComp.compare(infos.get(i));
			for(Info info : infos) {
				gumTreeComp.compare(info);  
			}
			patternWriter.writePatternsAnalysis(gumTreeComp.getPatterns());
			gumTreeComp.clear();
		}
		long end = System.currentTimeMillis();		
		System.out.println("INFO: Finish to get and Write FC-Miner Result File (" + (end - start)/1000 + " sec.)");
	}
}
