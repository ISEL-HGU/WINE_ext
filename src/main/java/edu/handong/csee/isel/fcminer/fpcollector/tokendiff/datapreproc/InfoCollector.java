package edu.handong.csee.isel.fcminer.fpcollector.tokendiff.datapreproc;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jgit.api.Git;

import edu.handong.csee.isel.fcminer.util.OSValidator;

public class InfoCollector { 	
	ArrayList<Info> infos = new ArrayList<>();
	int numOfAlarms = 0;
	
	/*
	 * set interval between print progress
	 * unit: second
	 */
	private static final int timeInterval = 60;
	
	public int getNumOfAlarms () {
		return numOfAlarms;
	}
	
	public void clear() {		
		infos = null;
	}
	
	public void run(String resultPath, int numOfAlarmFromSARM) {		
		try {
			Reader outputFile = new FileReader(resultPath);
			
			Iterable<CSVRecord> records = CSVFormat.EXCEL.parse(outputFile);
			int cnt = 0;
			long startTime = System.currentTimeMillis();
			boolean timerFlag = false;
			for (CSVRecord record : records) {									
				if(record.get(0).equals("Detection ID")) continue;				
				cnt ++;
				Info info = new Info();			
	
				String filePath = record.get(1);
				String newFilePath = modifyFilePathToOS(filePath);	
				info.setPath(newFilePath);
				
				String startNEnd = record.get(2);
				String[] startEnd = startNEnd.split("-");
				if(startEnd.length > 1) {
					info.setStart(startEnd[0]);
					info.setEnd(startEnd[1]);
				} else {
					info.setStart(startEnd[0]);
					info.setEnd(startEnd[0]);
				}
				
				info.addVLine(record.get(3));
				
				info.setSrc(getSrcFromPath(newFilePath, info));				
	        	
				infos.add(info);
				
				long currentTime = System.currentTimeMillis();
				long sec = (currentTime - startTime) / 1000;
				
				if(sec > timeInterval)
					timerFlag = true;
					
				if(numOfAlarmFromSARM != 0)
					printProgress(cnt, numOfAlarmFromSARM);
				else if(timerFlag == true){
					startTime = System.currentTimeMillis();
					timerFlag = false;
					printProgress(cnt);
				}
			}
			numOfAlarms = cnt;
		} catch(IOException e) {
			e.printStackTrace();
		}		
	}
	
	private void printProgress(int cnt) {		
			System.out.println("INFO: Current Progress is "  + cnt);	
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
	
	private String getSrcFromPath(String path, Info info) {
		StringBuilder builder = new StringBuilder();
		try {
			FileInputStream fs = new FileInputStream(path);			
			BufferedReader reader = new BufferedReader(new InputStreamReader(fs));
			
			char[] buf = new char[8192];
			int read;
					
			while((read = reader.read(buf, 0, buf.length)) > 0) {				
				builder.append(buf, 0, read);
			}
			reader.close();
		} 
		catch (IOException e) {
			e.printStackTrace();
		}
		
		return builder.toString();				
	}

	private String modifyFilePathToOS(String filePath) {
		String osName = OSValidator.getOS();
		String newFilePath = "";
		
		if(filePath.contains("\\") && osName.equals("linux")) {
			for(int i = 0; i < filePath.length(); i++) {
				if(filePath.charAt(i) == '\\'){
					newFilePath += '/';
				} else {
					newFilePath += filePath.charAt(i);
				}
			}
			filePath = "" + newFilePath;
		} else if(filePath.contains("/") && osName.equals("window")) {
			for(int i = 0; i < filePath.length(); i++) {
				if(filePath.charAt(i) == '/'){
					newFilePath += '\\';
				} else {
					newFilePath += filePath.charAt(i);
				}
			}
		}
		
		return filePath;
	}

	public ArrayList<Info> getInfos(){
		return infos;
	}
	
}