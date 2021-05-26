package edu.handong.csee.isel.fcminer.fpcollector.tokendiff.datapreproc;

import java.util.ArrayList;

import edu.handong.csee.isel.fcminer.fpcollector.tokendiff.ast.ITree;
import edu.handong.csee.isel.fcminer.fpcollector.tokendiff.ast.gen.Property;

public class Node {
	//compare data
	private ArrayList<Property> parentProperty = new ArrayList<>();
	private int type = -1;
	private int pos = -1;
	private int depth = -1;
	private String vNodeStr = "";
	private String label = "";
	private boolean leaf = false;
	
	public Node() {
		
	}
	
	public Node(ArrayList<Property> parentProperty, int type, int pos, int depth, String vNodeStr, boolean leaf, String label) {
		this.parentProperty.addAll(parentProperty);
		this.type = type;
		this.pos = pos;
		this.depth = depth;		
		this.vNodeStr = vNodeStr;
		this.leaf = leaf;		
		this.label = label;
	}
	
	public Node(ArrayList<Property> parentProperty, int type, int pos, int depth, String vNodeStr, boolean leaf) {
		this.parentProperty.addAll(parentProperty);
		this.type = type;
		this.pos = pos;
		this.depth = depth;		
		this.vNodeStr = vNodeStr;
		this.leaf = leaf;		
	}
	
	public String getLabel() {
		return label;
	}
	
	public boolean isLeaf() {
		return leaf;	
	}
	
	public String getVNodeStr() {
		return vNodeStr;
	}
	
	public int getDepth() {
		return depth;
	}
	
	public int getPos() {
		return pos;
	}
	
	public ArrayList<Property> getParentProperty(){
		return parentProperty;
	}
	
	public int getType() {
		return type;
	}
}
