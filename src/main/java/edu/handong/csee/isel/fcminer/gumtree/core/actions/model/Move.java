package edu.handong.csee.isel.fcminer.gumtree.core.actions.model;

import edu.handong.csee.isel.fcminer.gumtree.core.actions.model.Addition;
import edu.handong.csee.isel.fcminer.gumtree.core.tree.ITree;

public class Move extends Addition {

    public Move(ITree node, ITree parent, int pos) {
        super(node, parent, pos);
    }

    @Override
    public String getName() {
        return "MOV";
    }

}
