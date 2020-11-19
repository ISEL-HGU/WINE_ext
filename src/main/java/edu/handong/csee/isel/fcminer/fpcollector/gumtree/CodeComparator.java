package edu.handong.csee.isel.fcminer.fpcollector.gumtree;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Stack;

import org.eclipse.jdt.core.dom.ASTNode;

import edu.handong.csee.isel.fcminer.gumtree.client.Run;
import edu.handong.csee.isel.fcminer.gumtree.core.actions.ActionGenerator;
import edu.handong.csee.isel.fcminer.gumtree.core.actions.model.Action;
import edu.handong.csee.isel.fcminer.gumtree.core.matchers.MappingStore;
import edu.handong.csee.isel.fcminer.gumtree.core.matchers.Matcher;
import edu.handong.csee.isel.fcminer.gumtree.core.matchers.Matchers;
import edu.handong.csee.isel.fcminer.gumtree.core.tree.ITree;
import edu.handong.csee.isel.fcminer.gumtree.gen.jdt.JdtTreeGenerator;
import edu.handong.csee.isel.fcminer.saresultminer.git.Diff;

public class CodeComparator {
	private Stack<Info> gumTreeStack = new Stack<>();
	private Patterns patterns = new Patterns();
	
	public void compare(Info info) { 
		if(gumTreeStack.contains(info)) return;
		
		gumTreeStack.add(info);
		
		if(gumTreeStack.size() == 1) return;
		
		runGumTree();
	}
	
	private void runGumTree() {		
		//real

		Info variableClass = gumTreeStack.pop();
		Info fixedClass = gumTreeStack.elementAt(0);
		patterns.setFixed(fixedClass);
		patterns.addVariables(variableClass);
		System.out.println(fixedClass.getMockClass());
		System.out.println(variableClass.getMockClass());
		
		//GumTree pattern comparison
		//save patterns in File
		//ex) Pattern1.csv, Pattern2.csv ...
		
		//GumTree Init
		Run.initGenerators();
		
		ITree variable = variableClass.getCtx().getRoot();
		ITree fix = fixedClass.getCtx().getRoot();
		
		Matcher matchClass = Matchers.getInstance().getMatcher(fixedClass.getVNode(), variableClass.getVNode());
		matchClass.match();
		
		ActionGenerator actionGen = new ActionGenerator(fixedClass.getVNode(), variableClass.getVNode(), matchClass.getMappings());
		actionGen.generate();
		
		//pattern generation
		Pattern pattern = new Pattern();
		for(ITree node: actionGen.getCommonNodes()) {
			if(node.getType() == 55 || node.getParent().getType() == 55 || node.getType() == 8)
				continue;
			pattern.addNode2Pattern(node);
//			System.out.println(node.toShortString());
		}
		
		pattern.bfsPattern.sort(new Comparator<ITree>() {
			public int compare(ITree node1, ITree node2) {
				if(node1.getPos() > node2.getPos()) {
					return 1;
				} else if(node1.getPos() == node2.getPos()) {
					if(node1.getDepth() > node2.getDepth()) {
						return 1;
					} else return -1;
				} else return -1;
			}
		});
		
		System.out.println("@->\n");
		
		for(ITree pa : pattern.bfsPattern) {
			System.out.println( 
					"node type: " + pa.getType() +"(" + pattern.type2String(pa.getType())+ ")" + 
					" node label: " + pa.getLabel() + 
					" node depth: " + pa.getDepth() +
					" node position: " + pa.getPos() + "~" + pa.getEndPos());
		}
		
		System.out.println("<-@\n");
		
		patterns.addPattern(pattern);
		
//		for(ITree node: actionGen.getMovedNodes()) {
//			System.out.println(node.getType());
//			System.out.println(node.toShortString());
//		}
//		
//		for(ITree node: actionGen.getUpdatedNodes()) {
//			System.out.println(node.toShortString());
//		}
//		
//		for(ITree node: actionGen.getMaintainedNodes()) {
//			System.out.println(node.toShortString());
//		}
	}
	
	public Patterns getPatterns(){
		return patterns;
	}
	
	public void clear() {
		gumTreeStack.clear();
		patterns = new Patterns();
	}
}
