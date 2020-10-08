package edu.handong.csee.isel.fcminer.fpcollector.gumtree;

import java.util.ArrayList;

import edu.handong.csee.isel.fcminer.gumtree.core.tree.ITree;

public class Pattern {
	ArrayList<ITree> bfsPattern = new ArrayList<>();
	
	public void addNode2Pattern(ITree node) {
		bfsPattern.add(node);
	}
	
	public void printPattern() {
		for(ITree node: bfsPattern) {
			System.out.print(node.toShortString() + " ");
		}
	}
}
