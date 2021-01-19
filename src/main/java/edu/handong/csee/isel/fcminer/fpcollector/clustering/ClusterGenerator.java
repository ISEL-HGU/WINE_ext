package edu.handong.csee.isel.fcminer.fpcollector.clustering;

import java.util.ArrayList;
import java.util.HashMap;

import edu.handong.csee.isel.fcminer.fpcollector.tokendiff.compare.MappingStorage;

public class ClusterGenerator {					
	ArrayList<Integer> hashList = new ArrayList<>();
	
	public ArrayList<Integer> getHashList(){
		return hashList;
	}
	
	public HashMap<Integer, Cluster> clusterGenerate(ArrayList<MappingStorage> sto) {
		
		HashMap<Integer, Cluster> clusterHashMap = new HashMap<>();		
		
		for(int i = 0 ; i < sto.size(); i ++) {
			printProgress(i, sto.size());
			int tempHash = sto.get(i).getHash();
						
			if(!hashList.contains(tempHash)) {
				hashList.add(tempHash);
			}
			
			MappingStorage tempMappingSto = sto.get(i);
			//code String omitted in here
			if(!clusterHashMap.containsKey(tempHash)) {
				clusterHashMap.put(tempHash, new Cluster(tempMappingSto));
			} else {
				clusterHashMap.get(tempHash).addElement(tempMappingSto);
			}
		}
		
		return clusterHashMap;
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
