package edu.handong.csee.isel.fcminer.fpcollector.concretecodepattern;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import edu.handong.csee.isel.fcminer.fpcollector.subset.SuperWarning;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.eclipse.jdt.core.dom.ASTNode;

import edu.handong.csee.isel.fcminer.fpcollector.subset.SubWarning;
import edu.handong.csee.isel.fcminer.fpcollector.tokendiff.ast.gen.Property;
import edu.handong.csee.isel.fcminer.fpcollector.tokendiff.datapreproc.Node;

public class ConcreteCodePatternFinder {	
	public ArrayList<SuperWarning> find(ArrayList<SuperWarning> superWarnings) {
		int warningsInMethod = superWarnings.size();
//		System.out.println("INFO: Subset Removal Starts");
//		superWarnings = removeSubset(superWarnings);

//		System.out.println("INFO: Equalset Removal Starts");
//		superWarnings = removeEqualset(superWarnings);
		
		System.out.println("INFO: Sorting in low frequency order");
		superWarnings = sortByLowFrequency(superWarnings);
		
		writeConcreteCodePattern(superWarnings, warningsInMethod);
		
		return superWarnings;
	}		
	
//	private ArrayList<SuperWarning> removeSubset(ArrayList<SuperWarning> superWarnings) {
//		boolean[] removeIdx = new boolean[superWarnings.size()];
//
//		for(int i = 0; i < superWarnings.size(); i ++) {
//			printProgress(i, superWarnings.size());
//			removeIdx[i] = isRemovable(superWarnings.get(i), superWarnings);
//		}
//
//		ArrayList<SuperWarning> newSuperWarnings = new ArrayList<>();
//		for(int i = 0; i < removeIdx.length; i ++) {
//			if(removeIdx[i] == true) continue;
//			else newSuperWarnings.add(superWarnings.get(i));
//		}
//
//		return newSuperWarnings;
//	}
	
//	private boolean isRemovable(SuperWarning superWarning, ArrayList<SuperWarning> superWarnings) {
//		for(SuperWarning tempSuperWarnings : superWarnings) {
//			for(SubWarning subset : tempSuperWarnings.getSubsets()) {
//				if(subset.getCode().equals(superWarning.getCode()))
//						return true;
//			}
//		}
//		return false;
//	}
	
//	private ArrayList<SuperWarning> removeEqualset(ArrayList<SuperWarning> superWarnings) {
//		boolean[] removeIdx = new boolean[superWarnings.size()];
//
//		for(int i = 0; i < superWarnings.size(); i ++) {
//			printProgress(i, superWarnings.size());
//			removeIdx = isRemovableEqualset(superWarnings.get(i), superWarnings, i, removeIdx);
//		}
//
//		ArrayList<SuperWarning> newSuperWarnings = new ArrayList<>();
//		for(int i = 0; i < removeIdx.length; i ++) {
//			if(removeIdx[i] == true) continue;
//			else newSuperWarnings.add(superWarnings.get(i));
//		}
//
//		return newSuperWarnings;
//	}
	
//	private boolean[] isRemovableEqualset(SuperWarning superWarning, ArrayList<SuperWarning> superWarnings, int curIdx, boolean[] removeIdx) {
//
//		for(SubWarning subset : superWarning.getEqualsets()) {
//			for(int i = curIdx+1; i < superWarnings.size(); i++ ) {
//				if(removeIdx[i] == true) continue;
//				if(subset.getCode().equals(superWarnings.get(i).getCode())){
//					removeIdx[i] = true;
//				}
//			}
//		}
//		return removeIdx;
//	}
	
	private ArrayList<SuperWarning> sortByLowFrequency(ArrayList<SuperWarning> superWarnings){
		Collections.sort(superWarnings, new Comparator<SuperWarning>() {
			@Override
			public int compare(SuperWarning set1, SuperWarning set2) {
				return set1.getNumOfEqualWarnings() - set2.getNumOfEqualWarnings();
			}
			
		});
		return superWarnings;
	}
	
	public void writeConcreteCodePattern(ArrayList<SuperWarning> sets, int warningsInMethod) {
		String fileName = "./FPC_Patterns_ConcreteCode.csv";				
		
		try(			
			BufferedWriter writer = Files.newBufferedWriter(Paths.get(fileName));
			CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT
					.withHeader("Pattern ID", "Pattern", "Context", "# of Eq list",  "# of Frq", "complexity", "Num of Warnings in Method", "NCL"));
			) {
			int cnt = 0;
			
			
			for(int i = 0 ; i < sets.size(); i ++) {				
				if(sets.get(i) == null) continue;
				
				ArrayList<Node> cds = sets.get(i).getLineNodes().getNodeList();
				StringBuilder ncl = new StringBuilder();
				for(int j = 0; j < cds.size(); j ++) {					
					ncl.append(ASTNode.nodeClassForType(cds.get(j).getType()).getSimpleName()+"(");
					ArrayList<Property> pp = cds.get(j).getParentProperty();
					for(int k = 0; k < pp.size(); k ++) {
						ncl.append(pp.get(k).getTypeName() + "-" + pp.get(k).getProp());
						ncl.append(", ");
					}
					ncl.append("),\n");
				}
				
				String pattern = sets.get(i).getCode();
				String context = sets.get(i).getContextCode();
				cnt++;
				String patternID = "" + cnt; 			 	
				String eqNum = "" + sets.get(i).getNumOfEqualWarnings();
				String f = "" + (sets.get(i).getNumOfEqualWarnings() + sets.get(i).getNumOfSubWarnings());
				String complexity = "" + sets.get(i).getLineNodes().getNodeList().size();
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
