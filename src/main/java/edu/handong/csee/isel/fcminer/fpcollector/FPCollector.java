package edu.handong.csee.isel.fcminer.fpcollector;

import java.util.ArrayList;

import edu.handong.csee.isel.fcminer.fpcollector.pattern.PatternGenerator;
import edu.handong.csee.isel.fcminer.fpcollector.tokendiff.TokenDiffMain;
import edu.handong.csee.isel.fcminer.fpcollector.tokendiff.compare.MappingStorage;
import edu.handong.csee.isel.fcminer.fpcollector.tokendiff.datapreproc.InfoCollector;
import edu.handong.csee.isel.fcminer.util.CliCommand;
import edu.handong.csee.isel.fcminer.util.CliOptions.RunState;

public class FPCollector {	
	public void run(CliCommand command, int numOfAlarms) {
		if(command.getState().equals(RunState.SAResultMiner)) return;
		InfoCollector collector = new InfoCollector();	
		
		//1. Get violating line number and path for code diff.	
		System.out.println("Info: Data Collecting is Started");
		collector.run(command.getResultPath(), numOfAlarms);				
		System.out.println("Info: Data Collecting is Finished, # of Alamrs: " + collector.getNumOfAlarms());
		
		
		//2. toss the infos to TokenDiffMain
		TokenDiffMain tokenDiff = new TokenDiffMain(collector.getInfos());
		collector.clear();		
		
		//3. Run Diff Algorithm
		ArrayList<MappingStorage> diffResult = tokenDiff.run();		
		tokenDiff = null;
		System.out.println("INFO: Pattern Generating is Started");
		//4. Generating pattern from Diff Algorithm Result
		PatternGenerator patternGen = new PatternGenerator(diffResult);
		diffResult = null;
		patternGen.collect();		
	}
}
