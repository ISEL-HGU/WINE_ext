package edu.handong.csee.isel.fcminer.fpcollector.tokendiff.datapreproc;

import java.util.ArrayList;

public class RawDatasStorage {
	ArrayList<RawDatas> rawDatas = new ArrayList<>();
	
	public void saveRawDatas(ArrayList<RawData> rawDatas) {
		ArrayList<RawData> tempRawDatas = new ArrayList<>();
		int dataSize = rawDatas.size();
		
		for(int i = 0 ; i < rawDatas.size(); i ++) {
			if((i+1) % 100 == 0) {
				this.rawDatas.add(new RawDatas(tempRawDatas));
				tempRawDatas.clear();
				dataSize -= 100;
			} else if(dataSize < 100) {
				this.rawDatas.add(new RawDatas(tempRawDatas));
				tempRawDatas.clear();
			}
			tempRawDatas.add(rawDatas.get(i));
		}		
	}
	
	public ArrayList<RawDatas> getRawDatas() {
		return rawDatas;
	}
}
