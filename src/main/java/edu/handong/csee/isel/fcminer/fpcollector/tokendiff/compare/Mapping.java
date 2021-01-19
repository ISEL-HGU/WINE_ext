package edu.handong.csee.isel.fcminer.fpcollector.tokendiff.compare;

import java.util.ArrayList;

import edu.handong.csee.isel.fcminer.fpcollector.tokendiff.ast.ITree;
import edu.handong.csee.isel.fcminer.fpcollector.tokendiff.ast.gen.Property;
import edu.handong.csee.isel.fcminer.fpcollector.tokendiff.compare.Matcher.Part;
import edu.handong.csee.isel.fcminer.fpcollector.tokendiff.datapreproc.CompareData;

public class Mapping {
	Pair<CompareData, CompareData> mapping;
	int matchedParent = -1;
	ArrayList<Property> parentProperties = new ArrayList<>();
	Part part = null;
	int hash = -1;
	
	public Mapping() {
		
	}
	
	public Mapping(CompareData t1, CompareData t2) {
		this.mapping = new Pair<CompareData, CompareData>(t1, t2);
	}

	public void setHash(int hash) {
		this.hash = hash;
	}
	
	public int getHash() {
		return hash;
	}
	
	public void setPart(Part part) {
		this.part = part;
	}
	
	public Part getPart() {
		return part;
	}

	public Pair<CompareData, CompareData> getMapping() {
		return mapping;
	}

	public void setMapping(Pair<CompareData, CompareData> mapping) {
		this.mapping = mapping;
	}

	public int getMatchedParent() {
		return matchedParent;
	}

	public void setMatchedParent(int matchedParent) {
		this.matchedParent = matchedParent;
	}

	public ArrayList<Property> getParentProperties() {
		return parentProperties;
	}

	public void addParentProperties(Property parentProperty) {
		this.parentProperties.add(parentProperty);
	}		
	
	

}
