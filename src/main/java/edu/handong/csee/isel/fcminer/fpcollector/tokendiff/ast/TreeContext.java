package edu.handong.csee.isel.fcminer.fpcollector.tokendiff.ast;

public class TreeContext {

    private ITree root;

    public void setRoot(ITree root) {
        this.root = root;
    }

    public ITree getRoot() {
        return root;
    }

    public ITree createTree(int type, String label, String typeLabel) {
        return new Tree(type, label);
    }

    public ITree createTree(ITree... trees) {
        return new AbstractTree.FakeTree(trees);
    }

    public void validate() {
        root.refresh();
        TreeUtils.postOrderNumbering(root);
    }
}