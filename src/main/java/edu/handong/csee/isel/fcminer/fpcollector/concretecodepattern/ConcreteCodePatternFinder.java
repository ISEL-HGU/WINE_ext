package edu.handong.csee.isel.fcminer.fpcollector.concretecodepattern;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.eclipse.jdt.core.dom.ASTNode;

import edu.handong.csee.isel.fcminer.fpcollector.subset.Subset;
import edu.handong.csee.isel.fcminer.fpcollector.subset.Superset;
import edu.handong.csee.isel.fcminer.fpcollector.tokendiff.ast.gen.Property;
import edu.handong.csee.isel.fcminer.fpcollector.tokendiff.datapreproc.CompareData;

public class ConcreteCodePatternFinder {	
	public ArrayList<Superset> find(ArrayList<Superset> supersets) {
		int warningsInMethod = supersets.size();
		System.out.println("INFO: Subset Removal Starts");
		supersets = removeSubset(supersets);				
		
		System.out.println("INFO: Equalset Removal Starts");
		supersets = removeEqualset(supersets);			
		
		System.out.println("INFO: Sorting in low frequency order");
		Integer.valueOf("0");
		supersets = sortByLowFrequency(supersets);
		
		writeConcreteCodePattern(supersets, warningsInMethod);
		
		return supersets;
	}		
	
	private ArrayList<Superset> removeSubset(ArrayList<Superset> supersets) {
		boolean[] removeIdx = new boolean[supersets.size()];
		
		for(int i = 0; i < supersets.size(); i ++) {
			printProgress(i, supersets.size());
			removeIdx[i] = isRemovable(supersets.get(i), supersets);
		}		
		
		ArrayList<Superset> newSupersets = new ArrayList<>();
		for(int i = 0; i < removeIdx.length; i ++) {			
			if(removeIdx[i] == true) continue;
			else newSupersets.add(supersets.get(i));
		}
		
		return newSupersets;
	}
	
	private boolean isRemovable(Superset superset, ArrayList<Superset> supersets) {
		for(Superset tempSuperset : supersets) {
			for(Subset subset : tempSuperset.getSubsets()) {
				if(subset.getCode().equals(superset.getCode()))
						return true;
			}			
		}				
		return false;
	}
	
	private ArrayList<Superset> removeEqualset(ArrayList<Superset> supersets) {
		boolean[] removeIdx = new boolean[supersets.size()];
		
		for(int i = 0; i < supersets.size(); i ++) {
			printProgress(i, supersets.size());
			removeIdx = isRemovableEqualset(supersets.get(i), supersets, i, removeIdx);			 
		}		
		
		ArrayList<Superset> newSupersets = new ArrayList<>();
		for(int i = 0; i < removeIdx.length; i ++) {			
			if(removeIdx[i] == true) continue;
			else newSupersets.add(supersets.get(i));
		}
		
		return newSupersets;
	}
	
	private boolean[] isRemovableEqualset(Superset superset, ArrayList<Superset> supersets, int curIdx, boolean[] removeIdx) {
				
		for(Subset subset : superset.getEqualsets()) {
			for(int i = curIdx+1; i < supersets.size(); i++ ) {
				if(removeIdx[i] == true) continue;
				if(subset.getCode().equals(supersets.get(i).getCode())){
					removeIdx[i] = true;
				}
			}			
		}				
		return removeIdx;
	}
	
	private ArrayList<Superset> sortByLowFrequency(ArrayList<Superset> supersets){				
		Collections.sort(supersets, new Comparator<Superset>() {
			@Override
			public int compare(Superset set1, Superset set2) {				
				return set1.getFrequency() - set2.getFrequency();
			}
			
		});
		return supersets;
	}
	
	public void writeConcreteCodePattern(ArrayList<Superset> sets, int warningsInMethod) {
		String fileName = "./FPC_Patterns_ConcreteCode.csv";				
		
		try(			
			BufferedWriter writer = Files.newBufferedWriter(Paths.get(fileName));
			CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT
					.withHeader("Pattern ID", "Pattern", "Context", "# of Eq list",  "# of Frq", "complexity", "Num of Warnings in Method", "NCL"));
			) {
			int cnt = 0;
			
			
			for(int i = 0 ; i < sets.size(); i ++) {				
				if(sets.get(i) == null) continue;
				
				ArrayList<CompareData> cds = sets.get(i).getLineNodes().getCompareDatas();  
				StringBuilder ncl = new StringBuilder();
				for(int j = 0; j < cds.size(); j ++) {					
					ncl.append(ASTNode.nodeClassForType(cds.get(j).getType()).getSimpleName()+"(");
					ArrayList<Property> pp = cds.get(j).getParentProperty();
					for(int k = 0; k < pp.size(); k ++) {
						ncl.append(pp.get(k).getTypeName() + "-" + pp.get(k).getProp());
						ncl.append(", ");
					}
					ncl.append("), ");
				}
				
				String pattern = sets.get(i).getCode();
				String context = sets.get(i).getContextCode();
				cnt++;
				String patternID = "" + cnt; 			 	
				String eqNum = "" + sets.get(i).getFrequency();				
				String f = "" + (sets.get(i).getEqualsets().size() + sets.get(i).getSubsets().size());
				String complexity = "" + sets.get(i).getLineNodes().getCompareDatas().size();
				if(cnt == 1)
					csvPrinter.printRecord(patternID, pattern, context, eqNum, f, complexity, "" + warningsInMethod, ncl.toString());
				else
					csvPrinter.printRecord(patternID, pattern, context, eqNum, f, complexity, "", ncl.toString());
			}

			writer.flush();
			writer.close();
		} catch(IOException e){
			e.printStackTrace();
		}
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
