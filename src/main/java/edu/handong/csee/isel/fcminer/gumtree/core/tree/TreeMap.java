package edu.handong.csee.isel.fcminer.gumtree.core.tree;

import gnu.trove.map.TIntObjectMap;
import gnu.trove.map.hash.TIntObjectHashMap;

public final class TreeMap {

    private TIntObjectMap<ITree> trees;

    public TreeMap(ITree tree) {
        this();
        putTrees(tree);
    }

    public TreeMap() {
        trees = new TIntObjectHashMap<>();
    }

    public ITree getTree(int id) {
        return trees.get(id);
    }

    public boolean contains(ITree tree) {
        return contains(tree.getId());
    }

    public boolean contains(int id) {
        return trees.containsKey(id);
    }

    public void putTrees(ITree tree) {
        for (ITree t: tree.getTrees())
            trees.put(t.getId(), t);
    }

    public void putTree(ITree t) {
        trees.put(t.getId(), t);
    }
}
