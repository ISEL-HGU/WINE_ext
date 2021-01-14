package edu.handong.csee.isel.fcminer.fpcollector.tokendiff;

import java.util.ArrayList;

import edu.handong.csee.isel.fcminer.fpcollector.tokendiff.compare.CodeComparator;
import edu.handong.csee.isel.fcminer.fpcollector.tokendiff.compare.MappingStorage;
import edu.handong.csee.isel.fcminer.fpcollector.tokendiff.datapreproc.CompareData;
import edu.handong.csee.isel.fcminer.fpcollector.tokendiff.datapreproc.CompareDatas;
import edu.handong.csee.isel.fcminer.fpcollector.tokendiff.datapreproc.RawDataCollector;

public class TokenDiffMain {
		
	public ArrayList<MappingStorage> run(String resultPath, int numOfAlarms) {
		//collect violating file path, line number, violating code line
		
		long start = System.currentTimeMillis();
		ArrayList<CompareDatas> cDatas = new ArrayList<>();
		
		dataCollecting(resultPath, numOfAlarms);				
		
		long end = System.currentTimeMillis();
		long time = (end - start) / 100;	
		
		return codeCompare(cDatas);				
	}
	
	private void dataCollecting(String resultPath, int numOfAlarms) {
		RawDataCollector collector = new RawDataCollector();			
		System.out.println("Info: Data Collecting is Started");
		collector.run(resultPath, numOfAlarms);				
		System.out.println("Info: Data Collecting is Finished, # of Alamrs: " + collector.getNumOfAlarms());		
	}
	
	private ArrayList<MappingStorage> codeCompare(ArrayList<CompareDatas> infos) { 
		CodeComparator tokenDiff = new CodeComparator();		
		for(int i = 0 ; i < infos.size(); i ++) {
			if(infos.get(i) == null) continue;
			printProgress(i, infos.size());
			
			tokenDiff.compare(infos.get(i));
			for(int j = i ; j < infos.size(); j++) {
				if(infos.get(j) == null) continue;
				tokenDiff.compare(infos.get(j));
			}

			tokenDiff.clear();
		}				
		return tokenDiff.getMappingStorage();
	}
	
	private void printProgress(int cnt, int total) {
		if(total / 10 == cnt) {
			System.out.print("10%...");
		}
		else if(total * 2 / 10 == cnt) {
			System.out.print("20%...");
		}
		else if(total * 3 / 10 == cnt) {
			System.out.print("30%...");
		}
		else if(total * 4 / 10 == cnt) {
			System.out.print("40%...");
		}
		else if(total * 5/ 10 == cnt) {
			System.out.print("50%...");
		}
		else if(total * 6 / 10 == cnt) {
			System.out.print("60%...");
		}
		else if(total * 7 / 10 == cnt) {
			System.out.print("70%...");
		}
		else if(total * 8 / 10 == cnt) {
			System.out.print("80%...");
		}
		else if(total * 9 / 10 == cnt) {
			System.out.print("90%...");
		}
		else if(total-1 == cnt) {
			System.out.print("done!\n");
		}		
	}
}
