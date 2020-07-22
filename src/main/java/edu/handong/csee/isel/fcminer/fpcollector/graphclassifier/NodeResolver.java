package edu.handong.csee.isel.fcminer.fpcollector.graphclassifier;

import java.util.ArrayList;
import java.util.HashMap;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.CatchClause;
import org.eclipse.jdt.core.dom.ConditionalExpression;
import org.eclipse.jdt.core.dom.DoStatement;
import org.eclipse.jdt.core.dom.EnhancedForStatement;
import org.eclipse.jdt.core.dom.ForStatement;
import org.eclipse.jdt.core.dom.IfStatement;
import org.eclipse.jdt.core.dom.ReturnStatement;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.StringLiteral;
import org.eclipse.jdt.core.dom.SwitchStatement;
import org.eclipse.jdt.core.dom.ThisExpression;
import org.eclipse.jdt.core.dom.ThrowStatement;
import org.eclipse.jdt.core.dom.TryStatement;
import org.eclipse.jdt.core.dom.WhileStatement;

import edu.handong.csee.isel.fcminer.fpcollector.graphanalyzer.GraphInfo;
import edu.handong.csee.isel.fcminer.fpcollector.graphbuilder.ControlNode;
import edu.handong.csee.isel.fcminer.fpcollector.graphbuilder.ControlState;
import edu.handong.csee.isel.fcminer.fpcollector.graphbuilder.DataNode;
import edu.handong.csee.isel.fcminer.fpcollector.graphbuilder.GraphNode;
import edu.handong.csee.isel.fcminer.fpcollector.graphbuilder.VarState;


/*
 * Graph2String Rule
 * <String> := <Level> <ControlState><ASTNodeType> | <Level><DataState><ASTNodeType>
 * <Level> := {x | 0 < x < 100 && x is Integer}
 * <ASTNodeType> := {1(SimpleName), 2(ThisExpression), 3(DoStatement), 4(IfStatement), 5(ConditionalExpression), 6(ForStatement), 7(WhileStatement),
 * 8(EnhancedForStatement), 9(TryStatement), 10(CatchClause), 11(SwitchStatement), 12(ReutrnStatement), 13(ThrowStatement), 14(StringLiteral)}
 * <ControlState> := <L> | <T> | <C>
 * <DataState> := (<Define> | <Reference>)<InCondition><InAnnotation><Type><getFrom>
 * 
 * <L> := 999999
 * <T> := 999998
 * <C> := 999997
 * 
 * <Define> := 0(DIN) | 1(DI) | 2(D) | 3(FDIN) | 4(FDI) | 5(FD)
 * <Reference> := 6(Ass) | 7(FAss) | 8(Ref) | 9(FRef) | 10(NAss) | 11(FNAss)
 * <InCondition> := 0(I) | 1(O)
 * <InAnnotation> := 0(IA) | 1(OA)
 * <Type> := 0(ArrIdxC) | 1(ArrIdxF) | 2(NArr)
 * <getFrom> := 0(null) | 1(not null)
 * 
 */

public class NodeResolver {	
	public void transformNode(HashMap<Integer, ArrayList<GraphInfo>> g) {
		for(Integer totalNodeNum : g.keySet()) {
			for(int i = 0 ; i < g.get(totalNodeNum).size() ; i ++) {
				String tempString = graphToString(g.get(totalNodeNum).get(i).root);
				g.get(totalNodeNum).get(i).graph2String = tempString;
			}
		}
	}
	
	private String graphToString(ControlNode g) {
		String tempS = "";
		
		for(GraphNode n : g.nexts) {
			int violatedNodeStartPosition;
			int violatedNodeEndPosition;
			int nodeStartPosition;
			int nodeEndPosition;
			if(n.varNodes.size() == 0) {
				for(ASTNode tempNode : n.fieldNodes) {
					violatedNodeStartPosition = tempNode.getStartPosition();
					violatedNodeEndPosition = violatedNodeStartPosition + tempNode.getLength();
					nodeStartPosition = n.node.getStartPosition();
					nodeEndPosition = nodeStartPosition + n.node.getLength();
					
					if(violatedNodeStartPosition <= nodeStartPosition && violatedNodeEndPosition >= nodeEndPosition) {
						tempS += addLevel(n);
						tempS += addState(n);			
						if(n instanceof DataNode) {
							tempS += addInCondition(n);
							tempS += addInAnnotation(n);
							tempS += addType(n);
							tempS += addFrom(n);				
						}
						tempS += addASTNodeType(n);
					}								
				}
				if(n instanceof ControlNode) {
					tempS += graphToString((ControlNode) n);
				}
			}
			else if(n.fieldNodes.size() == 0) {
				for(ASTNode tempNode : n.varNodes) {
					violatedNodeStartPosition = tempNode.getStartPosition();
					violatedNodeEndPosition = violatedNodeStartPosition + tempNode.getLength();
					nodeStartPosition = n.node.getStartPosition();
					nodeEndPosition = nodeStartPosition + n.node.getLength();
					if(violatedNodeStartPosition <= nodeStartPosition && violatedNodeEndPosition >= nodeEndPosition) {
						tempS += addLevel(n);
						tempS += addState(n);			
						if(n instanceof DataNode) {
							tempS += addInCondition(n);
							tempS += addInAnnotation(n);
							tempS += addType(n);
							tempS += addFrom(n);				
						}
						tempS += addASTNodeType(n);
					}							
				}
				if(n instanceof ControlNode) {
					tempS += graphToString((ControlNode) n);
				}
			}
			else if(n.fieldNodes.size() != 0 && n.varNodes.size() != 0) {
				for(ASTNode tempNode : n.varNodes) {
					violatedNodeStartPosition = tempNode.getStartPosition();
					violatedNodeEndPosition = violatedNodeStartPosition + tempNode.getLength();
					nodeStartPosition = n.node.getStartPosition();
					nodeEndPosition = nodeStartPosition + n.node.getLength();
					if(violatedNodeStartPosition <= nodeStartPosition && violatedNodeEndPosition >= nodeEndPosition) {
						tempS += addLevel(n);
						tempS += addState(n);			
						if(n instanceof DataNode) {
							tempS += addInCondition(n);
							tempS += addInAnnotation(n);
							tempS += addType(n);
							tempS += addFrom(n);				
						}
						tempS += addASTNodeType(n);
					}							
				}
				for(ASTNode tempNode : n.fieldNodes) {
					violatedNodeStartPosition = tempNode.getStartPosition();
					violatedNodeEndPosition = violatedNodeStartPosition + tempNode.getLength();
					nodeStartPosition = n.node.getStartPosition();
					nodeEndPosition = nodeStartPosition + n.node.getLength();
					
					if(violatedNodeStartPosition <= nodeStartPosition && violatedNodeEndPosition >= nodeEndPosition) {
						tempS += addLevel(n);
						tempS += addState(n);			
						if(n instanceof DataNode) {
							tempS += addInCondition(n);
							tempS += addInAnnotation(n);
							tempS += addType(n);
							tempS += addFrom(n);				
						}
						tempS += addASTNodeType(n);
					}								
				}
				if(n instanceof ControlNode) {
					tempS += graphToString((ControlNode) n);
				}
			}
		}		
		if(tempS.length() > 10) {
//			System.out.println("A");
		}
		return tempS;		
	}
	
