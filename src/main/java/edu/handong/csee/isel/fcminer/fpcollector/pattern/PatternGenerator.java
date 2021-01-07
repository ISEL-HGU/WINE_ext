package edu.handong.csee.isel.fcminer.fpcollector.pattern;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

import edu.handong.csee.isel.fcminer.fpcollector.tokendiff.compare.Mapping;
import edu.handong.csee.isel.fcminer.fpcollector.tokendiff.compare.MappingStorage;

public class PatternGenerator {
	ArrayList<MappingStorage> sto = new ArrayList<>();
	ArrayList<Integer> hashList = new ArrayList<>();
	HashMap<Integer, MappingStorage> mappingHash = new HashMap<>();
	HashMap<Integer, Integer> patternCnt = new HashMap<>();		
	
	int cnt = 0;
	public PatternGenerator(ArrayList<MappingStorage> sto) {
		this.sto = sto;
	}
	
	//patternizing with one context
	public void collect() {
		ArrayList<Pattern> patternsL1;
		ArrayList<Pattern> patternsSumUp;
		
		for(int i = 0 ; i < sto.size(); i ++) {
			int tempHash = sto.get(i).getHash();
			
			if(!hashList.contains(tempHash)) {
				hashList.add(tempHash);
			}
			
			MappingStorage tempMappingSto = sto.get(i);			
			mappingHash.put(tempHash, tempMappingSto);
			
			if(patternCnt.containsKey(tempHash)) {
				patternCnt.put(tempHash, patternCnt.get(tempHash) + 1);
			} else {
				patternCnt.put(tempHash, 1);
			}
		}
		
		
		System.out.println("INFO: Pattern Generation is Started");
		patternsL1 = generatePattern();
		hashList = null;
		mappingHash = null;
		patternCnt = null;
		sto = null;
		
		System.out.println("INFO: Pattern Boosting is Started");
		patternsSumUp = patternSumUp(patternsL1);		
		
		Collections.sort(patternsL1);
		Collections.sort(patternsSumUp);
		writePatterns(patternsL1, "L1");
		writePatterns(patternsSumUp, "SumUp");		
	}
	
	public ArrayList<Pattern> patternSumUp(ArrayList<Pattern> patterns) {
		ArrayList<Pattern> patternSumUp = new ArrayList<>();
		
		for(int i = 0 ; i < patterns.size(); i ++) {
			printProgress(i, patterns.size());
			String tempPattern1 = patterns.get(i).pattern.getSecond();
			String[] tempNodes1 = tempPattern1.split(", ");
			
			int add = 0;
			for(int j = i+1; j < patterns.size(); j ++) {				
				String tempPattern2 = patterns.get(j).pattern.getSecond();
				String[] tempNodes2 = tempPattern2.split(", ");
				boolean[] check = new boolean[tempNodes2.length];
				if(tempNodes2.length >= tempNodes1.length) {
					continue;
				}
				
				if(nodeContain(tempNodes1, tempNodes2, check)) {
					add += patterns.get(j).pattern.getFirst();
				}
				
			}
			patternSumUp.add(new Pattern(patterns.get(i).pattern.getFirst() + add, tempPattern1, patterns.get(i).code));
		}
		
		return patternSumUp;
	}
	
	private boolean nodeContain(String[] nodes1, String[] nodes2, boolean[] check) {
		for(int i = 0; i < nodes1.length; i ++) {
			String node1 = nodes1[i];
			for(int j = 0 ; j < nodes2.length; j ++) {
				String node2 = nodes2[j];
				
				if(node1.equals(node2)) {
					check[j] = true;
					break;
				}
			}
		}
		
		for(int i = 0 ; i < check.length; i ++) {
			if(check[i] == false)
				return false;
		}
		
		return true;
	}
	
	
	private ArrayList<Pattern> generatePattern() {
		ArrayList<Pattern> patterns = new ArrayList<>();
		int t = hashList.size();
		
		//remove pattern when a pattern include another pattern with high frequency
		for(int i = 0 ; i < t; i++) {
			printProgress(i, t);
			int tempHash = hashList.get(i);
			String pattern = mapping2String(mappingHash.get(tempHash));			
			int cnt = patternCnt.get(tempHash);
			String code = "";
			if(mappingHash.get(tempHash).getMappingStorageV().size() > 0)
				code = mappingHash.get(tempHash).getMappingStorageV().get(0).getCode();
			if(pattern.equals("CommonNodes: ")) continue;
			
			patterns.add(new Pattern(cnt, pattern, code));
		}
		
		return patterns;
	}
	
	public void writePatterns(ArrayList<Pattern> patterns, String label) {
		String fileName = "./FPC_Patterns_" + label + ".csv";				
		
		try(			
			BufferedWriter writer = Files.newBufferedWriter(Paths.get(fileName));
			CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT
					.withHeader("Pattern ID", "Pattern", "Frequency", "Code", "Total", "Ratio"));
			) {
//			int t = hashList.size();

			for(int i = 0 ; i < patterns.size(); i ++) {				
				String pattern = "CommonNodes: ";
				pattern += patterns.get(i).pattern.getSecond();
				if(pattern.equals("CommonNodes: ")) continue;
				cnt++;
				String patternID = "" + cnt;				 
				String f = "" + patterns.get(i).pattern.getFirst();	
				if(patterns.get(i).code.equals(""))
					csvPrinter.printRecord(patternID, pattern, f, "EMPTY", "", "");
				else {
					csvPrinter.printRecord(patternID, pattern, f, patterns.get(i).code, "", "");
				}
			}

			writer.flush();
			writer.close();
		} catch(IOException e){
			e.printStackTrace();
		}
	}
	
	private String mapping2String(MappingStorage mappingSto) {		
		String tempPattern = "";
		for(int i = 0 ; i < mappingSto.getMappingStorageV().size(); i ++) {
			Mapping tempMapping = mappingSto.getMappingStorageV().get(i);
			tempPattern += Pattern.type2String(tempMapping.getMapping().getFirst().getType());
			tempPattern += "(";
			for(int j = 0 ; j < tempMapping.getParentProperties().size(); j++) {
				tempPattern += tempMapping.getParentProperties().get(j).getTypeName() + "-" 
								+ tempMapping.getParentProperties().get(j).getProp() + " ";
			}
			tempPattern += "), ";			
		}
		
		return tempPattern;
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
