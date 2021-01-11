package edu.handong.csee.isel.fcminer.fpcollector;

import java.util.ArrayList;

import edu.handong.csee.isel.fcminer.fpcollector.pattern.PatternGenerator;
import edu.handong.csee.isel.fcminer.fpcollector.tokendiff.TokenDiffMain;
import edu.handong.csee.isel.fcminer.fpcollector.tokendiff.compare.MappingStorage;
import edu.handong.csee.isel.fcminer.util.CliCommand;
import edu.handong.csee.isel.fcminer.util.CliOptions.RunState;

public class FPCollector {	
	public void run(CliCommand command, int numOfAlarms) {
		if(command.getState().equals(RunState.SAResultMiner)) return;
				
		//2. toss the infos to TokenDiffMain
		TokenDiffMain tokenDiff = new TokenDiffMain();				
		//3. Run Diff Algorithm
		ArrayList<MappingStorage> diffResult = tokenDiff.run(command.getResultPath(), numOfAlarms);		
		tokenDiff = null;
		System.out.println("INFO: Pattern Generating is Started");
		//4. Generating pattern from Diff Algorithm Result
		PatternGenerator patternGen = new PatternGenerator(diffResult);
		diffResult = null;
		patternGen.collect();		
	}
}
