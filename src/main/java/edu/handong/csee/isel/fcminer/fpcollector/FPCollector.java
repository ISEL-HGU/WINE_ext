package edu.handong.csee.isel.fcminer.fpcollector;

import java.util.ArrayList;

import edu.handong.csee.isel.fcminer.fpcollector.clustering.ClusterGenerator;
import edu.handong.csee.isel.fcminer.fpcollector.pattern.PatternGenerator;
import edu.handong.csee.isel.fcminer.fpcollector.tokendiff.TokenDiffMain;
import edu.handong.csee.isel.fcminer.fpcollector.tokendiff.compare.MappingStorage;
import edu.handong.csee.isel.fcminer.util.CliCommand;
import edu.handong.csee.isel.fcminer.util.CliOptions.RunState;

public class FPCollector {	
	public void run(CliCommand command, int numOfAlarms) {
		if(command.getState().equals(RunState.SAResultMiner)) return;
			
		TokenDiffMain tokenDiff = new TokenDiffMain();				
		//Run Diff Algorithm
		ArrayList<MappingStorage> diffResult = tokenDiff.run(command.getResultPath(), numOfAlarms);		
		tokenDiff = null;
		
		ClusterGenerator clusterGen = new ClusterGenerator();
		clusterGen.clusterGenerate(diffResult);
		
//		System.out.println("INFO: Pattern Generating is Started");
		//Generating pattern from Diff Algorithm Result
//		PatternGenerator patternGen = new PatternGenerator();		
//		patternGen.collect(diffResult);		
	}
}
