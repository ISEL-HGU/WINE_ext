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
//		if(cDatas.size() == 0)
//			root = cData;
//
		nodeList.add(n);
	}
	
	public ArrayList<Node> getNodeList(){
		return nodeList;
	}
	
	public int containRoot(Node node) {
		int rootIdx = 0;
		for(Node cData : nodeList) {
			if(cData.getType() == node.getType()) {
				return rootIdx;				
			}
			rootIdx++;
		}
		rootIdx = -1;
		return rootIdx;
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
	
	private int containProperty(Node cData, Property p, int propIdx) {
		ArrayList<Property> props = cData.getParentProperty();
		
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
