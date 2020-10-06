package edu.handong.csee.isel.fcminer.gumtree.core.actions.model;

import edu.handong.csee.isel.fcminer.gumtree.core.tree.ITree;
import edu.handong.csee.isel.fcminer.gumtree.core.tree.TreeContext;

public abstract class Addition extends Action {

    protected ITree parent;

    protected int pos;

    public Addition(ITree node, ITree parent, int pos) {
        super(node);
        this.parent = parent;
        this.pos = pos;
    }

    public ITree getParent() {
        return parent;
    }

    public int getPosition() {
        return pos;
    }

    @Override
    public String toString() {
        return getName() + " " + node.toShortString() + " to " + parent.toShortString() + " at " + pos;
    }

    @Override
    public String format(TreeContext ctx) {
        return getName() + " " + node.toPrettyString(ctx) + " to " + parent.toPrettyString(ctx) + " at " + pos;
    }

}
