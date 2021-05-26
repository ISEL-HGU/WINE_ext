package edu.handong.csee.isel.fcminer.fpcollector.tokendiff.datapreproc;

import java.util.ArrayList;

import edu.handong.csee.isel.fcminer.fpcollector.tokendiff.ast.gen.Property;

public class NodeList {
	ArrayList<Node> nodeList = new ArrayList<>();
//	CompareData root;
	private String vLineCode = "";
	private String vNodeCode = "";
	
	public String getvLineCode() {
		return vLineCode;
	}

	public void setvLineCode(String vLineCode) {
		this.vLineCode = vLineCode;
	}

	public String getvNodeCode() {
		return vNodeCode;
	}

	public void setvNodeCode(String vNodeCode) {
		this.vNodeCode = vNodeCode;
	}

	public void addNode(Node n) {
		nodeList.add(n);
	}
	
	public ArrayList<Node> getNodeList(){
		return nodeList;
	}
	
	public int contain(Node node2, int curIdx) {
		int propSize = node2.getParentProperty().size();
		
		for(int i = curIdx; i < nodeList.size(); i ++) {
			Node node1 = nodeList.get(i);
			
			if(node1.getType() == node2.getType()) {
				int propCnt = 0;
				int propIdx = -1;
				for(Property p : node2.getParentProperty()) {
					propIdx = containProperty(node1, p, propIdx);
					if(propIdx != -1)
						propCnt++;					
				}
				if(propCnt == propSize) {
					return i;
				} 				
			}
		}
		
		return -1;
	}
	
	private int containProperty(Node node, Property p, int propIdx) {
		ArrayList<Property> props = node.getParentProperty();
		
		for(int i = propIdx + 1; i < props.size(); i ++) {
			if(props.get(i).getNodeType() == p.getNodeType()) {
				if(props.get(i).getProp().equals(p.getProp())) {
					return i;
				}
			}
		}
		return -1;
	}
}
