package edu.handong.csee.isel.fcminer.fpcollector.subset;

import edu.handong.csee.isel.fcminer.fpcollector.tokendiff.datapreproc.CompareDatas;

public class Subset {	
	private String code = "";
	private CompareDatas lineNodes= new CompareDatas();
	
	public Subset(String code, CompareDatas lineNodes) {
		this.code = code;
		this.lineNodes = lineNodes;
	}
	
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}

	public CompareDatas getLineNodes() {
		return lineNodes;
	}

	public void setLineNodes(CompareDatas lineNodes) {
		this.lineNodes = lineNodes;
	}
	
}
