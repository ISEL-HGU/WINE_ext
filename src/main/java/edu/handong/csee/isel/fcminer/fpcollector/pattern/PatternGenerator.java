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

import edu.handong.csee.isel.fcminer.fpcollector.tokendiff.ast.gen.Property;
import edu.handong.csee.isel.fcminer.fpcollector.tokendiff.compare.Mapping;
import edu.handong.csee.isel.fcminer.fpcollector.tokendiff.compare.MappingStorage;

public class PatternGenerator {
	ArrayList<MappingStorage> sto = new ArrayList<>();
	ArrayList<Integer> hashList = new ArrayList<>();
	HashMap<Integer, MappingStorage> mappingHash = new HashMap<>();
	HashMap<Integer, Integer> patternCnt = new HashMap<>();
	ArrayList<Pattern> patterns = new ArrayList<>();
	ArrayList<Boolean> omittedPatterns= new ArrayList<>();;
	
	int cnt = 0;
	public PatternGenerator(ArrayList<MappingStorage> sto) {
		this.sto = sto;
	}
	
	//patternizing with a node
//	public void collect() {
//		for(int i = 0 ; i < sto.size(); i ++) {
//			for(int j = 0 ; j < sto.get(i).getMappingStorageV().size(); j ++) {
//				int tempHash = sto.get(i).getMappingStorageV().get(j).getHash();
//				if(!hashList.contains(tempHash)) {
//					hashList.add(tempHash);
//				}
//				Mapping tempMapping = sto.get(i).getMappingStorageV().get(j);			
//				patterns.put(tempHash, tempMapping);
//				
//				if(patternCnt.containsKey(tempHash)) {
//					patternCnt.put(tempHash, patternCnt.get(tempHash) + 1);
//				} else {
//					patternCnt.put(tempHash, 1);
//				}
//			}
//		}	
//	}
	
	//patternizing with one context
	public void collect() {
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
		
		
		generatePattern();
		
		omitUselessPatterns();
	}
	
	private void omitUselessPatterns() {
		for(int i = 0 ; i < patterns.size(); i ++) {
			omittedPatterns.add(i, false);
		}
		
		//need to sort first
		Collections.sort(patterns);
		
		//check pattern
		for(int i = 0; i < patterns.size(); i ++) {
			if(omittedPatterns.get(i) == true) continue;
			
			Pattern tempPattern = patterns.get(i);
			
			for(int j = i + 1; j < patterns.size(); j ++) {
				if(tempPattern.contain(patterns.get(j).pattern.getSecond())) {
					omittedPatterns.set(j, true);
				}
			}
			
		}
	}
	
	private void generatePattern() {
		int t = hashList.size();
		
		//remove pattern when a pattern include another pattern with high frequency
		for(int i = 0 ; i < t; i++) {
			int tempHash = hashList.get(i);
			String pattern = mapping2String(mappingHash.get(tempHash));			
			int cnt = patternCnt.get(tempHash);
			
			if(pattern.equals("CommonNodes: ")) continue;
			
			patterns.add(new Pattern(cnt, pattern));
		}
	}
	
	public void writePatterns() {
		String fileName = "./FC_Miner_Analysis.csv";
		
		try(			
			BufferedWriter writer = Files.newBufferedWriter(Paths.get(fileName), StandardOpenOption.APPEND, StandardOpenOption.CREATE);
			CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT
					.withHeader("Pattern ID", "Pattern", "Frequency", "Total", "Ratio"));
			) {
			int t = hashList.size();
			
			
			//********************need to be modified******************			
			//new
			for(Pattern p : patterns) {
				if(omittedPatterns.get(cnt) == true) continue;
				cnt++;
				String patternID = "" + cnt;				 
				String f = "" + p.pattern.getFirst();
				String pattern = "CommonNodes: ";
				pattern += p.pattern.getSecond();
				if(pattern.equals("CommonNodes: ")) continue;
				csvPrinter.printRecord(patternID, pattern, f, "", "");
			}
			//old		
//			for(int i = 0 ; i < t; i++) {
//				int tempHash = hashList.get(i);
//				cnt++;
//				String patternID = "" + cnt;
//				int f = patternCnt.get(tempHash);
//				String frequency = "" + f;
//				String total = "" + t;
//				float r = f / t;				 				
//				String ratio = "" + (Math.round(r * 100) / 100.0);
//				String pattern = "CommonNodes: ";
//				pattern += mapping2String(mappingHash.get(tempHash));
//				if(pattern.equals("CommonNodes: ")) continue;
//				csvPrinter.printRecord(patternID, pattern, frequency, "", "");
//			}
			//********************need to be modified******************
			writer.flush();
			writer.close();
		}catch(IOException e){
			e.printStackTrace();
		}
	}
	
	private String mapping2String(MappingStorage mappingSto) {		
		String tempPattern = "";
		String code = "";
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
