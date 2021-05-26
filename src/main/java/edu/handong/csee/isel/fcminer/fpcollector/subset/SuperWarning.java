package edu.handong.csee.isel.fcminer.fpcollector.subset;

import java.util.ArrayList;

import edu.handong.csee.isel.fcminer.fpcollector.tokendiff.datapreproc.NodeList;

public class SuperWarning {
	private String code = "";
	private String contextCode = "";
	private NodeList lineNodes = new NodeList();
	private int numOfSubWarnings = 0;
	private int numOfEqualWarnings = 0;
//	private ArrayList<SubWarning> subsets = new ArrayList<>();
//	private ArrayList<SubWarning> equalsets = new ArrayList<>();
//	private int frequency = 0;
	
	public SuperWarning(String code, String contextCode, NodeList lineNodes) {
		this.code = code;
		this.contextCode = contextCode;
		this.lineNodes = lineNodes;
	}

	public void addSubWarning(){
		numOfSubWarnings++;
	}

	public void addSubWarnings(int num) {
		numOfSubWarnings += num;
	}
	public void addEqualWarning(){
		numOfEqualWarnings++;
	}

	public void addEqualWarnings(int num){
		numOfEqualWarnings += num;
	}

	public int getNumOfSubWarnings(){
		return numOfSubWarnings;
	}

	public int getNumOfEqualWarnings(){
		return numOfEqualWarnings;
	}

	public String getContextCode() {
		return contextCode;
	}

	public void setContextCode(String contextCode) {
		this.contextCode = contextCode;
	}

	public NodeList getLineNodes() {
		return lineNodes;
	}
	
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
//	public ArrayList<SubWarning> getSubsets() {
//		return subsets;
//	}
//	public void addSubset(SubWarning subset) {
//		subsets.add(subset);
//	}
//	public int getFrequency() {
//		return frequency;
//	}
//	public void addEqualset(SubWarning equalset) {
//		equalsets.add(equalset);
//		frequency++;
//	}
//	public ArrayList<SubWarning> getEqualsets() {
//		return equalsets;
//	}
	
}
