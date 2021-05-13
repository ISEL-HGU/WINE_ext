package edu.handong.csee.isel.fcminer.fpcollector.tokendiff.datapreproc;

import edu.handong.csee.isel.fcminer.fpcollector.tokendiff.ast.ITree;
import edu.handong.csee.isel.fcminer.fpcollector.tokendiff.ast.TreeContext;

public class ProcessedData {	
	//preprocessed data
	private ITree vNode;
	private TreeContext ctx;
	private String code = "";
	private int start = -1;
	private int end = -1;
	private int vLineNum = -1;
	
	public int getStart() {
		return start;
	}
	
	public int getEnd() {
		return end;
	}

	public void setStartEnd(int start, int end, int vLineNum) {
		this.start = start;
		this.end = end;
		this.vLineNum = vLineNum;
	}
	public int getVLineNum(){
		return vLineNum;
	}
	public void setCode(String code) {
		this.code = code;
	}
	
	public String getCode() {
		return code;
	}
	
	public void setVNode(ITree vNode) {
		this.vNode = vNode;
	}
	
	public ITree getVNode() {
		return vNode;
	}

	public TreeContext getCtx() {
		return ctx;
	}

	public void setCtx(TreeContext ctx) {
		this.ctx = ctx;
	}
}
