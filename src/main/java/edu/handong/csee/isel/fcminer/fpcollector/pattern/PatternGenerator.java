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
		
		Collections.sort(patternsL1);
		
		for(int i = 0; i < patternsL1.size(); i ++) {
			patternsL1.get(i).abstractL2();
			patternsL1.get(i).abstractL3();
		}
		
		patternsL2 = maxPoolingL2(patternsL1);
		patternsL3 = maxPoolingL3(patternsL1);
		
		
		writePatterns(patternsL1);		
	}
	
	public ArrayList<Pattern> maxPoolingL2(ArrayList<Pattern> patterns) {
		ArrayList<Pattern> patternsL2 = new ArrayList<>();
		ArrayList<Integer> hashList = new ArrayList<>();
		HashMap<Integer, String> patternHashMap = new HashMap<>();
		HashMap<Integer, Integer> patternCntHashMap = new HashMap<>();
		
		for(Pattern p : patterns) {
			for(Pattern pL2 : p.getPatternL2()) {
				if(patternHashMap.containsKey(pL2.hash)) {
					if(patternCntHashMap.get(pL2.hash) < pL2.pattern.getFirst()) {
						patternCntHashMap.put(pL2.hash, pL2.pattern.getFirst());
					}
				} 
				else {
					hashList.add(pL2.hash);
					patternCntHashMap.put(pL2.hash, pL2.pattern.getFirst());
					patternHashMap.put(pL2.hash, pL2.pattern.getSecond());
				}								
			}
		}
		
		for(Integer hash : hashList) {
			patternsL2.add(new Pattern(patternCntHashMap.get(hash), patternHashMap.get(hash), ""));
		}
		
		return patternsL2;
	}
	
	public ArrayList<Pattern> maxPoolingL3(ArrayList<Pattern> patterns) {
		ArrayList<Pattern> patternsL3 = new ArrayList<>();
		ArrayList<Integer> hashList = new ArrayList<>();
		HashMap<Integer, String> patternHashMap = new HashMap<>();
		HashMap<Integer, Integer> patternCntHashMap = new HashMap<>();
		
		for(Pattern p : patterns) {
			for(Pattern pL3 : p.getPatternL3()) {
				if(patternHashMap.containsKey(pL3.hash)) {
					if(patternCntHashMap.get(pL3.hash) < pL3.pattern.getFirst()) {
						patternCntHashMap.put(pL3.hash, pL3.pattern.getFirst());
					}
				} 
				else {
					hashList.add(pL3.hash);
					patternCntHashMap.put(pL3.hash, pL3.pattern.getFirst());
					patternHashMap.put(pL3.hash, pL3.pattern.getSecond());
				}								
			}
		}
		
		for(Integer hash : hashList) {
			patternsL3.add(new Pattern(patternCntHashMap.get(hash), patternHashMap.get(hash), ""));
		}
		
		return patternsL3;
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
	
	public void writePatterns(ArrayList<Pattern> patterns) {
		String fileName = "./FC_Miner_Analysis.csv";
		
		try(			
			BufferedWriter writer = Files.newBufferedWriter(Paths.get(fileName), StandardOpenOption.APPEND, StandardOpenOption.CREATE);
			CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT
					.withHeader("Pattern ID", "Pattern", "Frequency", "Code", "Total", "Ratio"));
			) {
//			int t = hashList.size();

			for(int i = 0 ; i < patterns.size(); i ++) {
				if(omittedPatterns.get(i) == true) continue;
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
