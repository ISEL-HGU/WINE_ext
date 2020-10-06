package edu.handong.csee.isel.fcminer.gumtree.core.actions.model;

import edu.handong.csee.isel.fcminer.gumtree.core.tree.ITree;
import edu.handong.csee.isel.fcminer.gumtree.core.tree.TreeContext;

public class Delete extends Action {

    public Delete(ITree node) {
        super(node);
    }

    @Override
    public String getName() {
        return "DEL";
    }

    @Override
    public String toString() {
        return getName() + " " + node.toShortString();
    }

    @Override
    public String format(TreeContext ctx) {
        return getName() + " " + node.toPrettyString(ctx);
    }

}