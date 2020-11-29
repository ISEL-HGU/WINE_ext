package edu.handong.csee.isel.fcminer.fpcollector.tokendiff.compare;

import java.util.Stack;

import edu.handong.csee.isel.fcminer.fpcollector.tokendiff.datapreproc.Info;
import edu.handong.csee.isel.fcminer.fpcollector.tokendiff.pattern.Pattern;
import edu.handong.csee.isel.fcminer.fpcollector.tokendiff.pattern.Patterns;

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

		Pattern pattern = new Pattern();
		System.out.println("Fixed:");
		for(int i = 0 ; i < fixedClass.getVPart().size(); i ++) {
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
			//parent props
			System.out.print("[" + pattern.type2String((variableClass.getVPart().get(i).getType())) + "-");			
			for(int j =0 ; j < variableClass.getVPart().get(i).getParentProps().size(); j ++) {
				System.out.print("("+variableClass.getVPart().get(i).getParentProps().get(j) +" ");
			}
			System.out.print(")]\n");
									
			
		}
		System.out.println("\n");
		
		matcher.match();
		
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
