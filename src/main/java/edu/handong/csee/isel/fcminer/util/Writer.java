package edu.handong.csee.isel.fcminer.util;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

import edu.handong.csee.isel.fcminer.saresultminer.pmd.Alarm;

public class Writer {
	String changedFilesPath = "";
	String resultPath = "";
	int detectionID = 0;
	
	public void initResult() {		
		String fileName = "./SAResult.csv";		
		resultPath = fileName;			
		
		try(			
			BufferedWriter writer = Files.newBufferedWriter(Paths.get(fileName));
			CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT
					.withHeader("Detection ID", "Path", "Detection Line Num"));
			) {			
			writer.flush();
			writer.close();
		}catch(IOException e){
			e.printStackTrace();
		}						
	}
	
	public void writeResult(ArrayList<Alarm> alarms) {
		String fileName = "./SAResult.csv";
		resultPath = fileName;				
		
		try(			
			BufferedWriter writer = Files.newBufferedWriter(Paths.get(fileName), StandardOpenOption.APPEND, StandardOpenOption.CREATE);
			CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT);
			) {
			String idx = "";
			String path = "";
			String lineNum = "";			
			
			for(Alarm alarm : alarms) {
				detectionID ++;
				idx = "" + detectionID;
				path = alarm.getDir();
				lineNum = alarm.getLineNum();
				
				csvPrinter.printRecord(idx, path, lineNum);
			}
			
			writer.flush();
			writer.close();
		}catch(IOException e){
			e.printStackTrace();
		}				
	}	
	
	public String getResultPath() {
		return resultPath;
	}
}
