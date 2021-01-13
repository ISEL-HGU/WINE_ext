package edu.handong.csee.isel.fcminer.fpcollector.tokendiff.compare;

import java.util.ArrayList;

import edu.handong.csee.isel.fcminer.fpcollector.tokendiff.ast.ITree;
import edu.handong.csee.isel.fcminer.fpcollector.tokendiff.ast.gen.Property;
import edu.handong.csee.isel.fcminer.fpcollector.tokendiff.datapreproc.CompareData;
import edu.handong.csee.isel.fcminer.fpcollector.tokendiff.datapreproc.CompareDatas;

public class Matcher {
	CompareDatas fixed;
	CompareDatas var;
	
	MappingStorage storage = new MappingStorage();
	StringBuilder tempHashString = new StringBuilder();
	
	int forIdx = -1;
	int backIdx = -1;
	int vIdx = -1;
	
	public enum Part{
		F, V, B
	}		
	
	public Matcher(CompareDatas fixed, CompareDatas var) {
		this.fixed = fixed;
		this.var = var;
	}
	
	public MappingStorage match() {
//		findMatchIn(fixed.getForwardPart(), var.getForwardPart(), Part.F);
		findMatchIn(fixed.getCompareDatas(), var.getCompareDatas(), Part.V);
//		findMatchIn(fixed.getBackwardPart(), var.getBackwardPart(), Part.B);
		return storage;
	}
	
	private void findMatchIn(ArrayList<CompareData> fixedLineTree, ArrayList<CompareData> varLineTree,Part part) {
		if(part == Part.F) {
			
		} 
		else if(part == Part.V) {
			// same type node collect
			boolean[] varCh = new boolean[varLineTree.size()];
			boolean[] fixedCh = new boolean[fixedLineTree.size()];
			ArrayList<Mapping> tempMapStorage = new ArrayList<>();
			
			for(int i = 0; i < fixedLineTree.size(); i ++) {
				CompareData tempFNode = fixedLineTree.get(i);
				for(int j = 0 ; j < varLineTree.size(); j ++) {
					CompareData tempVarNode = varLineTree.get(j);
					if(varCh[j] == true) continue;
					if(fixedCh[i] == true) break;
					
					if(tempFNode.getType() == tempVarNode.getType()) {
						int ppMatchingCnt = 0;
						int tempPPLen = 0;
						ArrayList<Property> tempFNodePP = tempFNode.getParentProperty();
						ArrayList<Property> tempVarNodePP = tempVarNode.getParentProperty();
						
						if(tempFNodePP.size() > tempVarNodePP.size()) {
							tempPPLen = tempVarNodePP.size();
						} else {
							tempPPLen = tempFNodePP.size();
						}
						
						Mapping tempMapping = new Mapping();
						for(int k = 0; k < tempPPLen; k ++) {
							if(tempFNodePP.get(k).getNodeType() == tempVarNodePP.get(k).getNodeType()
									&& tempFNodePP.get(k).getProp().equals(tempVarNodePP.get(k).getProp())) {
								if(ppMatchingCnt == 0) {
									tempMapping.setMapping(new Pair<CompareData, CompareData>(tempFNode, tempVarNode));
									tempMapping.setPart(Part.V);
								}
								ppMatchingCnt ++;								
								tempMapping.setMatchedParent(ppMatchingCnt);
								tempMapping.addParentProperties(
										new Property(tempFNodePP.get(k).getNodeType(),
													 tempFNodePP.get(k).getTypeName(),
													 tempFNodePP.get(k).getProp()));
								varCh[j] = true;
								fixedCh[i] = true;
							} else break;
						}
						StringBuilder tempMappingHashString = new StringBuilder();
						if(tempMapping.getMatchedParent() != -1) {
							tempMappingHashString.append(tempMapping.getMapping().getFirst().getType()); 
							tempMappingHashString.append(tempMapping.getMatchedParent());
							tempHashString.append(tempMapping.getMapping().getFirst().getType());
							tempHashString.append(tempMapping.getMatchedParent());
							for(int k = 0; k < tempMapping.getParentProperties().size(); k ++) {
								tempHashString.append(tempMapping.getParentProperties().get(k).getNodeType()); 
								tempHashString.append(tempMapping.getParentProperties().get(k).getProp());
								tempMappingHashString.append(tempMapping.getParentProperties().get(k).getNodeType()); 
								tempMappingHashString.append(tempMapping.getParentProperties().get(k).getProp());
							}									
														
							tempMapping.setHash(tempMappingHashString.toString().hashCode());							
							tempMapStorage.add(tempMapping);
						}
					}
				}
			}			
			storage.add2MappingStorageV(tempMapStorage);
		}
		else if(part == Part.B) {
			
		}
	}
	
	public void setForIdx(int forIdx) {
		this.forIdx = forIdx;
	}
	
	public void setBackIdx(int backIdx) {
		this.backIdx = backIdx;
	}
	
	public void setVIdx(int vIdx) {
		this.vIdx = vIdx;
	}
	
	public int getForIdx() {
		return forIdx;
	}
	
	public int getBackIdx() {
		return backIdx;
	}
	
	public int getVIdx() {
		return vIdx;
	}
	
	public String getTempHashString() {
		return tempHashString.toString();
	}
	
	public MappingStorage getMappingStorage() {
		return storage;
	}
}
