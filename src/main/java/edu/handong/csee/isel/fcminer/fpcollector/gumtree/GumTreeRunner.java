package edu.handong.csee.isel.fcminer.fpcollector.gumtree;

import java.util.Stack;

import org.eclipse.jdt.core.dom.ASTNode;

public class GumTreeRunner {
	private Stack<String> gumTreeStack = new Stack<>();
	private ASTNode pattern;
	public GumTreeRunner(String mockClass) {
		gumTreeStack.add(mockClass);
		runGumTree();
	}
	
	private void runGumTree() {
		if(gumTreeStack.size() == 1) {
			pattern = null;
		} else if(gumTreeStack.size() == 2) {
			String variableClass = gumTreeStack.pop();
			String fixedClass = gumTreeStack.elementAt(0);
			
			//GumTree pattern comparison
			
			
		}
	}
	
	public ASTNode getPattern() {
		return pattern;
	}
}
