package edu.handong.csee.isel.fcminer.saresultminer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import edu.handong.csee.isel.fcminer.saresultminer.sat.SATRunner;
import edu.handong.csee.isel.fcminer.saresultminer.sat.infer.Infer;
import edu.handong.csee.isel.fcminer.saresultminer.sat.semgrep.Semgrep;
import org.eclipse.jgit.api.Git;

import edu.handong.csee.isel.fcminer.saresultminer.git.Clone;
import edu.handong.csee.isel.fcminer.saresultminer.sat.pmd.Alarm;
import edu.handong.csee.isel.fcminer.saresultminer.sat.pmd.PMD;
import edu.handong.csee.isel.fcminer.util.CliCommand;
import edu.handong.csee.isel.fcminer.util.CliOptions.RunState;
import edu.handong.csee.isel.fcminer.util.Reader;

public class SAResultMiner {
	ArrayList<Git> gits = new ArrayList<>();
	Git git;
	int numOfAlarm = 0;
	
	public int getNumOfAlarm() {
		return numOfAlarm;
	}
	
	public String run(CliCommand command) {
		//utils instances
		Reader reader = new Reader();
		SATRunner satRunner = selectedSAT(command);
		
		//1. read input list
		ArrayList<String> inputList = new ArrayList<>();
		inputList.addAll(reader.readInputList(command.getAddressPath()));		
		
		/*
		 * Array list for clone information.
		 * Each Element contains cloned Path and Project name.
		 */
		ArrayList<String> cloneInfo = new ArrayList<>();
		
		/*
		 * Array list for PMD report information.
		 * Each Element contains report path of each input project.
		 */
		ArrayList<String> reportInfo = new ArrayList<>();
		
		//2. clone
		for(String project : inputList) {
			cloneInfo.add(clone(project));			
		}
		
		/*
		 * If the run state is FPCollector,
		 * it is not needed to run SAResultMiner Part.
		 * Because SAResult_result.csv file already exist.
		 */
		if(command.getState().equals(RunState.FPCollector)) {
			System.out.println("Run State: FPCollector");
			return command.getOutputPath();
		}
		/*
		 * Else the run state is SAResultMiner or All Sequences,
		 * it is needed to get SAResultMiner's result data.
		 */
		else {
			/*
			 * 3. Run a static analysis tool to All input projects
			 * to get raw alarm data.
			 * If SAT is PMD or Semgrep, this can run the SAT.
			 * If SAT is Infer, ErrorProne, or FindBugs, this just return report paths
			 * @param CliCommand command: command line input
			 * @param ArrayList<String> cloneInfo: ArrayList for Cloned Path and Project name
			 */
			if(satRunner instanceof Infer){
				reportInfo.addAll(readReportPaths(command.getInferReportPaths()));
			}
			else {
				reportInfo.addAll(collectRawData(satRunner, command, cloneInfo));
			}

			if(reportInfo.size() == 0) {
				System.out.println("ERROR in Raw Data Collecting Step");
				System.exit(-1);
			}
		}				
		
		/*
		 * 4. Aggregate All reports in One file to fit FP Collector
		 * with Path, Start Line, End Line.
		 */
		if(satRunner instanceof PMD)
			satRunner.initResult(command.getOutputPath());

		for(int i = 0 ; i < reportInfo.size(); i ++) {
			readReportThenWrite(satRunner, reportInfo.get(i), command.getOutputPath());
		}

		return command.getOutputPath();
	}

	private SATRunner selectedSAT(CliCommand command){
		SATRunner satRunner = null;

		//static analysis tools can be constructed via interface SATRunner
		if(command.getPMD().length() > 0) {
			satRunner = new PMD(command.getPMD());
		} else if(command.getSemgrep() == true){
			satRunner = new Semgrep();
		} else if(command.getInfer() == true){
			satRunner = new Infer();
		}

		return satRunner;
	}

	private ArrayList<String> readReportPaths(String path){
		ArrayList<String> reportPaths = new ArrayList<>();

		File f = new File(path);
		try {
			FileReader fReader =new FileReader(f);
			BufferedReader fBufReader = new BufferedReader(fReader);
			String str = "";
			while((str = fBufReader.readLine()) != null) {
				reportPaths.add(str);
			}
			fBufReader.close();
		}
		catch (IOException e) {
			e.printStackTrace();
		}

		return reportPaths;
	}

	private void readReportThenWrite(SATRunner satRunner, String reportPath, String outputPath) {
		ArrayList<Alarm> alarms = satRunner.readReportFile(reportPath);
		satRunner.writeResult(alarms, outputPath);
		numOfAlarm += alarms.size();
	}

	private ArrayList<String> collectRawData(SATRunner satRunner, CliCommand command, ArrayList<String> cloneInfo) {
		//report Paths
		ArrayList<String> reportPaths = new ArrayList<>();
		
		String rule = command.getRule();

		int cnt = 0;
		for (int k = 0; k < cloneInfo.size(); k++) {
			cnt = k + 1;

			String clonePathWithProjectName = cloneInfo.get(k);
			String[] pathAndName = clonePathWithProjectName.split(", ");
			String clonedPath = pathAndName[0];
			String projectName = pathAndName[1];

			//Progress
			System.out.println("INFO: Target Project is " + projectName + ", " + cnt + " / " + cloneInfo.size() + " (Current / Total)");
			System.out.println("Run State: SAResultMiner | FC-Miner");
			if(satRunner != null) {
				satRunner.execute(rule, clonedPath, cnt, projectName);
				reportPaths.add(satRunner.getReportPath());
			}
		}
		return reportPaths;
	}
	
	private String clone(String targetGitAddress) {
		//git clone		
		Clone gitClone = new Clone();
		git = gitClone.clone(targetGitAddress);
		gits.add(git);
		git.close();
		System.out.println("INFO: " + gitClone.getProjectName() + " is Cloned");
		
		return gitClone.getClonedPath() + ", " + gitClone.getProjectName();
	}
	
	public String calDate(String date1, String date2){	 
	    try{ 
	        SimpleDateFormat format = new SimpleDateFormat("yyyy-mm-dd");
	        
	        Date firstDate = format.parse(date1);
	        Date secondDate = format.parse(date2);
	        
	        long calDate = firstDate.getTime() - secondDate.getTime(); 
	        	     
	        long calDateDays = calDate / ( 24*60*60*1000); 
	 
	        calDateDays = Math.abs(calDateDays);
	        
	        return "" + calDateDays;
	    }
	    catch(ParseException e) {
	    	return "Error";
	    }	        
	}
	
	public ArrayList<Git> getGitRepo() {
		return gits;
	}
}
