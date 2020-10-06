package edu.handong.csee.isel.fcminer.gumtree.core.actions;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import edu.handong.csee.isel.fcminer.gumtree.core.actions.model.Delete;
import edu.handong.csee.isel.fcminer.gumtree.core.actions.model.Action;
import edu.handong.csee.isel.fcminer.gumtree.core.actions.model.Insert;
import edu.handong.csee.isel.fcminer.gumtree.core.actions.model.Move;
import edu.handong.csee.isel.fcminer.gumtree.core.actions.model.Update;
import edu.handong.csee.isel.fcminer.gumtree.core.matchers.Mapping;
import edu.handong.csee.isel.fcminer.gumtree.core.matchers.Matcher;
import edu.handong.csee.isel.fcminer.gumtree.core.tree.ITree;
import edu.handong.csee.isel.fcminer.gumtree.core.tree.TreeContext;

public class RootAndLeavesClassifier extends TreeClassifier {

    public RootAndLeavesClassifier(TreeContext src, TreeContext dst, Set<Mapping> rawMappings, List<Action> actions) {
        super(src, dst, rawMappings, actions);
    }

    public RootAndLeavesClassifier(TreeContext src, TreeContext dst, Matcher m) {
        super(src, dst, m);
    }

    @Override
    public void classify() {
        for (Action a: actions) {
            if (a instanceof Insert) {
                dstAddTrees.add(a.getNode());
            } else if (a instanceof Delete) {
                srcDelTrees.add(a.getNode());
            } else if (a instanceof Update) {
                srcUpdTrees.add(a.getNode());
                dstUpdTrees.add(mappings.getDst(a.getNode()));
            } else if (a instanceof Move) {
                srcMvTrees.add(a.getNode());
                dstMvTrees.add(mappings.getDst(a.getNode()));
            }
        }

        Set<ITree> fDstAddTrees = new HashSet<>();
        for (ITree t: dstAddTrees)
            if (!dstAddTrees.contains(t.getParent()))
                fDstAddTrees.add(t);
        dstAddTrees = fDstAddTrees;

        Set<ITree> fSrcDelTrees = new HashSet<>();
        for (ITree t: srcDelTrees) {
            if (!srcDelTrees.contains(t.getParent()))
                fSrcDelTrees.add(t);
        }
        srcDelTrees = fSrcDelTrees;
    }

}