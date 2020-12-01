package edu.handong.csee.isel.fcminer.fpcollector.tokendiff.compare;

import java.util.ArrayList;

import edu.handong.csee.isel.fcminer.fpcollector.pattern.Pattern;
import edu.handong.csee.isel.fcminer.fpcollector.tokendiff.ast.ITree;
import edu.handong.csee.isel.fcminer.fpcollector.tokendiff.ast.gen.Property;
import edu.handong.csee.isel.fcminer.fpcollector.tokendiff.datapreproc.Info;

public class Matcher {
	Info fixed;
	Info var;
	
	MappingStorage storage = new MappingStorage();
	String tempHashString = "";
	
	int forIdx = -1;
	int backIdx = -1;
	int vIdx = -1;
	
	public enum Part{
		F, V, B
	}		
	
	public Matcher(Info fixed, Info var) {
		this.fixed = fixed;
		this.var = var;
	}
	
	public MappingStorage match() {
		findMatchIn(fixed.getForwardPart(), var.getForwardPart(), Part.F);
		findMatchIn(fixed.getVPart(), var.getVPart(), Part.V);
		findMatchIn(fixed.getBackwardPart(), var.getBackwardPart(), Part.B);
		return storage;
	}
	
	private void findMatchIn(ArrayList<ITree> fixed, ArrayList<ITree> var,Part part) {
		if(part == Part.F) {
			
		} 
		else if(part == Part.V) {
			// same type node collect
			boolean[] varCh = new boolean[var.size()];
			boolean[] fixedCh = new boolean[fixed.size()];
			ArrayList<Mapping> tempMapStorage = new ArrayList<>();
			
			for(int i = 0; i < fixed.size(); i ++) {
				ITree tempFNode = fixed.get(i);
				for(int j = 0 ; j < var.size(); j ++) {
					ITree tempVarNode = var.get(j);
					if(varCh[j] == true) continue;
					if(fixedCh[i] == true) break;
					
					if(tempFNode.getType() == tempVarNode.getType()) {
						int ppMatchingCnt = 0;
						int tempPPLen = 0;
						ArrayList<Property> tempFNodePP = tempFNode.getParentProps();
						ArrayList<Property> tempVarNodePP = tempVarNode.getParentProps();
						
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
									tempMapping.setMapping(new Pair<ITree, ITree>(tempFNode, tempVarNode));
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
						String tempMappingHashString = "";
						if(tempMapping.getMatchedParent() != -1) {
							tempMappingHashString += tempMapping.getMapping().getFirst().getType();
							tempMappingHashString += tempMapping.getMatchedParent();
							tempHashString += tempMapping.getMapping().getFirst().getType();
							tempHashString += tempMapping.getMatchedParent();
							for(int k = 0; k < tempMapping.getParentProperties().size(); k ++) {
								tempHashString += tempMapping.getParentProperties().get(k).getNodeType(); 
								tempHashString += tempMapping.getParentProperties().get(k).getProp();
								tempMappingHashString += tempMapping.getParentProperties().get(k).getNodeType(); 
								tempMappingHashString += tempMapping.getParentProperties().get(k).getProp();
							}							
							tempMapping.setHash(tempMappingHashString.hashCode());
							tempMapStorage.add(tempMapping);
						}
					}
				}
			}			
			storage.add2MappingStorageV(tempMapStorage);
			//for print
//			Pattern p = new Pattern();
//			for(int i = 0; i <tempMapStorage.size(); i ++) {
//				System.out.println(p.type2String(tempMapStorage.get(i).getMapping().getFirst().getType()) 
//						+"(" +tempMapStorage.get(i).getMapping().getFirst().getNode2String() + ")"						
//						+ " / " +  p.type2String(tempMapStorage.get(i).getMapping().getFirst().getType()) + "("
//						+ tempMapStorage.get(i).getMapping().getSecond().getNode2String()+ ")");
//				System.out.println("Matched Parent: " + tempMapStorage.get(i).getMatchedParent());
//				System.out.print("Parent Properties: " + "[");
//				for(int j = 0 ; j < tempMapStorage.get(i).getParentProperties().size(); j ++) {
//					System.out.print(tempMapStorage.get(i).getParentProperties().get(j).getNodeType() 
//							+ "(" + tempMapStorage.get(i).getParentProperties().get(j).getTypeName()
//							+ ")-" + tempMapStorage.get(i).getParentProperties().get(j).getProp() + "$");
//				}
//				System.out.print("]\n\n");
//			}
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
		return tempHashString;
	}
	
	public MappingStorage getMappingStorage() {
		return storage;
	}
}
