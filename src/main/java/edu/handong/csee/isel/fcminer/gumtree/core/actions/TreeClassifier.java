package edu.handong.csee.isel.fcminer.gumtree.core.actions;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import edu.handong.csee.isel.fcminer.gumtree.core.actions.model.Action;
import edu.handong.csee.isel.fcminer.gumtree.core.matchers.Mapping;
import edu.handong.csee.isel.fcminer.gumtree.core.matchers.MappingStore;
import edu.handong.csee.isel.fcminer.gumtree.core.matchers.Matcher;
import edu.handong.csee.isel.fcminer.gumtree.core.tree.ITree;
import edu.handong.csee.isel.fcminer.gumtree.core.tree.TreeContext;

public abstract class TreeClassifier {

    protected Set<ITree> srcUpdTrees;

    protected Set<ITree> dstUpdTrees;

    protected Set<ITree> srcMvTrees;

    protected Set<ITree> dstMvTrees;

    protected Set<ITree> srcDelTrees;

    protected Set<ITree> dstAddTrees;

    protected TreeContext src;

    protected TreeContext dst;

    protected MappingStore mappings;

    protected List<Action> actions;

    public TreeClassifier(TreeContext src, TreeContext dst, Set<Mapping> rawMappings, List<Action> actions) {
        this(src, dst, rawMappings);
        this.actions = actions;
        classify();
    }

    public TreeClassifier(TreeContext src, TreeContext dst, Matcher m) {
        this(src, dst, m.getMappingsAsSet());
        ActionGenerator g = new ActionGenerator(src.getRoot(), dst.getRoot(), m.getMappings());
        g.generate();
        this.actions = g.getActions();
        classify();
    }

    private TreeClassifier(TreeContext src, TreeContext dst, Set<Mapping> rawMappings) {
        this.src = src;
        this.dst = dst;
        this.mappings = new MappingStore(rawMappings);
        this.srcDelTrees = new HashSet<>();
        this.srcMvTrees = new HashSet<>();
        this.srcUpdTrees = new HashSet<>();
        this.dstMvTrees = new HashSet<>();
        this.dstAddTrees = new HashSet<>();
        this.dstUpdTrees = new HashSet<>();
    }

    public abstract void classify();

    public Set<ITree> getSrcUpdTrees() {
        return srcUpdTrees;
    }

    public Set<ITree> getDstUpdTrees() {
        return dstUpdTrees;
    }

    public Set<ITree> getSrcMvTrees() {
        return srcMvTrees;
    }

    public Set<ITree> getDstMvTrees() {
        return dstMvTrees;
    }

    public Set<ITree> getSrcDelTrees() {
        return srcDelTrees;
    }

    public Set<ITree> getDstAddTrees() {
        return dstAddTrees;
    }

}

