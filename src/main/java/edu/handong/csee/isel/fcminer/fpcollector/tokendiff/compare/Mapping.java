package edu.handong.csee.isel.fcminer.fpcollector.tokendiff.compare;

import java.util.ArrayList;

import edu.handong.csee.isel.fcminer.fpcollector.tokendiff.ast.ITree;
import edu.handong.csee.isel.fcminer.fpcollector.tokendiff.ast.gen.Property;
import edu.handong.csee.isel.fcminer.fpcollector.tokendiff.compare.Matcher.Part;

public class Mapping {
	Pair<ITree, ITree> mapping;
	int matchedParent = -1;
	ArrayList<Property> parentProperties = new ArrayList<>();
	Part part = null;
	int hash = -1;
	String code = "";
	
	public Mapping() {
		
	}
	
	public Mapping(ITree t1, ITree t2) {
		this.mapping = new Pair<ITree, ITree>(t1, t2);
	}
	
	public void setCode(String code) {
		this.code = code;
	}
	
	public String getCode() {
		return code;
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

	public Pair<ITree, ITree> getMapping() {
		return mapping;
	}

	public void setMapping(Pair<ITree, ITree> mapping) {
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
