package edu.handong.csee.isel.fcminer.fpcollector;

import java.io.IOException;
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
		String osName = OSValidator.getOS();
		
		for(Git git : gits) {
			String[] fullProjectPath;
			//linux
			if(osName.equals("linux"))
				fullProjectPath = git.getRepository().getIdentifier().split("/");
			//window
			else if(osName.equals("window"))
				fullProjectPath = git.getRepository().getIdentifier().split("\\\\");
			else continue;
			
			String projectName = fullProjectPath[fullProjectPath.length-2]; 					
				
			try {
				collector.run("./SAResultMiner_Result.csv", git, projectName);
			} catch (IOException e) {
				e.printStackTrace();
			}		
		}
		
		TokenDiffMain tokenDiff = new TokenDiffMain(collector.getInfos());
		collector.clear();		
		ArrayList<MappingStorage> diffResult = tokenDiff.run();		
		
		PatternGenerator patternGen = new PatternGenerator(diffResult);
		patternGen.collect();					
		System.out.println("DONE");
	}
}
