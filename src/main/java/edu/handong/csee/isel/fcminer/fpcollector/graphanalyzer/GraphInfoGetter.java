package edu.handong.csee.isel.fcminer.fpcollector.graphanalyzer;

import java.util.HashMap;

import org.eclipse.jdt.core.dom.CatchClause;
import org.eclipse.jdt.core.dom.ConditionalExpression;
import org.eclipse.jdt.core.dom.DoStatement;
import org.eclipse.jdt.core.dom.EnhancedForStatement;
import org.eclipse.jdt.core.dom.ForStatement;
import org.eclipse.jdt.core.dom.IfStatement;
import org.eclipse.jdt.core.dom.ReturnStatement;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.SwitchStatement;
import org.eclipse.jdt.core.dom.ThisExpression;
import org.eclipse.jdt.core.dom.ThrowStatement;
import org.eclipse.jdt.core.dom.TryStatement;
import org.eclipse.jdt.core.dom.WhileStatement;

import edu.handong.csee.isel.fcminer.fpcollector.graphbuilder.ControlNode;
import edu.handong.csee.isel.fcminer.fpcollector.graphbuilder.DataNode;
import edu.handong.csee.isel.fcminer.fpcollector.graphbuilder.GraphNode;

public class GraphInfoGetter {
	static final int CONTROL = 0;
	
	static final int SIMPLE_NAME = 1;
	static final int THIS_EXPRESSION = 2;
	static final int DO_STATEMENT = 3;
	static final int IF_STATEMENT = 4;
	static final int CONDITIONAL_EXPRESSION = 5;
	static final int FOR_STATEMENT = 6;
	static final int WHILE_STATEMENT = 7;
	static final int ENHANCED_FOR_STATEMENT = 8;
	static final int TRY_STATEMENT = 9;
	static final int CATCH_CLAUSE = 10;
	static final int SWITCH_STATEMENT = 11;
	static final int RETURN_STATEMENT = 12;
	static final int THROW_STATEMENT = 13;
	
	public void getNodeNum(GraphInfo graph) {
		if(graph.root.nexts.size() == 0) {
			graph.dataNodeNum = 0;
			graph.controlNodeNum = 0;
		}
		else {
			graph.dataNodeNum = getDataNodeNum(graph.root);
			graph.controlNodeNum = getControlNodeNum(graph.root);
			graph.nodeNum = getASTNodeNum(graph.root, graph.nodeNum);
		}
	}
	
	private int getDataNodeNum(ControlNode root) {
		int num = 0;
		for(int i = 0 ; i < root.nexts.size(); i ++) {
			GraphNode tempChild = root.nexts.get(i);
			if(tempChild instanceof DataNode) {
				num++;
			} else if(tempChild instanceof ControlNode) {
				num += getDataNodeNum((ControlNode) tempChild);
			}
		}
		return num;
	}
	
	private int getControlNodeNum(ControlNode root) {
		int num = 0;
		for(int i = 0 ; i < root.nexts.size(); i ++) {
			GraphNode tempChild = root.nexts.get(i);
			if(tempChild instanceof ControlNode) {
				num++;
				num+=getControlNodeNum((ControlNode)tempChild);
			}
		}
		return num;
	}
	
	private HashMap<Integer, Integer> getASTNodeNum(ControlNode root, HashMap<Integer, Integer> num){		
		for(int i = 0 ; i < root.nexts.size(); i ++) {
			GraphNode tempChild = root.nexts.get(i);
			if(tempChild instanceof DataNode) {
				if(tempChild.node instanceof SimpleName) {
					if(!num.containsKey(SIMPLE_NAME))
						num.put(SIMPLE_NAME, 1);
					else {						
						num.replace(SIMPLE_NAME, num.get(SIMPLE_NAME) + 1);
					}				
				} else if (tempChild.node instanceof ThisExpression) {
					if(!num.containsKey(THIS_EXPRESSION)) {
						num.put(THIS_EXPRESSION, 1);
					}
					else {
						num.replace(THIS_EXPRESSION, num.get(THIS_EXPRESSION) + 1);
					}
				}
			}
			else if(tempChild instanceof ControlNode) {
				if(tempChild.node instanceof DoStatement) {
					if(!num.containsKey(DO_STATEMENT))
						num.put(DO_STATEMENT, 1);
					else {						
						num.replace(DO_STATEMENT, num.get(DO_STATEMENT) + 1);
					}				
				} else if (tempChild.node instanceof IfStatement) {
					if(!num.containsKey(IF_STATEMENT)) {
						num.put(IF_STATEMENT, 1);
					}
					else {
						num.replace(IF_STATEMENT, num.get(IF_STATEMENT) + 1);
					}
				} else if (tempChild.node instanceof ConditionalExpression) {
					if(!num.containsKey(CONDITIONAL_EXPRESSION)) {
						num.put(CONDITIONAL_EXPRESSION, 1);
					}
					else {
						num.replace(CONDITIONAL_EXPRESSION, num.get(CONDITIONAL_EXPRESSION) + 1);
					}
				} else if (tempChild.node instanceof ForStatement) {
					if(!num.containsKey(FOR_STATEMENT)) {
						num.put(FOR_STATEMENT, 1);
					}
					else {
						num.replace(FOR_STATEMENT, num.get(FOR_STATEMENT) + 1);
					}
				} else if (tempChild.node instanceof WhileStatement) {
					if(!num.containsKey(WHILE_STATEMENT)) {
						num.put(WHILE_STATEMENT, 1);
					}
					else {
						num.replace(WHILE_STATEMENT, num.get(WHILE_STATEMENT) + 1);
					}
				} else if (tempChild.node instanceof EnhancedForStatement) {
					if(!num.containsKey(ENHANCED_FOR_STATEMENT)) {
						num.put(ENHANCED_FOR_STATEMENT, 1);
					}
					else {
						num.replace(ENHANCED_FOR_STATEMENT, num.get(ENHANCED_FOR_STATEMENT) + 1);
					}
				} else if (tempChild.node instanceof TryStatement) {
					if(!num.containsKey(TRY_STATEMENT)) {
						num.put(TRY_STATEMENT, 1);
					}
					else {
						num.replace(TRY_STATEMENT, num.get(TRY_STATEMENT) + 1);
					}
				} else if (tempChild.node instanceof CatchClause) {
					if(!num.containsKey(CATCH_CLAUSE)) {
						num.put(CATCH_CLAUSE, 1);
					}
					else {
						num.replace(CATCH_CLAUSE, num.get(CATCH_CLAUSE) + 1);
					}
				} else if (tempChild.node instanceof SwitchStatement) {
					if(!num.containsKey(SWITCH_STATEMENT)) {
						num.put(SWITCH_STATEMENT, 1);
					}
					else {
						num.replace(SWITCH_STATEMENT, num.get(SWITCH_STATEMENT) + 1);
					}
				} else if (tempChild.node instanceof ReturnStatement) {
					if(!num.containsKey(RETURN_STATEMENT)) {
						num.put(RETURN_STATEMENT, 1);
					}
					else {
						num.replace(RETURN_STATEMENT, num.get(RETURN_STATEMENT) + 1);
					}
				} else if (tempChild.node instanceof ThrowStatement) {
					if(!num.containsKey(THROW_STATEMENT)) {
						num.put(THROW_STATEMENT, 1);
					}
					else {
						num.replace(THROW_STATEMENT, num.get(THROW_STATEMENT) + 1);
					}
				}
				getASTNodeNum((ControlNode) tempChild, num);
			}
		}
		return num;
	}
}
