package edu.handong.csee.isel.fcminer.fpcollector.subset;

import java.util.ArrayList;

import edu.handong.csee.isel.fcminer.fpcollector.tokendiff.datapreproc.CompareDatas;

public class Superset {
	private String code = "";
	private String contextCode = "";
	private CompareDatas lineNodes = new CompareDatas();
	private ArrayList<Subset> subsets = new ArrayList<>();
	private ArrayList<Subset> equalsets = new ArrayList<>();
	private int frequency = 0;
	
	public Superset(String code, String contextCode, CompareDatas lineNodes) {
		this.code = code;
		this.contextCode = contextCode;
		this.lineNodes = lineNodes;
	}
	
	public String getContextCode() {
		return contextCode;
	}

	public void setContextCode(String contextCode) {
		this.contextCode = contextCode;
	}

	public CompareDatas getLineNodes() {
		return lineNodes;
	}
	
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public ArrayList<Subset> getSubsets() {
		return subsets;
	}
	public void addSubset(Subset subset) {
		subsets.add(subset);
	}
	public int getFrequency() {
		return frequency;
	}
	public void addEqualset(Subset equalset) {
		equalsets.add(equalset);
		frequency++;
	}
	
	public ArrayList<Subset> getEqualsets() {
		return equalsets;
	}
	
}
