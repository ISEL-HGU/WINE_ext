package edu.handong.csee.isel.fcminer.fpcollector.tokendiff.ast.gen;

import java.io.Serializable;

@SuppressWarnings("serial")
public class Property implements Serializable{
	int nodeType = -1;
	String typeName = "";
	String prop = "";
	
	public Property() {
			
	}
	
	public Property(int nodeType, String typeName, String prop) {
		this.nodeType = nodeType;
		this.typeName = typeName;
		this.prop = prop;
	}
	
	public int getNodeType() {
		return nodeType;
	}
	
	public void setNodeType(int nodeType) {
		this.nodeType = nodeType;
	}
	
	public String getTypeName() {
		return typeName;
	}
	
	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}
	
	public String getProp() {
		return prop;
	}
	
	public void setProp(String prop) {
		this.prop = prop;
	}
}
