package edu.handong.csee.isel.fcminer.fpcollector.tokendiff.datapreproc;

import java.util.ArrayList;

import edu.handong.csee.isel.fcminer.fpcollector.tokendiff.ast.gen.Property;

public class CompareDatas {
	ArrayList<CompareData> cDatas = new ArrayList<>();
	CompareData root;
	
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
				
				for(Property p : node.getParentProperty()) {
					if(containProperty(cData, p))
						propCnt++;					
				}
				if(propCnt == propSize) {
					curIdx = i;
					return curIdx;
				} 				
			}
		}
		
		return -1;
	}
	
	private boolean containProperty(CompareData cData, Property p) {		
		for(Property prop : cData.getParentProperty()) {
			if(prop.getNodeType() == p.getNodeType()) {
				if(prop.getProp().equals(p.getProp())) {
					return true;
				}
			}
		}
		
		return false;
	}
}
