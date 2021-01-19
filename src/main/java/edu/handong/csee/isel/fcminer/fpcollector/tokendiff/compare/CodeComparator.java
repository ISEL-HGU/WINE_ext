package edu.handong.csee.isel.fcminer.fpcollector.tokendiff.compare;

import java.util.Stack;

import edu.handong.csee.isel.fcminer.fpcollector.tokendiff.datapreproc.CompareDatas;

public class CodeComparator {
	private Stack<CompareDatas> comparingStack = new Stack<>();	
	
	public MappingStorage compare(CompareDatas cDatas) { 
		if(comparingStack.contains(cDatas)) return null;
		
		comparingStack.add(cDatas);
		
		if(comparingStack.size() == 1) return null;
		
		CompareDatas variableCompareData = comparingStack.pop();
		CompareDatas fixedCompareData = comparingStack.elementAt(0);
		
		Matcher matcher = new Matcher(fixedCompareData, variableCompareData);
		
		return matcher.matchInVLine();		
	}

	public void clear() {
		comparingStack.clear();
	}
}
