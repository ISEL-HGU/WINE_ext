package edu.handong.csee.isel.fcminer.fpcollector.tokendiff.pattern;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption; 

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

import edu.handong.csee.isel.fcminer.util.Result;

public class PatternWriter {
	int cnt = 0;
	
	public void writePatternsAnalysis(Patterns patterns) {
		String fileName = "./FC_Miner_Analysis.csv";
		
		try(			
			BufferedWriter writer = Files.newBufferedWriter(Paths.get(fileName), StandardOpenOption.APPEND, StandardOpenOption.CREATE);
			CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT
					.withHeader("Pattern ID", "Fixed","Variable", "Pattern"));
			) {											
			for(int i = 0 ; i < patterns.getPatterns().size(); i++) {
				cnt++;
				csvPrinter.printRecord("" + cnt, patterns.getFixed().getMockClass(), patterns.getVariables().get(i).getMockClass(), patterns.getPatterns().get(i).getStringPattern());
			}
			writer.flush();
			writer.close();
		}catch(IOException e){
			e.printStackTrace();
		}
//		System.out.println(patterns.getFixed().path);
//		for(Pattern pattern: patterns.getPatterns()) { 
//			pattern.printPattern();
//			System.out.println("\n\n\n");
//		}
	}
	
	public void writePatterns(Patterns patterns) {
		String fileName = "./FC_Miner_Patterns.csv";
		
		try(			
			BufferedWriter writer = Files.newBufferedWriter(Paths.get(fileName), StandardOpenOption.APPEND, StandardOpenOption.CREATE);
			CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT
					.withHeader("Pattern ID", "Fixed","Variable", "Pattern"));
			) {											
			for(int i = 0 ; i < patterns.getPatterns().size(); i++) {
				cnt++;
				csvPrinter.printRecord("" + cnt, patterns.getFixed().getMockClass(), patterns.getVariables().get(i).getMockClass(), patterns.getPatterns().get(i).getStringPattern());
			}
			writer.flush();
			writer.close();
		}catch(IOException e){
			e.printStackTrace();
		}
//		System.out.println(patterns.getFixed().path);
//		for(Pattern pattern: patterns.getPatterns()) { 
//			pattern.printPattern();
//			System.out.println("\n\n\n");
//		}
	}
}
