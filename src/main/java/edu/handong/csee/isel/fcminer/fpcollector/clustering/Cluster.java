package edu.handong.csee.isel.fcminer.fpcollector.clustering;

import java.util.ArrayList;

import edu.handong.csee.isel.fcminer.fpcollector.tokendiff.compare.MappingStorage;

public class Cluster {		
	Integer cnt = -1;
	
	//elements are mapped nodes which shows the same pattern
	ArrayList<MappingStorage> elements = new ArrayList<>();
	
	public Cluster(MappingStorage ms) {
		cnt = 1;
		elements.add(ms);
	}
	
	public void addElement(MappingStorage ms) {
		cnt++;
		elements.add(ms);
	}
	
	public Integer getCnt() {
		return cnt;
	}
	
	public ArrayList<MappingStorage> getElements(){
		return elements;
	}
}
