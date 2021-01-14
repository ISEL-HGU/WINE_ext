package edu.handong.csee.isel.fcminer.fpcollector.tokendiff.datapreproc;

import java.io.Serializable;
import java.util.ArrayList;

@SuppressWarnings("serial")
public class CompareDatas implements Serializable{	
	ArrayList<CompareData> cDatas = new ArrayList<>();
	
	public void addCompareData(CompareData cData) {
		cDatas.add(cData);
	}
	
	public ArrayList<CompareData> getCompareDatas(){
		return cDatas;
	}
}
