package edu.handong.csee.isel.fcminer.fpcollector.tokendiff;

import java.util.ArrayList;

import edu.handong.csee.isel.fcminer.fpcollector.tokendiff.datapreproc.NodeList;
import edu.handong.csee.isel.fcminer.fpcollector.tokendiff.datapreproc.RawDataCollector;

public class DataCollector {
		
	public void run(String resultPath, int numOfAlarms) {
		RawDataCollector collector = new RawDataCollector();
		collector.run(resultPath, numOfAlarms);
	}
}
