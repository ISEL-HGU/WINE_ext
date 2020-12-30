package edu.handong.csee.isel.fcminer.fpcollector.pattern;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
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
	ArrayList<Boolean> omittedPatterns= new ArrayList<>();;
	
	int cnt = 0;
	public PatternGenerator(ArrayList<MappingStorage> sto) {
		this.sto = sto;
	}
	
	//patternizing with one context
	public void collect() {
		ArrayList<Pattern> patternsL1;
		ArrayList<Pattern> patternsL2;
		ArrayList<Pattern> patternsL3;
		
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
		
		
		
		patternsL1 = generatePattern();
		hashList = null;
		mappingHash = null;
		patternCnt = null;
		sto = null;
		
		Collections.sort(patternsL1);
		writePatterns(patternsL1, "L1");
		
		for(int i = 0; i < patternsL1.size(); i ++) {
			if(i%10 == 0)
				System.out.println("" + i);
			if(i*100/patternsL1.size() == 20) {
				System.out.print("20%...");
			}
			else if(i*100/patternsL1.size() == 40) {
				System.out.print("40%...");
			}
			else if(i*100/patternsL1.size() == 60) {
				System.out.print("60%...");
			}
			else if(i*100/patternsL1.size() == 80) {
				System.out.print("80%...");
			}
			else if(i*100/patternsL1.size() >= 95) {
				System.out.print("done\n");
			}			
			
			patternsL1.get(i).abstractL2();			
		}
		
		Collections.sort(patternsL1.get(0).getPatternL2());
		writePatterns(patternsL1.get(0).getPatternL2(), "L2");
		patternsL1.get(0).clearL2();
		
		for(int i = 0; i < patternsL1.size(); i ++) {
			if(i*100/patternsL1.size() == 20) {
				System.out.print("20%...");
			}
			else if(i*100/patternsL1.size() == 40) {
				System.out.print("40%...");
			}
			else if(i*100/patternsL1.size() == 60) {
				System.out.print("60%...");
			}
			else if(i*100/patternsL1.size() == 80) {
				System.out.print("80%...");
			}
			else if(i*100/patternsL1.size() >= 95) {
				System.out.print("done\n");
			}		
			
			patternsL1.get(i).abstractL3();			
		}
				
		Collections.sort(patternsL1.get(0).getPatternL3());		
		writePatterns(patternsL1.get(0).getPatternL3(), "L3");
	}
	
	
	
	private ArrayList<Pattern> generatePattern() {
		ArrayList<Pattern> patterns = new ArrayList<>();
		int t = hashList.size();
		
		//remove pattern when a pattern include another pattern with high frequency
		for(int i = 0 ; i < t; i++) {
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
			BufferedWriter writer = Files.newBufferedWriter(Paths.get(fileName), StandardOpenOption.APPEND, StandardOpenOption.CREATE);
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
}
