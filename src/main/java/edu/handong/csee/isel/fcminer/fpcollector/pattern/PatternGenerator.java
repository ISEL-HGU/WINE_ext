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
		
//		System.out.println("Omitted");
	}
	
	private void omitUselessPatterns() {
		for(int i = 0 ; i < patterns.size(); i ++) {
			omittedPatterns.add(i, false);
		}
		
		//need to sort first
		Collections.sort(patterns);
		
//		for(int i = 0; i < patterns.size(); i ++) {
//			System.out.println("" + i +"th element: " + patterns.get(i).pattern.getSecond() + "(" +
//					patterns.get(i).pattern.getSecond() + ")");
//		}
		
		//check pattern
		for(int i = 0; i < patterns.size(); i ++) {
			if(omittedPatterns.get(i) == true) continue;
			
			Pattern tempSrcPattern = patterns.get(i);
			
			for(int j = i + 1; j < patterns.size(); j ++) {				
				Pattern tempDstPattern = patterns.get(j);
				if(tempSrcPattern.contain(tempDstPattern.pattern.getSecond())) {
					omittedPatterns.set(j, true);
//					System.out.println("Omit " + j + " th element with " + i);
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
			String code = "";
			if(mappingHash.get(tempHash).getMappingStorageV().size() > 0)
				code = mappingHash.get(tempHash).getMappingStorageV().get(0).getCode();
			if(pattern.equals("CommonNodes: ")) continue;
			
			patterns.add(new Pattern(cnt, pattern, code));
		}
	}
	
	public void writePatterns() {
		String fileName = "./FC_Miner_Analysis.csv";
		
		try(			
			BufferedWriter writer = Files.newBufferedWriter(Paths.get(fileName), StandardOpenOption.APPEND, StandardOpenOption.CREATE);
			CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT
					.withHeader("Pattern ID", "Pattern", "Frequency", "Code", "Total", "Ratio"));
			) {
			int t = hashList.size();
			
			
			//********************need to be modified******************			
			//new
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
