package edu.handong.csee.isel.fcminer.gumtree.core.tree;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

public class Tree extends AbstractTree implements ITree {
	
	private int startLineNum = -1;
	
	private int endLineNum = -1;
	
    private int type;

    private String label;
    
    private String node2String;
    
    private String childProps = "";
    
    private ArrayList<String> parentProps = new ArrayList<>();

    // Begin position of the tree in terms of absolute character index and length
    private int pos;
    private int length;
    // End position

    private AssociationMap metadata;

    /**
     * Constructs a new node. If you need type labels corresponding to the integer
     * @see TreeContext#createTree(int, String, String)
     */
    public Tree(int type, String label) {
        this.type = type;
        this.label = (label == null) ? NO_LABEL : label.intern();
        this.id = NO_ID;
        this.depth = NO_VALUE;
        this.hash = NO_VALUE;
        this.height = NO_VALUE;
        this.depth = NO_VALUE;
        this.size = NO_VALUE;
        this.pos = NO_VALUE;
        this.length = NO_VALUE;
        this.children = new ArrayList<>();
    }

    // Only used for cloning ...
    protected Tree(Tree other) {
        this.type = other.type;
        this.label = other.getLabel();
        this.id = other.getId();
        this.pos = other.getPos();
        this.length = other.getLength();
        this.height = other.getHeight();
        this.size = other.getSize();
        this.depth = other.getDepth();
        this.hash = other.getHash();
        this.depth = other.getDepth();
        this.children = new ArrayList<>();
        this.metadata = other.metadata;
    }         
    
    @Override
    public void addChild(ITree t) {
        children.add(t);
        t.setParent(this);
    }

    @Override
    public void insertChild(ITree t, int position) {
        children.add(position, t);
        t.setParent(this);
    }

    @Override
    public Tree deepCopy() {
        Tree copy = new Tree(this);
        for (ITree child : getChildren())
            copy.addChild(child.deepCopy());
        return copy;
    }

    @Override
    public List<ITree> getChildren() {
        return children;
    }

    @Override
    public String getLabel() {
        return label;
    }

    @Override
    public int getLength() {
        return length;
    }

    @Override
    public ITree getParent() {
        return parent;
    }

    @Override
    public int getPos() {
        return pos;
    }

    @Override
    public int getType() {
        return type;
    }
    
    @Override
    public int getStartLineNum() {
    	return startLineNum;
    }
    
    @Override
    public int getEndLineNum() {
    	return endLineNum;
    }
    
    @Override
    public String getNode2String() {
    	return node2String;
    }
    
    @Override
    public String getChildProps() {
    	return childProps;
    }
    
    
    
    @Override
    public ArrayList<String> getParentProps() {
    	return parentProps;
    }
    
    @Override
    public void setParentProps(ArrayList<String> parentProps) {
    	this.parentProps = parentProps;
    }
    
    @Override
    public void setChildProps(String childProps) {
    	this.childProps = childProps;
    }
    
    @Override
    public void setNode2String(String node2String) {
    	this.node2String = node2String;
    }

    @Override
    public void setChildren(List<ITree> children) {
        this.children = children;
        for (ITree c : children)
            c.setParent(this);
    }

    @Override
    public void setLabel(String label) {
        this.label = label;
    }

    @Override
    public void setLength(int length) {
        this.length = length;
    }

    @Override
    public void setParent(ITree parent) {
        this.parent = parent;
    }

    @Override
    public void setParentAndUpdateChildren(ITree parent) {
        if (this.parent != null)
            this.parent.getChildren().remove(this);
        this.parent = parent;
        if (this.parent != null)
            parent.getChildren().add(this);
    }

    @Override
    public void setPos(int pos) {
        this.pos = pos;
    }

    @Override
    public void setType(int type) {
        this.type = type;
    }
    
    @Override
    public void setStartLineNum(int startLineNum) {
    	this.startLineNum = startLineNum;
    }
    
    @Override
    public void setEndLineNum(int endLineNum) {
    	this.endLineNum = endLineNum;
    }

    @Override
    public Object getMetadata(String key) {
        if (metadata == null)
            return null;
        return metadata.get(key);
    }

    @Override
    public Object setMetadata(String key, Object value) {
        if (value == null) {
            if (metadata == null)
                return null;
            else
                return metadata.remove(key);
        }
        if (metadata == null)
            metadata = new AssociationMap();
        return metadata.set(key, value);
    }

    @Override
    public Iterator<Entry<String, Object>> getMetadata() {
        if (metadata == null)
            return new EmptyEntryIterator();
        return metadata.iterator();
    }
}