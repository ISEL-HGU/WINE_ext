package edu.handong.csee.isel.fcminer.fpcollector.gumtree;

import java.util.ArrayList;

import edu.handong.csee.isel.fcminer.gumtree.core.tree.ITree;
import edu.handong.csee.isel.fcminer.gumtree.core.tree.TreeContext;

public class Info {
	public ArrayList<String> sourceByLine;
	public String path;
	public int start;
	public int end;
	public int startPosition;
	private String vLine = "";
	private ITree vNode;
	private ITree vMethod;
	private String vMethodString = "";
	private int changedLineNum;
	private String mockClass;
	private TreeContext ctx;
	
	public void setVMethod(ITree vMethod) {
		this.vMethod = vMethod;
	}
	
	public ITree getVMethod() {
		return vMethod;
	}
	
	public void setVNode(ITree vNode) {
		this.vNode = vNode;
	}
	
	public ITree getVNode() {
		return vNode;
	}
	
	public void addVLine(String vLine) {
		this.vLine = this.vLine + vLine.split("//")[0].trim();
	}
	
	public String getVLine() {
		return vLine;
	}
	
	public void setMockClass(String mockClass) {
		this.mockClass = mockClass;
	}
	
	public String getMockClass() {
		return mockClass;
	}
	
	public void setChangedLineNum(int lineNum) {
		this.changedLineNum = lineNum;
	}
	
	public int getChangedLineNum() {
		return changedLineNum;
	}

	public String getVMethodString() {
		return vMethodString;
	}

	public void setVMethodString(String vMethodString) {
		this.vMethodString = vMethodString;
	}

	public TreeContext getCtx() {
		return ctx;
	}

	public void setCtx(TreeContext ctx) {
		this.ctx = ctx;
	}
}
