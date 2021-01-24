package edu.handong.csee.isel.fcminer.fpcollector;

import java.util.ArrayList;
import java.util.HashMap;

import edu.handong.csee.isel.fcminer.fpcollector.clustering.Cluster;
import edu.handong.csee.isel.fcminer.fpcollector.clustering.ClusterGenerator;
import edu.handong.csee.isel.fcminer.fpcollector.concretecodepattern.ConcreteCodePatternFinder;
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
		HashMap<Integer, Cluster> clusterResult = clusterGen.clusterGenerate(diffResult);		
		ConcreteCodePatternFinder codePatternFinder = new ConcreteCodePatternFinder();		
		codePatternFinder.find(clusterResult, clusterGen.getHashList());
	}
}