	private String addLevel(GraphNode n) {
		String level = "" + n.level;
		
		if(n.level < 10) {
			level = "0" + n.level;
			return level;
		} 
		
		else return level;
	}

	private String addState(GraphNode n) {		
		if(n instanceof ControlNode) {
			if(((ControlNode) n).property == ControlState.L)
				return "999999";
			else if(((ControlNode) n).property == ControlState.T)
				return "999998";
			else if(((ControlNode) n).property == ControlState.C)
				return "999997";
		} else if (n instanceof DataNode) {
			if(((DataNode) n).state == VarState.DIN)
				return "00";
			else if(((DataNode) n).state == VarState.DI)
				return "01";
			else if(((DataNode) n).state == VarState.D)
				return "02";
			else if(((DataNode) n).state == VarState.FDIN)
				return "03";
			else if(((DataNode) n).state == VarState.FDI)
				return "04";
			else if(((DataNode) n).state == VarState.FD)
				return "05";
			else if(((DataNode) n).state == VarState.Ass)
				return "06";
			else if(((DataNode) n).state == VarState.FAss)
				return "07";
			else if(((DataNode) n).state == VarState.Ref)
				return "08";
			else if(((DataNode) n).state == VarState.FRef)
				return "09";
			else if(((DataNode) n).state == VarState.NAss)
				return "10";
			else if(((DataNode) n).state == VarState.FNAss)
				return "11";
		}
		return null;
	}
	
	private String addInCondition(GraphNode n) {
		if(n instanceof DataNode) {
			if(((DataNode) n).inCondition == VarState.I) {
				return "0";
			}
			else if(((DataNode) n).inCondition == VarState.O) {
				return "1";
			}
		}
		return null;
	}
	
	private String addInAnnotation(GraphNode n) {
		if(n instanceof DataNode) {
			if(((DataNode) n).inAnnotation == VarState.IA) {
				return "0";
			}
			else if(((DataNode) n).inAnnotation == VarState.NA) {
				return "1";
			}
		}
		return null;
	}
	
	private String addType(GraphNode n) {
		if(n instanceof DataNode) {
			if(((DataNode) n).type == VarState.ArrIdxC) {
				return "0";
			}
			else if(((DataNode) n).type == VarState.ArrIdxF) {
				return "1";
			}
			else if(((DataNode) n).type == VarState.NArr) {
				return "2";
			}
		}
		return null;
	}

	private String addFrom(GraphNode n) {
		if(n instanceof DataNode) {
			if(((DataNode) n).from == null) {
				return "0";
			}
			else return "1";
		}
		return null;
	}
	
	private String addASTNodeType(GraphNode n) {
		if(n instanceof DataNode) {
			if(n.node instanceof SimpleName) {
				return "01";
			}
			else if(n.node instanceof ThisExpression) {
				return "02";
			}
			else if(n.node instanceof StringLiteral) {
				return "14";
			}
		} else if(n instanceof ControlNode) {
			if(n.node instanceof DoStatement) {
				return "03";
			}
			if(n.node instanceof IfStatement) {
				return "04";
			}
			if(n.node instanceof ConditionalExpression) {
				return "05";
			}
			if(n.node instanceof ForStatement) {
				return "06";
			}
			if(n.node instanceof WhileStatement) {
				return "07";
			}
			if(n.node instanceof EnhancedForStatement) {
				return "08";
			}
			if(n.node instanceof TryStatement) {
				return "09";
			}
			if(n.node instanceof CatchClause) {
				return "10";
			}
			if(n.node instanceof SwitchStatement) {
				return "11";
			}
			if(n.node instanceof ReturnStatement) {
				return "12";
			}
			if(n.node instanceof ThrowStatement) {
				return "13";
			}
		}
		return null;
	}

}
