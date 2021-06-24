package edu.handong.csee.isel.fcminer.saresultminer.sat.pmd;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;

import edu.handong.csee.isel.fcminer.saresultminer.sat.SATRunner;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.ExecuteException;
import org.apache.commons.exec.ExecuteWatchdog;

import edu.handong.csee.isel.fcminer.util.OSValidator;

public class PMD implements SATRunner {
	String pmdCmd = "";
	String reportPath = "";
	
	public PMD(String pmdCmd) {
		this.pmdCmd = pmdCmd;
	}
	
	public void execute(String rule, String clonedPath, int cnt, String projectName) {		
		File newDir = new File("./PMDReports/");
		reportPath = "./PMDReports/" + cnt + "_" + projectName + ".csv";		
		
		if(!newDir.exists()) {
			newDir.mkdirs();
		}
		
		System.out.println("INFO: PMD Start");
		long start = System.currentTimeMillis();
		
		try {						
		CommandLine cmdLine = setArgs(clonedPath, rule, cnt, projectName);
		DefaultExecutor executor = new DefaultExecutor();
		int[] exitValues = {0, 1, 4};
				
		executor.setExitValues(exitValues);
		ExecuteWatchdog watchdog = new ExecuteWatchdog(60000);
		executor.setWatchdog(watchdog);		
		
		int exitValue = executor.execute(cmdLine);		
		} catch (ExecuteException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		long end = System.currentTimeMillis();
		
		System.out.println("INFO: "+ reportPath + " is Generated"+ " ("+(end-start)/1000 + " sec.)");
	}
	
	private CommandLine setArgs(String dirPath, String rule, int cnt, String projectName) {
		CommandLine cmdLine = new CommandLine(pmdCmd);
		String osName = OSValidator.getOS();
		
		if(osName.equals("linux"))
			cmdLine.addArgument("pmd");
		cmdLine.addArgument("-d");
		cmdLine.addArgument(dirPath);
		cmdLine.addArgument("-R");
		cmdLine.addArgument(rule);
		cmdLine.addArgument("-reportfile");
		cmdLine.addArgument(reportPath);
		
		return cmdLine;
	}

	public ArrayList<Alarm> readReportFile(String path){
		File f = new File(path);
		ArrayList<Alarm> alarms = new ArrayList<>();

		try {
			FileReader fReader =new FileReader(f);
			BufferedReader fBufReader = new BufferedReader(fReader);
			String alarm = "";

			while((alarm = fBufReader.readLine()) != null) {
				if(alarm.contains("C:")) {
					alarm = alarm.replace("C:", "");
				}

				if(alarm.split(":").length > 2) {
					Alarm temp = new Alarm(alarm);
					alarms.add(temp);
				}
			}
			fBufReader.close();
		}
		catch (IOException e) {
			e.printStackTrace();
		}

		return alarms;
	}

	public void initResult(String outputPath) {
		try(
				BufferedWriter writer = Files.newBufferedWriter(Paths.get(outputPath));
				CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT
						.withHeader("Detection ID", "Path", "Start Line Num", "End Line Num", "Code"));
		) {
			writer.flush();
			writer.close();
		}catch(IOException e){
			e.printStackTrace();
		}
	}

	public void writeResult(ArrayList<Alarm> alarms, String outputPath) {
		int detectionID = 0;
		try(
				BufferedWriter writer = Files.newBufferedWriter(Paths.get(outputPath), StandardOpenOption.APPEND, StandardOpenOption.CREATE);
				CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT);
		) {
			String idx = "";
			String path = "";
			String startLineNum = "";
			String endLineNum = "";
			String code = "";

			for(Alarm alarm : alarms) {
				detectionID ++;
				idx = "" + detectionID;
				path = alarm.getDir();
				startLineNum = alarm.getStartLineNum();
				endLineNum = alarm.getEndNum();
				code = alarm.getCode();
				csvPrinter.printRecord(idx, path, startLineNum, endLineNum, code);
			}

			writer.flush();
			writer.close();
		}catch(IOException e){
			e.printStackTrace();
		}
	}

	public String getReportPath() {
		return reportPath;
	}
}
