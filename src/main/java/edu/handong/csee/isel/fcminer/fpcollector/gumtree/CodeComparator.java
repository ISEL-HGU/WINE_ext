package edu.handong.csee.isel.fcminer.fpcollector.gumtree;

import java.util.List;
import java.util.Stack;

import org.eclipse.jdt.core.dom.ASTNode;

import com.github.gumtreediff.actions.ActionGenerator;
import com.github.gumtreediff.actions.model.Action;
import com.github.gumtreediff.client.Run;
import com.github.gumtreediff.gen.jdt.JdtTreeGenerator;
import com.github.gumtreediff.matchers.MappingStore;
import com.github.gumtreediff.matchers.Matcher;
import com.github.gumtreediff.matchers.Matchers;
import com.github.gumtreediff.tree.ITree;

public class CodeComparator {
	private Stack<String> gumTreeStack = new Stack<>();
	private ASTNode pattern;
	
	public void compare(String mockClass) {
		if(gumTreeStack.contains(mockClass)) return;
		gumTreeStack.add(mockClass);
		if(gumTreeStack.size() == 1) return;
		runGumTree();
	}
	
	private void runGumTree() {		
		String variableClass = gumTreeStack.pop();
		String fixedClass = gumTreeStack.elementAt(0);
		//need to data preprocess
//		String variableClass = "public class Main {"
//								+ "public void main(String args[]){ int a = 1; if(a == 1) a = 3; int c= 1; }"
//								+
//								" }";
//		String fixedClass = "public class Main {"
//							+ "public void main( String args[]){ int a = 1; a = 3;}"
//							+
//							" }";
		
		//GumTree pattern comparison
		//save patterns in File
		//ex) Pattern1.csv, Pattern2.csv ...
		
		Run.initGenerators();
		ITree variable = null;
		ITree fixed = null;
		try {
			variable = new JdtTreeGenerator().generateFromString(variableClass).getRoot();
			fixed = new JdtTreeGenerator().generateFromString(fixedClass).getRoot();
		} catch (Exception e) {
			e.printStackTrace();
		}
			
		Matcher matchClass = Matchers.getInstance().getMatcher(fixed, variable);
		matchClass.match();
		MappingStore mapStorage = matchClass.getMappings();
		ITree matchedTree = mapStorage.getSrc(variable);
		System.out.println(matchedTree.toTreeString());
	}
	
	public void clear() {
		gumTreeStack.clear();
	}
}
