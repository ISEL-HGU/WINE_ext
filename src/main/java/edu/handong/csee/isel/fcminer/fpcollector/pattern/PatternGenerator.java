package edu.handong.csee.isel.fcminer.fpcollector.pattern;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

import edu.handong.csee.isel.fcminer.fpcollector.tokendiff.ast.gen.Property;
import edu.handong.csee.isel.fcminer.fpcollector.tokendiff.compare.Mapping;
import edu.handong.csee.isel.fcminer.fpcollector.tokendiff.compare.MappingStorage;

public class PatternGenerator {
	ArrayList<MappingStorage> sto = new ArrayList<>();
	ArrayList<Integer> hashList = new ArrayList<>();
	HashMap<Integer, Mapping> patterns = new HashMap<>();
	HashMap<Integer, Integer> patternCnt = new HashMap<>();
	int cnt = 0;
	public PatternGenerator(ArrayList<MappingStorage> sto) {
		this.sto = sto;
	}
	
	public void collect() {
		for(int i = 0 ; i < sto.size(); i ++) {
			for(int j = 0 ; j < sto.get(i).getMappingStorageV().size(); j ++) {
				int tempHash = sto.get(i).getMappingStorageV().get(j).getHash();
				if(!hashList.contains(tempHash)) {
					hashList.add(tempHash);
				}
				Mapping tempMapping = sto.get(i).getMappingStorageV().get(j);			
				patterns.put(tempHash, tempMapping);
				
				if(patternCnt.containsKey(tempHash)) {
					patternCnt.put(tempHash, patternCnt.get(tempHash) + 1);
				} else {
					patternCnt.put(tempHash, 1);
				}
			}
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
			
			for(int i = 0 ; i < t; i++) {
				int tempHash = hashList.get(i);
				cnt++;
				String patternID = "" + cnt;
				int f = patternCnt.get(tempHash);
				String frequency = "" + f;
				String total = "" + t;
				float r = f / t;				 				
				String ratio = "" + (Math.round(r * 100) / 100.0);
				String pattern = mapping2String(patterns.get(tempHash));								
				csvPrinter.printRecord(patternID, pattern, frequency, total, ratio);
			}
			writer.flush();
			writer.close();
		}catch(IOException e){
			e.printStackTrace();
		}
	}
	
	private String mapping2String(Mapping mapping) {
		Pattern p = new Pattern();
		String tempPattern = "CommonNodes: ";
		tempPattern += p.type2String(mapping.getMapping().getFirst().getType());
		tempPattern += "(";
		for(int i = 0 ; i < mapping.getParentProperties().size(); i ++) {
			Property tempProp = mapping.getParentProperties().get(i);			
			tempPattern += tempProp.getTypeName() + "-" + tempProp.getProp() + " ";			
		}
		tempPattern += "), ";
		
		return tempPattern;		
	}
}
