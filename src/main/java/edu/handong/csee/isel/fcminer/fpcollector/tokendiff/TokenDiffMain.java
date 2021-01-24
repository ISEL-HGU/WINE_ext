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
		ArrayList<MappingStorage> mappingStos = new ArrayList<>();
		int mismatchedPattern = 0;
		for(int i = 0 ; i < cDatas.size(); i ++) {
			if(cDatas.get(i) == null) continue;
			printProgress(i, cDatas.size());
			
			ArrayList<MappingStorage> tempMappingStos = new ArrayList<>();
			MappingStorage tempMappingSto;
			
			tokenDiff.compare(cDatas.get(i));
			
			for(int j = 0 ; j < cDatas.size(); j++) {
				if(i == j) continue;
				if(cDatas.get(j) == null) continue;
				
				tempMappingSto = tokenDiff.compare(cDatas.get(j));
				
				if(tempMappingSto != null) {
					tempMappingStos.add(tempMappingSto);					
				}
			}
			
			MappingStorage.MatchingStatus status = MappingStorage.MatchingStatus.Mismatched;
			
			for(MappingStorage ms : tempMappingStos) {
				if(ms.getMatchingStatus() != status) {
					status = MappingStorage.MatchingStatus.Matched;
					break;
				}
			}
			
			if(status == MappingStorage.MatchingStatus.Mismatched) {
				String tempHash = "" + i;
				tempMappingStos.get(0).setHash(tempHash.hashCode());
				mappingStos.add(tempMappingStos.get(0));
				mismatchedPattern++;
			} else {
				mappingStos.addAll(tempMappingStos);
			}
			
			tokenDiff.clear();
		}
		System.out.println("INFO: Number of Mismatched Pattern: " + mismatchedPattern);
		return mappingStos;
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
