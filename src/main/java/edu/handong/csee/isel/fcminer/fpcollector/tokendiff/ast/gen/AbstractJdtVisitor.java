package edu.handong.csee.isel.fcminer.fpcollector.tokendiff.ast.gen;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

import org.eclipse.jdt.core.dom.*;

import edu.handong.csee.isel.fcminer.fpcollector.tokendiff.ast.ITree;
import edu.handong.csee.isel.fcminer.fpcollector.tokendiff.ast.TreeContext;
import edu.handong.csee.isel.fcminer.fpcollector.tokendiff.datapreproc.ProcessedData;
import edu.handong.csee.isel.fcminer.fpcollector.tokendiff.datapreproc.RawData;

public abstract class AbstractJdtVisitor extends ASTVisitor {
	final static int BLOCK = 8;
    protected TreeContext context = new TreeContext();

    private Deque<ITree> trees = new ArrayDeque<>();
    
    private RawData rawData;
    
    private ProcessedData pData = new ProcessedData();
    
    private CompilationUnit cUnit;

    public AbstractJdtVisitor(RawData rawData) {
    	super(true);
    	this.rawData = rawData;
    }

    public void setCUnit(CompilationUnit cUnit) {
    	this.cUnit = cUnit;
    }
    
    public ProcessedData getPreprocessedData() {
    	return pData;
    }
    
    public TreeContext getTreeContext() {
        return context;
    }

    @SuppressWarnings("rawtypes")
	protected void pushNode(ASTNode n, String label) {        	
        int type = n.getNodeType();        
        String typeName = n.getClass().getSimpleName();
        int startPos = n.getStartPosition();
        int len = n.getLength();
        String node2String = n.toString();
        boolean stmtFlag = false;

        ArrayList<Property> propertyPath = new ArrayList<>();

        if(rawData.getPath().equals("TargetProjects/netbeans/platform/o.n.bootstrap/src/org/netbeans/ModuleManager.java") && rawData.getStart() == 759){
//        	System.out.println("");
		}

        if(pData.getVNode() != null && rawData.getStart() <= getLineNum(n.getStartPosition()) && getLineNum(n.getStartPosition() + n.getLength()) <= rawData.getEnd()) {
	        propertyPath.addAll(parseProperty(n));
        }
		if(n instanceof Statement || n instanceof ImportDeclaration || n instanceof PackageDeclaration
				|| n instanceof FieldDeclaration )
			stmtFlag = true;

        push(n, type, typeName, label, startPos, len, node2String, propertyPath, stmtFlag);
    }

    private void push(ASTNode n, int type, String typeName, String label, int startPosition, int length, String node2String,
    					ArrayList<Property> propertyPath, boolean stmtFlag) {
    	ITree t = context.createTree(type, label, typeName);
        t.setPos(startPosition);
        t.setLength(length);
       	t.setStartLineNum(cUnit.getLineNumber(startPosition));
       	t.setEndLineNum(cUnit.getLineNumber(startPosition + length));
       	t.setNode2String(node2String);

        ArrayList<Property> newProps = t.getParentProps();
        newProps.addAll(propertyPath);
        t.setParentProps(newProps);
        
    	ITree parent = trees.peek();
        t.setParentAndUpdateChildren(parent);

		if(t.getStartLineNum() <= rawData.getVLineNum() && rawData.getVLineNum() <= t.getEndLineNum() && stmtFlag == true && t.getType() != BLOCK) {
			if (pData.getVNode() == null) {
				pData.setVNode(t);
				pData.getVNode().setStartLineNum(t.getStartLineNum());
				pData.getVNode().setEndLineNum(t.getEndLineNum());
				if (n instanceof IfStatement && t.getStartLineNum() == rawData.getVLineNum()) {
					ASTNode exp = ((IfStatement) n).getExpression();
					pData.getVNode().setStartLineNum(getLineNum(exp.getStartPosition()));
					pData.getVNode().setEndLineNum(getLineNum(exp.getStartPosition() + exp.getLength()));
				}
			}
			else {
				if ((pData.getVNode().getStartLineNum() != rawData.getStart() || pData.getVNode().getEndLineNum() != rawData.getEnd())) {
					pData.setVNode(t);
					if (n instanceof IfStatement && t.getStartLineNum() == rawData.getVLineNum()) {
						ASTNode exp = ((IfStatement) n).getExpression();
						pData.getVNode().setStartLineNum(getLineNum(exp.getStartPosition()));
						pData.getVNode().setEndLineNum(getLineNum(exp.getStartPosition() + exp.getLength()));
					}
					else {
						pData.getVNode().setStartLineNum(t.getStartLineNum());
						pData.getVNode().setEndLineNum(t.getEndLineNum());
					}
				}
			}
		}
        trees.push(t);
    }

