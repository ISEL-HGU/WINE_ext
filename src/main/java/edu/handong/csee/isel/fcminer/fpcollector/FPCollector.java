package edu.handong.csee.isel.fcminer.fpcollector;

import java.util.ArrayList;

import edu.handong.csee.isel.fcminer.fpcollector.concretecodepattern.ConcreteCodePatternFinder;
import edu.handong.csee.isel.fcminer.fpcollector.subset.SubWarningGenerator;
import edu.handong.csee.isel.fcminer.fpcollector.subset.SuperWarning;
import edu.handong.csee.isel.fcminer.fpcollector.tokendiff.DataCollector;
import edu.handong.csee.isel.fcminer.fpcollector.tokendiff.datapreproc.NodeList;
import edu.handong.csee.isel.fcminer.util.CliCommand;
import edu.handong.csee.isel.fcminer.util.CliOptions.RunState;

public class FPCollector {	
	public void run(CliCommand command, int numOfAlarms) {
		if(command.getState().equals(RunState.SAResultMiner)) return;
		
		DataCollector dataCollector = new DataCollector();				
		dataCollector.run(command.getResultPath(), numOfAlarms);
	}
}
