package edu.handong.csee.isel.fcminer.fpcollector.tokendiff.ast;

import java.util.*;

import edu.handong.csee.isel.fcminer.fpcollector.tokendiff.ast.gen.Property;

public abstract class AbstractTree implements ITree {

    protected int id;

    protected ITree parent;

    protected List<ITree> children;
    
    protected int depth;

    @Override
    public int getChildPosition(ITree child) {
        return getChildren().indexOf(child);
    }

    @Override
    public ITree getChild(int position) {
        return getChildren().get(position);
    }

    @Override
    public int getDepth() {
        return depth;
    }

    @Override
    public List<ITree> getDescendants() {
        List<ITree> trees = TreeUtils.preOrder(this);
        trees.remove(0);
        return trees;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public boolean hasLabel() {
        return !NO_LABEL.equals(getLabel());
    }

    @Override
    public ITree getParent() {
        return parent;
    }

    @Override
    public void setParent(ITree parent) {
        this.parent = parent;
    }

    @Override
    public List<ITree> getParents() {
        List<ITree> parents = new ArrayList<>();
        if (getParent() == null)
            return parents;
        else {
            parents.add(getParent());
            parents.addAll(getParent().getParents());
        }
        return parents;
    }
    
    @Override
    public List<ITree> getTrees() {
        return TreeUtils.preOrder(this);
    }

    @Override
    public boolean hasSameType(ITree t) {
        return getType() == t.getType();
    }

    @Override
    public boolean isLeaf() {
        return getChildren().size() == 0;
    }

    @Override
    public boolean isRoot() {
        return getParent() == null;
    }

    @Override
    public boolean hasSameTypeAndLabel(ITree t) {
        if (!hasSameType(t))
            return false;
        else if (!getLabel().equals(t.getLabel()))
            return false;
        return true;
    }

    @Override
    public Iterable<ITree> preOrder() {
        return new Iterable<ITree>() {
            @Override
            public Iterator<ITree> iterator() {
                return TreeUtils.preOrderIterator(AbstractTree.this);
            }
        };
    }

    @Override
    public Iterable<ITree> postOrder() {
        return new Iterable<ITree>() {
            @Override
            public Iterator<ITree> iterator() {
                return TreeUtils.postOrderIterator(AbstractTree.this);
            }
        };
    }

    @Override
    public Iterable<ITree> breadthFirst() {
        return new Iterable<ITree>() {
            @Override
            public Iterator<ITree> iterator() {
                return TreeUtils.breadthFirstIterator(AbstractTree.this);
            }
        };
    }

    @Override
    public int positionInParent() {
        ITree p = getParent();
        if (p == null)
            return -1;
        else
            return p.getChildren().indexOf(this);
    }

    @Override
    public void refresh() {
        TreeUtils.computeDepth(this);
    }

    @Override
    public void setDepth(int depth) {
        this.depth = depth;
    }

    @Override
    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toStaticHashString() {
        StringBuilder b = new StringBuilder();
        b.append(OPEN_SYMBOL);
        b.append(this.toShortString());
        for (ITree c: this.getChildren())
            b.append(c.toStaticHashString());
        b.append(CLOSE_SYMBOL);
        return b.toString();
    }

    @Override
    public String toString() {
        System.err.println("This method should currently not be used (please use toShortString())");
        return toShortString();
    }

    @Override
    public String toShortString() {
        return String.format("%d%s%s", getType(), SEPARATE_SYMBOL, getLabel());
    }

    public static class FakeTree extends AbstractTree {
        public FakeTree(ITree... trees) {
            children = new ArrayList<>(trees.length);
            children.addAll(Arrays.asList(trees));
        }

        private RuntimeException unsupportedOperation() {
            return new UnsupportedOperationException("This method should not be called on a fake tree");
        }

        @Override
        public void addChild(ITree t) {
            throw unsupportedOperation();
        }

        @Override
        public void insertChild(ITree t, int position) {
            throw unsupportedOperation();
        }

        @Override
        public ITree deepCopy() {
            throw unsupportedOperation();
        }
        
        @Override
        public ArrayList<Property> getParentProps() {
        	throw unsupportedOperation();
        }
        
        @Override
        public void setParentProps(ArrayList<Property> parentProps) {
        	throw unsupportedOperation();
        }
        
        @Override
        public List<ITree> getChildren() {
            return children;
        }

        @Override
        public String getLabel() {
            return NO_LABEL;
        }

        @Override
        public int getLength() {
            return getEndPos() - getPos();
        }

        @Override
        public int getPos() {
            return Collections.min(children, (t1, t2) -> t2.getPos() - t1.getPos()).getPos();
        }

        @Override
        public int getEndPos() {
            return Collections.max(children, (t1, t2) -> t2.getPos() - t1.getPos()).getEndPos();
        }

        @Override
        public int getType() {
            return -1;
        }

        @Override
        public int getStartLineNum() {
        	return 0;
        }
        
        @Override
        public int getEndLineNum() {
        	return 0;
        }
        
        @Override
        public String getNode2String() {
        	return "";
        }
        
        @Override
        public void setNode2String(String node2String) {
        	throw unsupportedOperation();
        }
        
        @Override
        public void setChildren(List<ITree> children) {
            throw unsupportedOperation();
        }

        @Override
        public void setLabel(String label) {
            throw unsupportedOperation();
        }

        @Override
        public void setLength(int length) {
            throw unsupportedOperation();
        }

        @Override
        public void setParentAndUpdateChildren(ITree parent) {
            throw unsupportedOperation();
        }

        @Override
        public void setPos(int pos) {
            throw unsupportedOperation();
        }

        @Override
        public void setType(int type) {
            throw unsupportedOperation();
        }

        @Override
        public void setEndLineNum(int lineNum) {
        	throw unsupportedOperation();
        }
        
        @Override
        public void setStartLineNum(int lineNum) {
        	throw unsupportedOperation();
        }

    }

}