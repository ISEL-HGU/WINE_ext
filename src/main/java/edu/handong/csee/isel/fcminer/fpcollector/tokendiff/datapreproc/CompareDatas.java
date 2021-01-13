package edu.handong.csee.isel.fcminer.fpcollector.tokendiff.datapreproc;

import java.util.ArrayList;

public class CompareDatas {
	ArrayList<CompareData> cDatas = new ArrayList<>();
	
	public void addCompareData(CompareData cData) {
		cDatas.add(cData);
	}
	
	public ArrayList<CompareData> getCompareDatas(){
		return cDatas;
	}
}
