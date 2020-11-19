package edu.handong.csee.isel.fcminer.fpcollector.gumtree;

import java.util.ArrayList;
import java.util.List;

import edu.handong.csee.isel.fcminer.gumtree.core.tree.ITree;
import edu.handong.csee.isel.fcminer.gumtree.core.tree.TreeUtils;

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
	    info.setMockClass(method2Class(info.getVMethodString(), cnt));
	    info.setVNode(findVNode(info));
	    return info;
	}
	
	private ITree findVNode(Info info) {
		ITree vNode = null;
		ITree vMethod = info.getVMethod();
		//set vNode based on line number in ITree
        List<ITree> currents = new ArrayList<>();
        currents.add(vMethod);
        while (currents.size() > 0) {        	
            ITree c = currents.remove(0);
            if(contain(c.getNode2String(), info.getVLine()) && 
            		(c.getStartLineNum() <= info.start && c.getEndLineNum() >= info.end)) {        
            	vNode = c;
            }
            currents.addAll(c.getChildren());
        }
		
		return vNode;
	}
	
	//for remove all blank and only compare characters
	private boolean contain(String src, String test) {
		String newSrc = "";
		String newTest = "";
		for(int i = 0; i < src.length(); i ++) {
			if(!Character.isSpaceChar(src.charAt(i))) {
				newSrc += src.charAt(i);
			}
		}
		
		for(int i = 0; i < test.length(); i ++) {
			if(!Character.isSpaceChar(test.charAt(i))) {
				newTest += test.charAt(i);
			}
		}
		
		return newSrc.contains(newTest);
	}
	
	private String method2Class(String vMethodString, int cnt) {
		
		vMethodString = "public class MockClass" + cnt + "{\n"
						+ vMethodString
						+ "}";
		
		return vMethodString;
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
