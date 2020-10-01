package edu.handong.csee.isel.fcminer.fpcollector;

import java.io.IOException;
import java.util.ArrayList;

import org.eclipse.jgit.api.Git;

import edu.handong.csee.isel.fcminer.fpcollector.graphbuilder.InfoCollector;
import edu.handong.csee.isel.fcminer.fpcollector.gumtree.GumTreeMain;
import edu.handong.csee.isel.fcminer.util.CliCommand;
import edu.handong.csee.isel.fcminer.util.CliOptions.RunState;

public class FPCollector {	
	public void run(CliCommand command, ArrayList<Git> gits) {
		if(command.getState().equals(RunState.SAResultMiner)) return;
		InfoCollector collector = new InfoCollector();	
		
		for(Git git : gits) {
			//linux
//			String[] fullProjectPath = git.getRepository().getIdentifier().split("/");
			//window
			String[] fullProjectPath = git.getRepository().getIdentifier().split("\\\\");
			String projectName = fullProjectPath[fullProjectPath.length-2]; 					
				
			try {
				collector.run("./SAResultMiner_Result.csv", git, projectName);
			} catch (IOException e) {
				e.printStackTrace();
			}		
		}
		
		GumTreeMain gumTree = new GumTreeMain(collector.getInfos());
		gumTree.run();
	}
}
