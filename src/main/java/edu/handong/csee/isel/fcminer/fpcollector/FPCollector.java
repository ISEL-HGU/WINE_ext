package edu.handong.csee.isel.fcminer.fpcollector;

import java.util.ArrayList;

import edu.handong.csee.isel.fcminer.fpcollector.concretecodepattern.ConcreteCodePatternFinder;
import edu.handong.csee.isel.fcminer.fpcollector.subset.SubsetGenerator;
import edu.handong.csee.isel.fcminer.fpcollector.subset.Superset;
import edu.handong.csee.isel.fcminer.fpcollector.tokendiff.DataCollector;
import edu.handong.csee.isel.fcminer.fpcollector.tokendiff.datapreproc.CompareDatas;
import edu.handong.csee.isel.fcminer.util.CliCommand;
import edu.handong.csee.isel.fcminer.util.CliOptions.RunState;

public class FPCollector {	
	public void run(CliCommand command, int numOfAlarms) {
		if(command.getState().equals(RunState.SAResultMiner)) return;
		
		DataCollector dataCollector = new DataCollector();				
		//Run Diff Algorithm
		ArrayList<CompareDatas> compareDatas = dataCollector.run(command.getResultPath(), numOfAlarms);				
		
		SubsetGenerator subsetGen = new SubsetGenerator();
		
		ArrayList<Superset> supersets = subsetGen.subsetGenerate(compareDatas);
		
		ConcreteCodePatternFinder codePatternFinder = new ConcreteCodePatternFinder();		
//		codePatternFinder.find(supersets);
	}
}
