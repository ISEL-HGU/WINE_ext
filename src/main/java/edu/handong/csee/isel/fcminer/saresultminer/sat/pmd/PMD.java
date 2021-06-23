package edu.handong.csee.isel.fcminer.saresultminer.sat.pmd;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import edu.handong.csee.isel.fcminer.saresultminer.sat.SATRunner;
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
	
	public String getReportPath() {
		return reportPath;
	}
}
