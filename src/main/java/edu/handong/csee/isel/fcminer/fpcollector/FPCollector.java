package edu.handong.csee.isel.fcminer.fpcollector;

import edu.handong.csee.isel.fcminer.fpcollector.tokendiff.datapreproc.RawData;
import edu.handong.csee.isel.fcminer.fpcollector.tokendiff.datapreproc.RawDataCollector;
import edu.handong.csee.isel.fcminer.util.CliCommand;
import edu.handong.csee.isel.fcminer.util.CliOptions.RunState;
import edu.handong.csee.isel.fcminer.util.OSValidator;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.io.*;
import java.util.ArrayList;

public class FPCollector {
	public void run(CliCommand command, int numOfAlarms) {
		if(command.getState().equals(RunState.SAResultMiner)) return;

		RawDataCollector collector = new RawDataCollector();
		collector.run(command.getResultPath(), numOfAlarms);
	}
}
