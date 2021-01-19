package edu.handong.csee.isel.fcminer.fpcollector.tokendiff.compare;

import java.util.ArrayList;
import java.util.Stack;

import edu.handong.csee.isel.fcminer.fpcollector.tokendiff.datapreproc.CompareData;
import edu.handong.csee.isel.fcminer.fpcollector.tokendiff.datapreproc.CompareDatas;

public class CodeComparator {
	private Stack<CompareDatas> gumTreeStack = new Stack<>();	
	
	public MappingStorage compare(CompareDatas cDatas) { 
		if(gumTreeStack.contains(cDatas)) return null;
		
		gumTreeStack.add(cDatas);
		
		if(gumTreeStack.size() == 1) return null;
		
		CompareDatas variableCompareData = gumTreeStack.pop();
		CompareDatas fixedCompareData = gumTreeStack.elementAt(0);
		
		Matcher matcher = new Matcher(fixedCompareData, variableCompareData);
		
		return matcher.matchInVLine();		
	}

	public void clear() {
		gumTreeStack.clear();
	}
}
