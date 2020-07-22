package edu.handong.csee.isel.fcminer.fpcollector.graphbuilder;

import java.util.ArrayList;

import org.eclipse.jdt.core.dom.ASTNode;

public class GraphNode {
	public ASTNode node;
	public ControlNode parent;
	public int level;
	public String path;
	public ArrayList<ASTNode> varNodes = new ArrayList<>();
	public ArrayList<ASTNode> fieldNodes = new ArrayList<>();
	
	public GraphNode(ASTNode node, int level, Info info) {
		this.node = node;
		this.level = level;
		this.path = info.path;
		this.varNodes = info.varNodes;
		this.fieldNodes = info.fieldNodes;
	}
	public GraphNode(ASTNode node, int level) {
		this.node = node;
		this.level = level;		
	}
	public ASTNode getNode() {
		return node;
	}
}
