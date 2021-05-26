package edu.handong.csee.isel.fcminer.fpcollector.subset;

import java.util.ArrayList;
import java.util.Comparator;

import edu.handong.csee.isel.fcminer.fpcollector.tokendiff.ast.ITree;
import edu.handong.csee.isel.fcminer.fpcollector.tokendiff.datapreproc.Node;
import edu.handong.csee.isel.fcminer.fpcollector.tokendiff.datapreproc.NodeList;

public class SubWarningGenerator {
	private enum Relation {
		NULL, SubWarning, Equivalent;
	}
	ArrayList<Integer> hashList = new ArrayList<>();
	
	public ArrayList<Integer> getHashList(){
		return hashList;
	}
	
	public ArrayList<SuperWarning> generateSubWarning(ArrayList<NodeList> nodeLists) {
		ArrayList<SuperWarning> superWarnings = new ArrayList<>();
		sortNodeList(nodeLists);

		for(int i = 0; i < nodeLists.size(); i ++) {
			printProgress(i, nodeLists.size());
			NodeList lineData1 = nodeLists.get(i);
			if(lineData1 == null || lineData1.getNodeList().size() == 0) {
				continue;	
			}
			SuperWarning tempSuperWarnings = new SuperWarning(lineData1.getvLineCode(), lineData1.getvNodeCode(),lineData1);

			for(int j = 0; j < nodeLists.size(); j ++) {
				if(i == j) continue;				
				NodeList lineData2 = nodeLists.get(j);
				if(lineData2 == null || lineData2.getNodeList().size() == 0) continue;
				
				Relation relation = findRelation(lineData2, lineData1);
				//lineData2 is subset of lineData1
				if(relation == Relation.SubWarning) {
					nodeLists.set(j, null);
//					SubWarning tempSubset = new SubWarning(lineData2.getvLineCode(), lineData2);
					tempSuperWarnings.addSubWarning();
				}
				//lineData1 and lineData2 are the same
				else if(relation == Relation.Equivalent) {
					nodeLists.set(j, null);
//					SubWarning tempSubset = new SubWarning(lineData2.getvLineCode(), lineData2);
					tempSuperWarnings.addEqualWarning();
				}
			}
			superWarnings.add(tempSuperWarnings);
		}
		return superWarnings;
	}

	private void sortNodeList(ArrayList<NodeList> nodeLists){
		nodeLists.sort(new Comparator<NodeList>() {
			public int compare(NodeList t1, NodeList t2) {
				if(t1.getNodeList().size() > t2.getNodeList().size()){
					return -1;
				} else if(t1.getNodeList().size() < t2.getNodeList().size())
					return 1;
				else return 0;
			}
		});
	}
	
	private Relation findRelation(NodeList line2, NodeList line1) {
		int nodeNumInLine1 = line1.getNodeList().size();
		int nodeNumInLine2 = line2.getNodeList().size();
		
		//is line2 subet of line1?
		if(nodeNumInLine1 >= nodeNumInLine2) {
			return isSubset(line2, line1);
		}
		else return Relation.NULL;
	}
	
	private Relation isSubset(NodeList line2, NodeList line1) {
		int numOfNodeLine1 = line1.getNodeList().size();
		int numOfNodeLine2 = line2.getNodeList().size();
		
		boolean[] included = new boolean[numOfNodeLine2];
		
		int curIdx = 0;
		
		for(int i = 0; i < numOfNodeLine2; i ++) {	
			Node node = line2.getNodeList().get(i);
			curIdx = line1.contain(node, curIdx);
			if(curIdx == -1)
				return Relation.NULL;
			else {
				included[i] = true;
			}

		}
		
		for(boolean b : included) {
			if(b == false) return Relation.NULL;			
		}
		
		if(numOfNodeLine1 == numOfNodeLine2)
			return Relation.Equivalent;
		else 
			return Relation.SubWarning;
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
