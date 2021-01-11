package edu.handong.csee.isel.fcminer.fpcollector.tokendiff.compare;

import java.util.ArrayList;
import java.util.Stack;

import edu.handong.csee.isel.fcminer.fpcollector.tokendiff.datapreproc.CompareData;

public class CodeComparator {
	private Stack<CompareData> gumTreeStack = new Stack<>();
	private ArrayList<MappingStorage> storage = new ArrayList<>();
	
	public void compare(CompareData info) { 
		if(gumTreeStack.contains(info)) return;
		
		gumTreeStack.add(info);
		
		if(gumTreeStack.size() == 1) return;
		
		CompareData variableClass = gumTreeStack.pop();
		CompareData fixedClass = gumTreeStack.elementAt(0);
		
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
