package edu.handong.csee.isel.fcminer.fpcollector.tokendiff.compare;

import java.util.ArrayList;
import java.util.Stack;

import edu.handong.csee.isel.fcminer.fpcollector.tokendiff.datapreproc.CompareData;
import edu.handong.csee.isel.fcminer.fpcollector.tokendiff.datapreproc.CompareDatas;

public class CodeComparator {
	private Stack<CompareDatas> gumTreeStack = new Stack<>();
	private ArrayList<MappingStorage> storage = new ArrayList<>();
	
	public void compare(CompareDatas info) { 
		if(gumTreeStack.contains(info)) return;
		
		gumTreeStack.add(info);
		
		if(gumTreeStack.size() == 1) return;
		
		CompareDatas variableClass = gumTreeStack.pop();
		CompareDatas fixedClass = gumTreeStack.elementAt(0);
		
		Matcher matcher = new Matcher(fixedClass, variableClass);
		MappingStorage tempMappingSto = new MappingStorage();
		tempMappingSto = matcher.match();
		tempMappingSto.setHash(matcher.getTempHashString().hashCode());
		storage.add(tempMappingSto);		
	}
	
	public ArrayList<MappingStorage> getMappingStorage() {
		return storage;
	}
	
	public void clear() {
		gumTreeStack.clear();
	}
}
