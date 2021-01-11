package edu.handong.csee.isel.fcminer.fpcollector.tokendiff.datapreproc;

import java.util.ArrayList;

import edu.handong.csee.isel.fcminer.fpcollector.tokendiff.ast.ITree;
import edu.handong.csee.isel.fcminer.fpcollector.tokendiff.ast.TreeContext;

public class CompareData {		
	//compare data
	private ArrayList<ITree> forwardPart = new ArrayList<>();
	private ArrayList<ITree> vPart = new ArrayList<>();
	private ArrayList<ITree> backwardPart = new ArrayList<>();
	
	public void addForwardPart(ITree node) {
		this.forwardPart.add(node);
	}
	
	public void addVPart(ITree node) {
		this.vPart.add(node);
	}
	
	public void addBackwardPart(ITree node) {
		this.backwardPart.add(node);
	}
	
	public ArrayList<ITree> getForwardPart() {
		return forwardPart;
	}
	
	public ArrayList<ITree> getVPart() {
		return vPart;
	}
	
	public ArrayList<ITree> getBackwardPart() {
		return backwardPart;
	}
}
