package edu.handong.csee.isel.fcminer.fpcollector.tokendiff;

import java.util.ArrayList;

import edu.handong.csee.isel.fcminer.fpcollector.tokendiff.datapreproc.NodeList;
import edu.handong.csee.isel.fcminer.fpcollector.tokendiff.datapreproc.RawDataCollector;

public class DataCollector {
		
	public ArrayList<NodeList> run(String resultPath, int numOfAlarms) {
		//collect violating file path, line number, violating code line		
		ArrayList<NodeList> nodeLists = new ArrayList<>();
		
		nodeLists.addAll(generateNodeList(resultPath, numOfAlarms));
		
		return nodeLists;
	}
	
	private ArrayList<NodeList> generateNodeList(String resultPath, int numOfAlarms) {
		RawDataCollector collector = new RawDataCollector();			
		System.out.println("Info: Data Collecting is Started");
		collector.run(resultPath, numOfAlarms);				
		System.out.println("Info: Data Collecting is Finished, # of Alamrs: " + collector.getNumOfAlarms());
		return collector.getNodeLists();
	}
}
