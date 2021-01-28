package edu.handong.csee.isel.fcminer.fpcollector.tokendiff;

import java.util.ArrayList;

import edu.handong.csee.isel.fcminer.fpcollector.tokendiff.datapreproc.CompareDatas;
import edu.handong.csee.isel.fcminer.fpcollector.tokendiff.datapreproc.RawDataCollector;

public class DataCollector {
		
	public ArrayList<CompareDatas> run(String resultPath, int numOfAlarms) {
		//collect violating file path, line number, violating code line		
		ArrayList<CompareDatas> cDatas = new ArrayList<>();
		
		cDatas.addAll(dataCollecting(resultPath, numOfAlarms));					
		
		return cDatas;				
	}
	
	private ArrayList<CompareDatas> dataCollecting(String resultPath, int numOfAlarms) {
		RawDataCollector collector = new RawDataCollector();			
		System.out.println("Info: Data Collecting is Started");
		collector.run(resultPath, numOfAlarms);				
		System.out.println("Info: Data Collecting is Finished, # of Alamrs: " + collector.getNumOfAlarms());
		return collector.getCompareDatas();
	}
}
