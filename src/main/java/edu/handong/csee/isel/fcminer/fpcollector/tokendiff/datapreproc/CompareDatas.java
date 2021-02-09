package edu.handong.csee.isel.fcminer.fpcollector.tokendiff.datapreproc;

import java.util.ArrayList;

import edu.handong.csee.isel.fcminer.fpcollector.tokendiff.ast.gen.Property;

public class CompareDatas {
	ArrayList<CompareData> cDatas = new ArrayList<>();
	CompareData root;
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

	public void addCompareData(CompareData cData) {		
		if(cDatas.size() == 0)
			root = cData;
		
		cDatas.add(cData);
	}
	
	public ArrayList<CompareData> getCompareDatas(){
		return cDatas;
	}
	
	public int containRoot(CompareData node) {
		int rootIdx = 0;
		for(CompareData cData : cDatas) {
			if(cData.getType() == node.getType()) {
				return rootIdx;				
			}
			rootIdx++;
		}
		rootIdx = -1;
		return rootIdx;
	}
	
	public int contain(CompareData node, int curIdx) {
		int propSize = node.getParentProperty().size();		
		
		for(int i = curIdx + 1; i < cDatas.size(); i ++) {
			CompareData cData = cDatas.get(i);
			
			if(cData.getType() == node.getType()) {
				int propCnt = 0;
				int propIdx = -1;
				for(Property p : node.getParentProperty()) {					
					propIdx = containProperty(cData, p, propIdx);		
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
	
	private int containProperty(CompareData cData, Property p, int propIdx) {		
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
