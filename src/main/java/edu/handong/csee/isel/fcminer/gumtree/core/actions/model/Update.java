package edu.handong.csee.isel.fcminer.gumtree.core.actions.model;

import edu.handong.csee.isel.fcminer.gumtree.core.tree.ITree;
import edu.handong.csee.isel.fcminer.gumtree.core.tree.TreeContext;

public class Update extends Action {

    private String value;

    public Update(ITree node, String value) {
        super(node);
        this.value = value;
    }

    @Override
    public String getName() {
        return "UPD";
    }

    public String getValue() {
        return this.value;
    }

    @Override
    public String toString() {
        return getName() + " " + node.toShortString() + " from " + node.getLabel() + " to " + value;
    }

    @Override
    public String format(TreeContext ctx) {
        return getName() + " " + node.toPrettyString(ctx) + " from " + node.getLabel() + " to " + value;
    }

}
