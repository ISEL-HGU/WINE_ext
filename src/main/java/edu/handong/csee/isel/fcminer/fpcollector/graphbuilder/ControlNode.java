package edu.handong.csee.isel.fcminer.fpcollector.graphbuilder;

import java.util.ArrayList;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.SimpleName;

public class ControlNode extends GraphNode{	
	public ControlState state;
	public ControlState property;
	
	public ArrayList<GraphNode> nexts = new ArrayList<GraphNode>();
	
	public ControlNode(ASTNode node, ControlState state, int level) {
		super(node, level);
		
		this.state = state;
	}
	
	public ControlNode(ASTNode node, ControlState state, int level, Info info) {
		super(node, level, info);
		
		this.state = state;
	}
	
	public void setState(ControlState state) {
		this.state = state;
	}
	
	public void setProperty(ControlState property) {
		this.property = property;
	}
	
	public void printInfo() {
		System.out.println(this.node);
		printChildren(this);
		System.out.println("========================================================================================");
	}
	
	private void printChildren(ControlNode n) {
		for (int i = 0; i < n.nexts.size(); i++) {
			GraphNode n_ =  n.nexts.get(i);
			if (n_ instanceof DataNode) {
				for(int k = 0 ; k < n_.level; k ++) {
					System.out.printf("\t");
				}
				if(n_.node instanceof SimpleName)
					System.out.println("level: " + n_.level + "(D) n: " + n_.node.getClass().getSimpleName() + "( "+ n_.node +" )" +  ", state : " + ((DataNode)n_).state + " " + ((DataNode)n_).inCondition + " " + ((DataNode)n_).type + " " + ((DataNode)n_).from);
				else
					System.out.println("level: " + n_.level + "(D) n: " + n_.node.getClass().getSimpleName()  + ", state : " + ((DataNode)n_).state + " " + ((DataNode)n_).inCondition + " " + ((DataNode)n_).from);
			}
			else {
				for(int k = 0 ; k < n_.level; k ++) {
					System.out.printf("\t");
				}
				System.out.println("level: " + n_.level + "(C) n: " + n_.node.getClass().getSimpleName() + /*"( "+ ("" + n_.node).split("\n")[0] +" )" +*/ ", state : " + ((ControlNode)n_).state + " " + ((ControlNode)n_).property);
			}
			if (n_ instanceof ControlNode) {
				printChildren((ControlNode)n_);
			}
		}
	}
	
	public String writeInfo() {
		String graphRep = "";
		graphRep = writeChildren(this);
		return graphRep;
	}
	
	private String writeChildren(ControlNode n) {
		String graphRep = "";
		for (int i = 0; i < n.nexts.size(); i++) {
			GraphNode n_ =  n.nexts.get(i);
			if (n_ instanceof DataNode) {
				for(int k = 0 ; k < n_.level; k ++) {
					graphRep += "|___";
				}
				if(n_.node instanceof SimpleName)
					graphRep += "level: " + n_.level + " (D) n: " + n_.node.getClass().getSimpleName() + "( "+ n_.node +" )" +  ", state : " + ((DataNode)n_).state + " " + ((DataNode)n_).inCondition + " " + ((DataNode)n_).type + " " + ((DataNode)n_).from + "\n";
				else
					graphRep += "level: " + n_.level + " (D) n: " + n_.node.getClass().getSimpleName()  + ", state : " + ((DataNode)n_).state + " " + ((DataNode)n_).inCondition + " " + ((DataNode)n_).from+ "\n";
			}
			else {
				for(int k = 0 ; k < n_.level; k ++) {
					graphRep += "|___";
				}
				graphRep += "level: " + n_.level + " (C) n: " + n_.node.getClass().getSimpleName() + /*"( "+ ("" + n_.node).split("\n")[0] +" )" +*/ ", state : " + ((ControlNode)n_).state + " " + ((ControlNode)n_).property+ "\n";
			}
			if (n_ instanceof ControlNode) {
				graphRep += writeChildren((ControlNode)n_);
			}
		}
		return graphRep;
	}
	
}
