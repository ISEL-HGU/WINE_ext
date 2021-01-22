package edu.handong.csee.isel.fcminer.fpcollector.tokendiff;

import java.util.ArrayList;

import edu.handong.csee.isel.fcminer.fpcollector.tokendiff.compare.CodeComparator;
import edu.handong.csee.isel.fcminer.fpcollector.tokendiff.compare.MappingStorage;
import edu.handong.csee.isel.fcminer.fpcollector.tokendiff.datapreproc.CompareDatas;
import edu.handong.csee.isel.fcminer.fpcollector.tokendiff.datapreproc.RawDataCollector;

public class TokenDiffMain {
		
	public ArrayList<MappingStorage> run(String resultPath, int numOfAlarms) {
		//collect violating file path, line number, violating code line		
		ArrayList<CompareDatas> cDatas = new ArrayList<>();
		
		cDatas.addAll(dataCollecting(resultPath, numOfAlarms));					
		
		return codeCompare(cDatas);				
	}
	
	private ArrayList<CompareDatas> dataCollecting(String resultPath, int numOfAlarms) {
		RawDataCollector collector = new RawDataCollector();			
		System.out.println("Info: Data Collecting is Started");
		collector.run(resultPath, numOfAlarms);				
		System.out.println("Info: Data Collecting is Finished, # of Alamrs: " + collector.getNumOfAlarms());
		return collector.getCompareDatas();
	}
	
	private ArrayList<MappingStorage> codeCompare(ArrayList<CompareDatas> cDatas) { 
		CodeComparator tokenDiff = new CodeComparator();
		ArrayList<MappingStorage> tempMappingStos = new ArrayList<>();
		for(int i = 0 ; i < cDatas.size(); i ++) {
			if(cDatas.get(i) == null) continue;
			printProgress(i, cDatas.size());
			
			MappingStorage tempMappingSto;
			
			tokenDiff.compare(cDatas.get(i));
			
			for(int j = 0 ; j < cDatas.size(); j++) {
				if(i == j) continue;					
				
				tempMappingSto = tokenDiff.compare(cDatas.get(j));
				
				if(tempMappingSto != null) {
					tempMappingStos.add(tempMappingSto);
					tempMappingSto = null;
				}
			}			
			
			tokenDiff.clear();
		}				
		return tempMappingStos;
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
