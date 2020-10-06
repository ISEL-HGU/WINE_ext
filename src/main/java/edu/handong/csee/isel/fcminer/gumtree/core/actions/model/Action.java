package edu.handong.csee.isel.fcminer.gumtree.core.actions.model;

import edu.handong.csee.isel.fcminer.gumtree.core.tree.ITree;
import edu.handong.csee.isel.fcminer.gumtree.core.tree.TreeContext;

public abstract class Action {

    protected ITree node;

    public Action(ITree node) {
        this.node = node;
    }

    public ITree getNode() {
        return node;
    }

    public void setNode(ITree node) {
        this.node = node;
    }

    public abstract String getName();

    @Override
    public abstract String toString();

    public abstract String format(TreeContext ctx);

}

