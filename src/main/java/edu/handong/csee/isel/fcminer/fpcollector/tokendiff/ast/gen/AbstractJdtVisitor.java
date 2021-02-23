package edu.handong.csee.isel.fcminer.fpcollector.tokendiff.ast.gen;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.StructuralPropertyDescriptor;

import edu.handong.csee.isel.fcminer.fpcollector.tokendiff.ast.ITree;
import edu.handong.csee.isel.fcminer.fpcollector.tokendiff.ast.TreeContext;
import edu.handong.csee.isel.fcminer.fpcollector.tokendiff.datapreproc.ProcessedData;
import edu.handong.csee.isel.fcminer.fpcollector.tokendiff.datapreproc.RawData;

public abstract class AbstractJdtVisitor extends ASTVisitor {

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

        ArrayList<Property> propertyPath = new ArrayList<>();
               
        if(getLineNum(n.getStartPosition()) == rawData.getStart()) {
	        //for current node description	        
	        List list = n.structuralPropertiesForType();
	        
	        //for get where the current node belongs to parent's property	        
	        ASTNode _n = n;
	        ASTNode tempParent = _n.getParent();
	        while(getLineNum(tempParent.getStartPosition())== rawData.getStart()) {
	        	Property parentProperty = new Property();			        
	        	int parentType = tempParent.getNodeType(); 
			    list = tempParent.structuralPropertiesForType();
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
			    if(tempParent == null)
			    	System.out.println("Null");
	        }	    
        }
//        
        
        push(type, typeName, label, startPos, len, node2String, propertyPath);    
    }

    private void push(int type, String typeName, String label, int startPosition, int length, String node2String,
    					ArrayList<Property> propertyPath ) {    	    	
    	ITree t = context.createTree(type, label, typeName);
        t.setPos(startPosition);
        t.setLength(length);
        if(cUnit.getLineNumber(startPosition) == rawData.getStart()) {
        	t.setStartLineNum(cUnit.getLineNumber(startPosition));
        	t.setEndLineNum(cUnit.getLineNumber(startPosition + length));
        	t.setNode2String(node2String);
        }
        
        ArrayList<Property> newProps = t.getParentProps();
        newProps.addAll(propertyPath);
        t.setParentProps(newProps);
        
//        int vMethodPos = 0;
//        int vMethodLen = 0;

//    	if(pData != null && pData.getVMethod() != null) {
//        	vMethodPos = pData.getVMethod().getPos();
//        	vMethodLen = pData.getVMethod().getLength();
//	
//        	if(vMethodPos <= startPosition && vMethodPos + vMethodLen >= startPosition + length){
//                    ITree parent = trees.peek();
//                    t.setParentAndUpdateChildren(parent);                
//        	}
//        }
        
    	if(t.getStartLineNum() == rawData.getStart()) {
    		ITree parent = trees.peek();
            t.setParentAndUpdateChildren(parent);
    	}
    	
        if(pData.getVNode() == null && (t.getStartLineNum() == rawData.getStart())) {        
        	pData.setVNode(t);
        } 
        
        trees.push(t);        
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
