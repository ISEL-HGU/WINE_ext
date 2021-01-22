package edu.handong.csee.isel.fcminer.fpcollector.tokendiff.compare;

import java.util.ArrayList;

import edu.handong.csee.isel.fcminer.fpcollector.tokendiff.ast.gen.Property;
import edu.handong.csee.isel.fcminer.fpcollector.tokendiff.datapreproc.CompareData;
import edu.handong.csee.isel.fcminer.fpcollector.tokendiff.datapreproc.CompareDatas;

//one node matching <-> in MappingStore, there are nodes which are matched
public class Matcher {
	CompareDatas fixed;
	CompareDatas var;
	
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
	
	public MappingStorage matchInVLine() {
//		findMatchIn(fixed.getForwardPart(), var.getForwardPart(), Part.F);
		return findMatchIn(fixed.getCompareDatas(), var.getCompareDatas(), Part.V);
//		findMatchIn(fixed.getBackwardPart(), var.getBackwardPart(), Part.B);		
	}
	
	private MappingStorage findMatchIn(ArrayList<CompareData> fixedCompareDatas, ArrayList<CompareData> varCompareDatas,Part part) {
		MappingStorage storage = new MappingStorage();
		StringBuilder tempHashString = new StringBuilder();
		
		if(part == Part.F) {
			return null;
		} 
		else if(part == Part.V) {
			// same type node collect
			boolean[] varCh = new boolean[varCompareDatas.size()];
			boolean[] fixedCh = new boolean[fixedCompareDatas.size()];
			MappingStorage tempMapStorage = new MappingStorage();
			
			for(int i = 0; i < fixedCompareDatas.size(); i ++) {
				CompareData tempFixedCompareData = fixedCompareDatas.get(i);
				for(int j = 0 ; j < varCompareDatas.size(); j ++) {
					CompareData tempVarCompareData = varCompareDatas.get(j);
					if(varCh[j] == true) continue;
					if(fixedCh[i] == true) break;
					
					if(tempFixedCompareData.getType() == tempVarCompareData.getType()) {
						int ppMatchingCnt = 0;
						int tempPPLen = 0;
						ArrayList<Property> tempFNodePP = tempFixedCompareData.getParentProperty();
						ArrayList<Property> tempVarNodePP = tempVarCompareData.getParentProperty();
						
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
									tempMapping.setMapping(new Pair<CompareData, CompareData>(tempFixedCompareData, tempVarCompareData));
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
							tempMapStorage.add2MappingStorageV(tempMapping);
							tempMapStorage.setVLineCodes(tempFixedCompareData.getVLineCode(), tempVarCompareData.getVLineCode());
							tempMapStorage.setVNodeCodes(tempFixedCompareData.getVNodeCode(), tempVarCompareData.getVNodeCode());	
							tempMapStorage.setHash(tempHashString.toString().hashCode());
						}
					}
				}
			}
			if(tempMapStorage.getMappingStorageV().size() != 0)
				return tempMapStorage;
			else return null;
		}
		else if(part == Part.B) {
			return null;
		}
		return null;
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
}
