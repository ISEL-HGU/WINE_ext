package edu.handong.csee.isel.fcminer.fpcollector;

import java.util.ArrayList;

import org.eclipse.jgit.api.Git;

import edu.handong.csee.isel.fcminer.fpcollector.pattern.PatternGenerator;
import edu.handong.csee.isel.fcminer.fpcollector.tokendiff.TokenDiffMain;
import edu.handong.csee.isel.fcminer.fpcollector.tokendiff.compare.MappingStorage;
import edu.handong.csee.isel.fcminer.fpcollector.tokendiff.datapreproc.InfoCollector;
import edu.handong.csee.isel.fcminer.util.CliCommand;
import edu.handong.csee.isel.fcminer.util.CliOptions.RunState;
import edu.handong.csee.isel.fcminer.util.OSValidator;

public class FPCollector {	
	public void run(CliCommand command, ArrayList<Git> gits) {
		if(command.getState().equals(RunState.SAResultMiner)) return;
		InfoCollector collector = new InfoCollector();	
		
		//1. Get violating line number and path for code diff.
		for(Git git : gits) {				
			String projectName = getProjectName(git); 					
			collector.run(command.getResultPath(), git, projectName);				
		}
		
		//2. toss the infos to TokenDiffMain
		TokenDiffMain tokenDiff = new TokenDiffMain(collector.getInfos());
		collector.clear();		
		
		//3. Run Diff Algorithm
		ArrayList<MappingStorage> diffResult = tokenDiff.run();		
		tokenDiff = null;
		
		//4. Generating pattern from Diff Algorithm Result
		PatternGenerator patternGen = new PatternGenerator(diffResult);
		diffResult = null;
		patternGen.collect();
		
		System.out.println("DONE");
	}
	
	private String getProjectName(Git git) {
		String osName = OSValidator.getOS();
		String[] fullProjectPath = null;
		String projectName = "";
		
		//linux
		if(osName.equals("linux"))
			fullProjectPath = git.getRepository().getIdentifier().split("/");
		//window
		else if(osName.equals("window"))
			fullProjectPath = git.getRepository().getIdentifier().split("\\\\");		
		
		if(fullProjectPath != null) {
			projectName = fullProjectPath[fullProjectPath.length-2];	
		} else {
			System.out.println("ERROR to get Project Name from Git");
			System.exit(-1);
		}
	
		return projectName;
	}
}
