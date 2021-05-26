package edu.handong.csee.isel.fcminer.fpcollector.subset;

import edu.handong.csee.isel.fcminer.fpcollector.tokendiff.datapreproc.NodeList;

public class SubWarning {
	private String code = "";
	private NodeList lineNodes= new NodeList();
	
	public SubWarning(String code, NodeList lineNodes) {
		this.code = code;
		this.lineNodes = lineNodes;
	}
	
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}

	public NodeList getLineNodes() {
		return lineNodes;
	}

	public void setLineNodes(NodeList lineNodes) {
		this.lineNodes = lineNodes;
	}
	
}
