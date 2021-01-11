package edu.handong.csee.isel.fcminer.fpcollector.tokendiff.datapreproc;

import edu.handong.csee.isel.fcminer.fpcollector.tokendiff.ast.ITree;
import edu.handong.csee.isel.fcminer.fpcollector.tokendiff.ast.TreeContext;

public class ProcessedData {
	//preprocessed data
	private ITree vNode;
	private String vMethodString = "";
	private ITree vMethod;
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
