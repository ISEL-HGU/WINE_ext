package edu.handong.csee.isel.fcminer.fpcollector.tokendiff.datapreproc;

import java.util.ArrayList;

import edu.handong.csee.isel.fcminer.fpcollector.tokendiff.ast.ITree;
import edu.handong.csee.isel.fcminer.fpcollector.tokendiff.ast.TreeContext;

public class Info {
	//Raw Data	
	private String path;
	private int start;
	private int end;
	private String vLine = "";
	private String src;	
	
	//preprocessed data
	private ITree vNode;
	private String vMethodString = "";
	private ITree vMethod;
	private TreeContext ctx;
	
	//compare data
	private ArrayList<ITree> forwardPart = new ArrayList<>();
	private ArrayList<ITree> vPart = new ArrayList<>();
	private ArrayList<ITree> backwardPart = new ArrayList<>();

	public void clearPreprocessedData() {
		vNode = null;
		vMethodString = null;
		vMethod = null;
		ctx = null;
	}
	
	public void clearRawData() {
		path = null;
		start = -1;
		end = -1;
		vLine =  null;
		src = null;
	}
	
	public int getStart() {
		return start;
	}

	public void setStart(String start) {		
		this.start = Integer.valueOf(start);
	}

	public int getEnd() {
		return end;
	}

	public void setEnd(String end) {
		this.end = Integer.valueOf(end);
	}
	
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

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getSrc() {
		return src;
	}

	public void setSrc(String src) {
		this.src = src;
	}
}
