package edu.handong.csee.isel.fcminer.fpcollector.tokendiff;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Stack;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ConditionalExpression;

import edu.handong.csee.isel.fcminer.gumtree.core.tree.ITree;
//import edu.handong.csee.isel.fcminer.gumtree.client.Run;
//import edu.handong.csee.isel.fcminer.gumtree.core.actions.ActionGenerator;
//import edu.handong.csee.isel.fcminer.gumtree.core.actions.model.Action;
//import edu.handong.csee.isel.fcminer.gumtree.core.matchers.MappingStore;
//import edu.handong.csee.isel.fcminer.gumtree.core.matchers.Matcher;
//import edu.handong.csee.isel.fcminer.gumtree.core.matchers.Matchers;
//import edu.handong.csee.isel.fcminer.gumtree.gen.jdt.JdtTreeGenerator;
//import edu.handong.csee.isel.fcminer.saresultminer.git.Diff;

public class CodeComparator {
	private Stack<Info> gumTreeStack = new Stack<>();
	private Patterns patterns = new Patterns();
	
	public void compare(Info info) { 
		if(gumTreeStack.contains(info)) return;
		
		gumTreeStack.add(info);
		
		if(gumTreeStack.size() == 1) return;
		
		runTokenDiff();
	}
	
	private void runTokenDiff() {		
		//real

		Info variableClass = gumTreeStack.pop();
		Info fixedClass = gumTreeStack.elementAt(0);
		patterns.setFixed(fixedClass);
		patterns.addVariables(variableClass);
		System.out.println(fixedClass.getMockClass());
		System.out.println(variableClass.getMockClass());
		
		Matcher matcher = new Matcher(fixedClass, variableClass);				
		
		//for child props: sort in depth
//		fixedClass.getVPart().sort(new Comparator<ITree>() {
//			public int compare(ITree node1, ITree node2) {
//				if(node1.getDepth() < node2.getDepth()) {
//					return -1;
//				} else if(node1.getDepth() == node2.getDepth()) {
//					if(node1.getPos() < node2.getPos()) {
//						return 1;
//					} else return 1;
//				} else return 1;
//			}
//		});
//		
//		variableClass.getVPart().sort(new Comparator<ITree>() {
//			public int compare(ITree node1, ITree node2) {
//				if(node1.getDepth() < node2.getDepth()) {
//					return -1;
//				} else if(node1.getDepth() == node2.getDepth()) {
//					if(node1.getPos() < node2.getPos()) {
//						return 1;
//					} else return 1;
//				} else return 1;
//			}
//		});
		

		Pattern pattern = new Pattern();
		System.out.println("Fixed:");
		for(int i = 0 ; i < fixedClass.getVPart().size(); i ++) {
			//child props
//			System.out.print("[" + pattern.type2String(fixedClass.getVPart().get(i).getType())+ "-" 
//					+ fixedClass.getVPart().get(i).getChildProps()  + "] ");
			
			//parent props
			System.out.print("[" + pattern.type2String((fixedClass.getVPart().get(i).getType())) + "-");			
			for(int j =0 ; j < fixedClass.getVPart().get(i).getParentProps().size(); j ++) {
				System.out.print("("+fixedClass.getVPart().get(i).getParentProps().get(j) +" ");
			}
			System.out.print(")]\n");
		}
		
		System.out.println("\n");
		
		System.out.println("Variable:");
		for(int i = 0 ; i < variableClass.getVPart().size(); i ++) {		
			//child props
//			System.out.print("[" + pattern.type2String(variableClass.getVPart().get(i).getType())+ "-" 
//								+ variableClass.getVPart().get(i).getChildProps()  + "] ");
			
			//parent props
			System.out.print("[" + pattern.type2String((variableClass.getVPart().get(i).getType())) + "-");			
			for(int j =0 ; j < variableClass.getVPart().get(i).getParentProps().size(); j ++) {
				System.out.print("("+variableClass.getVPart().get(i).getParentProps().get(j) +" ");
			}
			System.out.print(")]\n");
									
			
		}
		System.out.println("\n");
		
		matcher.match();
		
		/*GumTree pattern comparison
		//GumTree Init
//		Run.initGenerators();
		
//		ITree variable = variableClass.getCtx().getRoot();
//		ITree fix = fixedClass.getCtx().getRoot();
		
//		Matcher matchClass = Matchers.getInstance().getMatcher(fixedClass.getVNode(), variableClass.getVNode());
//		matchClass.match();
		
//		ActionGenerator actionGen = new ActionGenerator(fixedClass.getVNode(), variableClass.getVNode(), matchClass.getMappings());
//		actionGen.generate();
		
		//pattern generation
		
//		for(ITree node: actionGen.getCommonNodes()) {
//			if(node.getType() == 55 || node.getParent().getType() == 55 || node.getType() == 8)
//				continue;
//			pattern.addNode2Pattern(node);
//			System.out.println(node.toShortString());
//		}
 
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
 */ 
//		System.out.println("@->\n");
//		int cnt = 0;
//		for(ITree pa : pattern.bfsPattern) {
//			if(cnt <= forIdx) System.out.print("FORWARD PART: ");
//			else if(cnt <= vIdx) System.out.print("V PART: ");
//			else System.out.print("BACKWARD PART: ");
//			System.out.println( 
//					"node type: " + pa.getType() +"(" + pattern.type2String(pa.getType())+ ")" + 
//					" node label: " + pa.getLabel() + 
//					" node depth: " + pa.getDepth() +
//					" node position: " + pa.getPos() + "~" + pa.getEndPos());
//			cnt++;
//		}
		
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
