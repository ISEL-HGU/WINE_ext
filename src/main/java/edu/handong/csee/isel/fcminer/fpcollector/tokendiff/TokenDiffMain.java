package edu.handong.csee.isel.fcminer.fpcollector.tokendiff;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import edu.handong.csee.isel.fcminer.fpcollector.tokendiff.ast.ITree;
import edu.handong.csee.isel.fcminer.fpcollector.tokendiff.compare.CodeComparator;
import edu.handong.csee.isel.fcminer.fpcollector.tokendiff.compare.MappingStorage;
import edu.handong.csee.isel.fcminer.fpcollector.tokendiff.datapreproc.Info;
import edu.handong.csee.isel.fcminer.fpcollector.tokendiff.datapreproc.MethodFinder;

public class TokenDiffMain {
	ArrayList<Info> infos = new ArrayList<>();
	
	public TokenDiffMain(ArrayList<Info> infos) {
		this.infos = infos;
	}
	
	public ArrayList<MappingStorage> run() {
		System.out.println("INFO: Data Pre-Processing is Started");
		dataPreprocess();
		System.out.println("INFO: Data Pre-Processing is Finished");
		System.out.println("INFO: Code Comparison is Started");
		return codeCompare();				
	}
	
	private void dataPreprocess() {
		int cnt =0;
		for(Info info: infos) {			
			info = prepare4GumTree(info, cnt);
			
			//the case when the violating line is not in a method but in static block or something.
			if(info.getVMethod() == null) {
				info = null;
			}
			
		    cnt++;
		    printProgress(cnt, infos.size());
		}
	}
	
	private Info prepare4GumTree(Info info, int cnt) {		
		MethodFinder methodFinder = new MethodFinder(info);
	    info = methodFinder.findMethod();
	    
	    //the case when the violating line is not in a method but in static block or something.
	    if(info.getVMethod() == null) {
	    	return info;
	    }
	    
	    info.setMockClass(method2Class(info.getVMethodString(), cnt));
	    info.setVNode(findVNode(info));
	    divide(info);
	    return info;
	}
	
	
	
	private void divide(Info info) {		
		List<ITree> currents = new ArrayList<>();
		
		currents.add(info.getVMethod());
        while (currents.size() > 0) {        	
            ITree c = currents.remove(0);
            if(c.getPos() <= info.getVNode().getPos() && c.getEndPos() <= info.getVNode().getPos()) {        
            	info.addForwardPart(c);
            } else if(c.getPos() >= info.getVNode().getPos() && c.getEndPos() <= info.getVNode().getEndPos()) {
            	info.addVPart(c);
            } else if(c.getPos() >= info.getVNode().getEndPos()) {
            	info.addBackwardPart(c);
            }
            currents.addAll(c.getChildren());
        }
        sort(info);        
	}
	
	private void sort(Info info) {
		info.getForwardPart().sort(new Comparator<ITree>() {
			public int compare(ITree node1, ITree node2) {
				if(node1.getPos() > node2.getPos()) {
					return 1;
				} else if(node1.getPos() == node2.getPos()) {
					if(node1.getDepth() > node2.getDepth()) {
						return 1;
					} else return -1;
				} else return -1;
			}
		});
		
		info.getVPart().sort(new Comparator<ITree>() {
			public int compare(ITree node1, ITree node2) {
				if(node1.getPos() > node2.getPos()) {
					return 1;
				} else if(node1.getPos() == node2.getPos()) {
					if(node1.getDepth() > node2.getDepth()) {
						return 1;
					} else return -1;
				} else return -1;
			}
		});
		
		info.getBackwardPart().sort(new Comparator<ITree>() {
			public int compare(ITree node1, ITree node2) {
				if(node1.getPos() > node2.getPos()) {
					return 1;
				} else if(node1.getPos() == node2.getPos()) {
					if(node1.getDepth() > node2.getDepth()) {
						return 1;
					} else return -1;
				} else return -1;
			}
		});
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
            		(c.getStartLineNum() <= info.getStart() && c.getEndLineNum() >= info.getEnd())) {        
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
			if(Character.isSpaceChar(src.charAt(i)) || Character.isWhitespace(src.charAt(i))) {
				continue;
			}
			newSrc += src.charAt(i);
		}
		
		for(int i = 0; i < test.length(); i ++) {
			if(Character.isSpaceChar(test.charAt(i)) || Character.isWhitespace(test.charAt(i))) {
				continue;
			}
			newTest += test.charAt(i);
		}
		
		return newSrc.contains(newTest);
	}
	
	private String method2Class(String vMethodString, int cnt) {
		
		vMethodString = "public class MockClass" + cnt + "{\n"
						+ vMethodString
						+ "}";
		
		return vMethodString;
	}
	
	private ArrayList<MappingStorage> codeCompare() { 
		CodeComparator tokenDiff = new CodeComparator();		
		for(int i = 0 ; i < infos.size(); i ++) {
			if(infos.get(i) == null) continue;
			printProgress(i, infos.size());
			
			tokenDiff.compare(infos.get(i));
			for(int j = i ; j < infos.size(); j++) {
				if(infos.get(j) == null) continue;
				tokenDiff.compare(infos.get(j));
			}

			tokenDiff.clear();
		}				
		return tokenDiff.getMappingStorage();
	}
	
	private void printProgress(int cnt, int total) {
		if(total / 10 == cnt) {
			System.out.print("10%...");
		}
		else if(total * 2 / 10 == cnt) {
			System.out.print("20%...");
		}
		else if(total * 3 / 10 == cnt) {
			System.out.print("30%...");
		}
		else if(total * 4 / 10 == cnt) {
			System.out.print("40%...");
		}
		else if(total * 5/ 10 == cnt) {
			System.out.print("50%...");
		}
		else if(total * 6 / 10 == cnt) {
			System.out.print("60%...");
		}
		else if(total * 7 / 10 == cnt) {
			System.out.print("70%...");
		}
		else if(total * 8 / 10 == cnt) {
			System.out.print("80%...");
		}
		else if(total * 9 / 10 == cnt) {
			System.out.print("90%...");
		}
		else if(total-1 == cnt) {
			System.out.print("done!\n");
		}		
	}
}
