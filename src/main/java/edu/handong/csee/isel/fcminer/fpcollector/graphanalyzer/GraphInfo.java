package edu.handong.csee.isel.fcminer.fpcollector.graphanalyzer;

import java.util.HashMap;

import edu.handong.csee.isel.fcminer.fpcollector.graphbuilder.ControlNode;

public class GraphInfo {
	public ControlNode root;
	public int controlNodeNum = 0;
	public int dataNodeNum = 0;
	public String graph2String = "";
	public HashMap<Integer, Integer> nodeNum = new HashMap<>();
	
	public GraphInfo(ControlNode root) {
		this.root = root;
	}
	
	public String getNumberInfo() {
		String info = "TotalNodeNum: " + (this.dataNodeNum + this.controlNodeNum) + "\nDataNodeNum: " + this.dataNodeNum + " ControlNodeNum: " + this.controlNodeNum;
		if(this.nodeNum.get(1) != null) {
			info += "\nSimpleName: " + this.nodeNum.get(1);
		}
		if(this.nodeNum.get(2) != null) {
			info += "\nThisExpression: " + this.nodeNum.get(2);
		}
		if(this.nodeNum.get(3) != null) {
			info += "\nDoStatement: " + this.nodeNum.get(3);
		}
		if(this.nodeNum.get(4) != null) {
			info+= "\nIfStatement: " + this.nodeNum.get(4);
		}
		if(this.nodeNum.get(5) != null) {
			info += "\nConditionalExpression: " + this.nodeNum.get(5);
		}
		if(this.nodeNum.get(6) != null) {
			info += "\nForStatement: " + this.nodeNum.get(6);
		}
		if(this.nodeNum.get(7) != null) {
			info += "\nWhileStatement: " + this.nodeNum.get(7);
		}
		if(this.nodeNum.get(8) != null) {
			info += "\nEnhancedForStatement: " + this.nodeNum.get(8);
		}
		if(this.nodeNum.get(9) != null) {
			info += "\nTryStatement: " + this.nodeNum.get(9);
		}
		if(this.nodeNum.get(10) != null) {
			info += "\nCatchClause: " + this.nodeNum.get(10);
		}
		if(this.nodeNum.get(11) != null) {
			info += "\nSwitchStatement: " + this.nodeNum.get(11);
		}
		if(this.nodeNum.get(12) != null) {
			info += "\nReturnStatement: " + this.nodeNum.get(12);
		}
		if(this.nodeNum.get(13) != null) {
			info += "\nThrowStatement: " + this.nodeNum.get(13);
		}
		
		return info;
	}
}
