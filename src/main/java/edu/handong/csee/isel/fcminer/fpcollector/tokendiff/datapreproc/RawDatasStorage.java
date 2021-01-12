package edu.handong.csee.isel.fcminer.fpcollector.tokendiff.datapreproc;

import java.util.ArrayList;

public class RawDatasStorage {
	ArrayList<RawDatas> rawDatas = new ArrayList<>();
	
	public void saveRawDatas(ArrayList<RawData> rawDatas) {
		ArrayList<RawData> tempRawDatas = new ArrayList<>();		
		
		for(int i = 0 ; i < rawDatas.size(); i ++) {			
			tempRawDatas.add(rawDatas.get(i));
			if((i+1) % 100 == 0) {
				this.rawDatas.add(new RawDatas(tempRawDatas));
				tempRawDatas.clear();
				
			} else if(i == rawDatas.size() - 1) {
				this.rawDatas.add(new RawDatas(tempRawDatas));
				tempRawDatas.clear();
			}
		}		
	}
	
	public ArrayList<RawDatas> getRawDatas() {
		return rawDatas;
	}
}