    private ArrayList<Property> parseProperty(ASTNode n){
		ASTNode _n = n;
		ASTNode tempParent = _n.getParent();
		ArrayList<Property> propertyPath = new ArrayList<>();
		while(rawData.getStart() <= getLineNum(_n.getStartPosition()) && rawData.getStart() <= getLineNum(tempParent.getStartPosition())) {
			List list = tempParent.structuralPropertiesForType();
			Property parentProperty = new Property();
			int parentType = tempParent.getNodeType();
			for(int i = 0 ; i < list.size(); i ++) {
				StructuralPropertyDescriptor prop = (StructuralPropertyDescriptor) list.get(i);
				Object child = tempParent.getStructuralProperty(prop);
				ASTNode tempNode;
				if(child instanceof List) {
					for(int j = 0; j < ((List) child).size(); j ++) {
						tempNode =  (ASTNode) ((List) child).get(j);
						if(tempNode.getStartPosition() == _n.getStartPosition()
								&& tempNode.getLength() == _n.getLength()) {
							parentProperty.setNodeType(parentType);
							parentProperty.setTypeName(prop.getNodeClass().getSimpleName());
							parentProperty.setProp(prop.getId());
							propertyPath.add(parentProperty);
							break;
						}
					}
				} else if (child instanceof ASTNode) {
					tempNode = (ASTNode) child;
					if(tempNode.getStartPosition() == _n.getStartPosition()
							&& tempNode.getLength() == _n.getLength()) {
						parentProperty.setNodeType(parentType);
						parentProperty.setTypeName(prop.getNodeClass().getSimpleName());
						parentProperty.setProp(prop.getId());
						propertyPath.add(parentProperty);
						break;
					}
				}
			}
			_n = tempParent;
			tempParent = tempParent.getParent();
			if(tempParent == null) break;
					//|| tempParent.getNodeType() == BLOCK)

		}
		return propertyPath;
	}

    private boolean contain(String src, String test) {
		String newSrc = "";
		String newTest = "";
		for(int i = 0; i < src.length(); i ++) {
			if(Character.isSpaceChar(src.charAt(i)) || Character.isWhitespace(src.charAt(i))) {
				continue;
			}
			newSrc += src.charAt(i);
		}
		
		for(int i = 0; i < test.length(); i ++) {
			if(Character.isSpaceChar(test.charAt(i)) || Character.isWhitespace(test.charAt(i))) {
				continue;
			}
			newTest += test.charAt(i);
		}
		
		if(newSrc.length() >= newTest.length() && newTest.contains("},") && !newSrc.contains(newTest)) {
			newTest = newTest.replace("},", "}}");
			return newSrc.contains(newTest);
		}
		else return newSrc.contains(newTest);
		 
	}
    
    protected ITree getCurrentParent() {
        return trees.peek();
    }

    protected void popNode() {
        trees.pop();
    }
    
    public Deque<ITree> getTrees(){
    	return trees;
    }
    
	private int getLineNum(int startPosition){
		return cUnit.getLineNumber(startPosition);
	}
}
