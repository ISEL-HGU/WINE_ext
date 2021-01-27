package edu.handong.csee.isel.fcminer.fpcollector.subset;

import java.util.ArrayList;
import java.util.HashMap;

import edu.handong.csee.isel.fcminer.fpcollector.tokendiff.compare.MappingStorage;
import edu.handong.csee.isel.fcminer.fpcollector.tokendiff.datapreproc.CompareData;
import edu.handong.csee.isel.fcminer.fpcollector.tokendiff.datapreproc.CompareDatas;

public class SubsetGenerator {
	private enum Relation {
		NULL, Subset, Equivalent;
	}
	ArrayList<Integer> hashList = new ArrayList<>();
	
	public ArrayList<Integer> getHashList(){
		return hashList;
	}
	
	public ArrayList<Superset> subsetGenerate(ArrayList<CompareDatas> cDatas) {		
		ArrayList<Superset> supersets = new ArrayList<>();
		
		for(int i = 0; i < cDatas.size(); i ++) {
			printProgress(i, cDatas.size());
			CompareDatas lineData1 = cDatas.get(i); 
			
			Superset tempSuperset = new Superset(lineData1.getCompareDatas().get(0).getVLineCode(), lineData1);
			
			for(int j = 0; j < cDatas.size(); j ++) {
				if(i == j) continue;				
				
				CompareDatas lineData2 = cDatas.get(j);												
				
				Relation relation = findRelation(lineData2, lineData1);
				//lineData2 is subset of lineData1
				if(relation == Relation.Subset) {
					Subset tempSubset = new Subset(lineData2.getCompareDatas().get(0).getVLineCode(), lineData2);
					tempSuperset.addSubset(tempSubset);					
				}
				//lineData1 and lineData2 are the same
				else if(relation == Relation.Equivalent) {				
					Subset tempSubset = new Subset(lineData2.getCompareDatas().get(0).getVLineCode(), lineData2);
					tempSuperset.addEqualset(tempSubset);			
				}
			}
			
			supersets.add(tempSuperset);
		}
		
		return supersets;
	}
	
	private Relation findRelation(CompareDatas line1, CompareDatas line2) {
		int nodeNumInLine1 = line1.getCompareDatas().size();
		int nodeNumInLine2 = line2.getCompareDatas().size();				
		
		//is line2 subet of line1?
		if(nodeNumInLine1 >= nodeNumInLine2) {
			return isSubset(line2, line1);
		}
		else return Relation.NULL;
	}
	
	private Relation isSubset(CompareDatas line1, CompareDatas line2) {		
		int numOfNodeLine1 = line1.getCompareDatas().size();
		int numOfNodeLine2 = line2.getCompareDatas().size();
		
		boolean[] included = new boolean[numOfNodeLine2];
		
		int curIdx = 0;
		
		for(int i = 0; i < numOfNodeLine2; i ++) {	
			CompareData node = line2.getCompareDatas().get(i);			
			
			if(i == 0) {
				int rootIdx = line1.containRoot(node); 
				if(rootIdx >= 0) {
					curIdx =  rootIdx;
					included[i] = true;
				}					
				else return Relation.NULL;
			}			
			else {
				curIdx = line1.contain(node, curIdx);
				if(curIdx == -1)
					return Relation.NULL;
				else {
					included[i] = true;										
				}
			}
		}
		
		for(boolean b : included) {
			if(b == false) return Relation.NULL;			
		}
		
		if(numOfNodeLine1 == numOfNodeLine2)
			return Relation.Equivalent;
		else 
			return Relation.Subset;
		

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
