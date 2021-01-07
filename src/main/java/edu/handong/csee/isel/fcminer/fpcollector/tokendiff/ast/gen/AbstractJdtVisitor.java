package edu.handong.csee.isel.fcminer.fpcollector.tokendiff.ast.gen;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.List;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.ChildListPropertyDescriptor;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.StructuralPropertyDescriptor;

import edu.handong.csee.isel.fcminer.fpcollector.tokendiff.ast.ITree;
import edu.handong.csee.isel.fcminer.fpcollector.tokendiff.ast.TreeContext;
import edu.handong.csee.isel.fcminer.fpcollector.tokendiff.ast.hash.HashGenerator;
import edu.handong.csee.isel.fcminer.fpcollector.tokendiff.datapreproc.Info;

public abstract class AbstractJdtVisitor extends ASTVisitor {

    protected TreeContext context = new TreeContext();

    private Deque<ITree> trees = new ArrayDeque<>();
    
    private Info info;
    
    private CompilationUnit cUnit;
    
    enum Flag {
    	Method, NULL
    }
    
    public AbstractJdtVisitor(Info info) {
    	super(true);
    	this.info = info;
    }

    public void setCUnit(CompilationUnit cUnit) {
    	this.cUnit = cUnit;
    }
    
    public Info getInfo() {
    	return info;
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
//      
        ArrayList<Property> propertyPath = new ArrayList<>();
        
        String childProps = "P(";
        if(info.getVMethod() != null) {
	        //for current node description	        
	        List list = n.structuralPropertiesForType();
	        for(int i = 0 ; i < list.size(); i ++) {
	        	StructuralPropertyDescriptor prop = (StructuralPropertyDescriptor) list.get(i);
	        	childProps += prop.getId() + " ";
	        }                
	        childProps += ")";
	        
	        //for get where the current node belongs to parent's property
	        if(getLineNum(n.getStartPosition()) >= info.getStart() && getLineNum(n.getStartPosition()) <= info.getEnd()) {
	        	ASTNode _n = n;
	        	ASTNode tempParent = _n.getParent();
	        	while(getLineNum(tempParent.getStartPosition())>= info.getStart() 
	        			&& getLineNum(tempParent.getStartPosition())<= info.getEnd()) {
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
        }
//        
        
        Flag flag = push(type, typeName, label, startPos, len, node2String, childProps, propertyPath);
        if(flag == Flag.Method) {
          	info.setVMethodString(node2String);
        }        
    }

    private Flag push(int type, String typeName, String label, int startPosition, int length, String node2String,
    					String childProps, ArrayList<Property> propertyPath ) {    	
    	Flag flag= Flag.NULL;
    	ITree t = context.createTree(type, label, typeName);
        t.setPos(startPosition);
        t.setLength(length);
        t.setStartLineNum(cUnit.getLineNumber(startPosition));
        t.setEndLineNum(cUnit.getLineNumber(startPosition + length));
        t.setNode2String(node2String);
        t.setChildProps(childProps);
        
        ArrayList<Property> newProps = t.getParentProps();
        newProps.addAll(propertyPath);
        t.setParentProps(newProps);
        
        int vMethodPos = 0;
        int vMethodLen = 0;

    	if(info.getVMethod() != null) {
        	vMethodPos = info.getVMethod().getPos();
        	vMethodLen = info.getVMethod().getLength();
	
        	if(vMethodPos <= startPosition && vMethodPos + vMethodLen >= startPosition + length){
                    ITree parent = trees.peek();
                    t.setParentAndUpdateChildren(parent);                
        	}
        }

        //for finding violating method
        if(type == 31 &&
    			getLineNum(startPosition) <= info.getStart()
    			&& info.getEnd() <= getLineNum(startPosition + length)){
        	info.setVMethod(t);
        	context.setRoot(t);
			flag = Flag.Method;
		}        
        
        //for finding violating node
//        if(info.getVNode() == null &&
//        		getLineNum(startPosition) == info.start && 
//        		getLineNum(startPosition + length) >= info.end) {
//        	info.setVNode(t);
//        	if(info.getVNode() == null) info.setVNode(t);
//        	else if(info.getVNode().getDepth() > t.getDepth()) info.setVNode(t);
//		}
        
        trees.push(t);
        return flag;
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
