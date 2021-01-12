package edu.handong.csee.isel.fcminer.fpcollector.tokendiff.datapreproc;

import java.util.ArrayList;

public class RawDatas {
	ArrayList<RawData> rawDatas = new ArrayList<>();
	
	public RawDatas(ArrayList<RawData> rawDatas) {
		this.rawDatas = rawDatas;
	}
	
	public ArrayList<RawData> getRawDatas(){
		return rawDatas;
	}
	
}
