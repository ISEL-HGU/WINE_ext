package edu.handong.csee.isel.fcminer.gumtree.gen.jdt;

import java.util.ArrayDeque;
import java.util.Deque;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.CompilationUnit;

import edu.handong.csee.isel.fcminer.fpcollector.gumtree.Info;
import edu.handong.csee.isel.fcminer.gumtree.core.tree.ITree;
import edu.handong.csee.isel.fcminer.gumtree.core.tree.TreeContext;
import edu.handong.csee.isel.fcminer.gumtree.gen.jdt.cd.EntityType;

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

    protected void pushNode(ASTNode n, String label) {        	
        int type = n.getNodeType();
        String typeName = n.getClass().getSimpleName();
        Flag flag = push(type, typeName, label, n.getStartPosition(), n.getLength());
        if(flag == Flag.Method) {
        	info.setVMethodString(n.toString());
        }
    }

    protected void pushFakeNode(EntityType n, int startPosition, int length) {
        int type = -n.ordinal(); // Fake types have negative types (but does it matter ?)
        String typeName = n.name();
        push(type, typeName, "", startPosition, length);
    }

    private Flag push(int type, String typeName, String label, int startPosition, int length) {
        Flag flag= Flag.NULL;
    	ITree t = context.createTree(type, label, typeName);
        t.setPos(startPosition);
        t.setLength(length);

        if (trees.isEmpty())
            context.setRoot(t);
        else {
            ITree parent = trees.peek();
            t.setParentAndUpdateChildren(parent);
        }
        //for finding violating method
        if(type == 31 &&
    			getLineNum(startPosition) <= info.start 
    			&& info.end <= getLineNum(startPosition + length)){
			info.setVMethod(t);
			flag = Flag.Method;
		}
        
        //for finding violating node
        if(info.getVNode() == null &&
        		getLineNum(startPosition) == info.start && 
        		getLineNum(startPosition + length) >= info.end) {
        	info.setVNode(t);
//        	if(info.getVNode() == null) info.setVNode(t);
//        	else if(info.getVNode().getDepth() > t.getDepth()) info.setVNode(t);
		}
        
        trees.push(t);
        return flag;
    }
    
    protected ITree getCurrentParent() {
        return trees.peek();
    }

    protected void popNode() {
        trees.pop();
    }
    
	private int getLineNum(int startPosition){
		return cUnit.getLineNumber(startPosition);
	}
}
