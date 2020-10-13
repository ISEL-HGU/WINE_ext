package edu.handong.csee.isel.fcminer.gumtree.core.actions;

import edu.handong.csee.isel.fcminer.gumtree.core.actions.model.*;
import edu.handong.csee.isel.fcminer.gumtree.core.matchers.Mapping;
import edu.handong.csee.isel.fcminer.gumtree.core.matchers.MappingStore;
import edu.handong.csee.isel.fcminer.gumtree.core.tree.AbstractTree;
import edu.handong.csee.isel.fcminer.gumtree.core.tree.ITree;
import edu.handong.csee.isel.fcminer.gumtree.core.tree.TreeUtils;
import gnu.trove.map.TIntObjectMap;
import gnu.trove.map.hash.TIntObjectHashMap;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ActionGenerator {
	//parsed original source code
    private ITree origSrc;
    //copy version of original source code
    private ITree newSrc;
    //parsed original destination code
    private ITree origDst;
    //mapping node id with ITree node
    private MappingStore origMappings;
    //copy version of original mapping
    private MappingStore newMappings;
    
    private Set<ITree> dstInOrder;

    private Set<ITree> srcInOrder;

    private int lastId;

    private List<Action> actions;
    //original parsed source code map, Key: node id, value: node
    private TIntObjectMap<ITree> origSrcTrees;
    //copy version parsed source code map, Key: node id, value: node
    private TIntObjectMap<ITree> cpySrcTrees;
    
//    private ArrayList<ITree> updatedNodes = new ArrayList<>();
    
//    private ArrayList<ITree> movedNodes = new ArrayList<>();
    
//    private ArrayList<ITree> maintainedNodes = new ArrayList<>();
    private ArrayList<ITree> commonNodes = new ArrayList<>();

    public ActionGenerator(ITree src, ITree dst, MappingStore mappings) {
        this.origSrc = src;
        this.newSrc = this.origSrc.deepCopy();
        this.origDst = dst;

        origSrcTrees = new TIntObjectHashMap<>();
        for (ITree t: origSrc.getTrees())
            origSrcTrees.put(t.getId(), t);
        cpySrcTrees = new TIntObjectHashMap<>();
        for (ITree t: newSrc.getTrees())
            cpySrcTrees.put(t.getId(), t);

        origMappings = new MappingStore();
        for (Mapping m: mappings) {
            this.origMappings.link(cpySrcTrees.get(m.getFirst().getId()), m.getSecond());
//            System.out.println(m.getFirst());
//            System.out.println(m.getSecond());
        }
        this.newMappings = origMappings.copy();
    }

    private enum actionStatus{
    	UPD, MOV, MAI
    }
    
    public List<Action> getActions() {
        return actions;
    }

    public List<Action> generate() {
    	//build fake tree of copy version of original source code
        ITree srcFakeRoot = new AbstractTree.FakeTree(newSrc);
        //build fake tree of original destination code
        ITree dstFakeRoot = new AbstractTree.FakeTree(origDst);
        //add fake source tree as parent to copy version of original source code
        newSrc.setParent(srcFakeRoot);
        //add fake destination tree as parent to original destination code
        origDst.setParent(dstFakeRoot);

        actions = new ArrayList<>();
        dstInOrder = new HashSet<>();
        srcInOrder = new HashSet<>();
        
        //set last id as +1 of newSrc size
        lastId = newSrc.getSize() + 1;
        //link fake trees
        newMappings.link(srcFakeRoot, dstFakeRoot);

        //apply BFS to Original Destination Tree
        List<ITree> bfsDst = TreeUtils.breadthFirst(origDst);
        
        //x is BFS node of destination
        for (ITree x: bfsDst) {
            ITree w = null;
            //y is parent of BFS Node x
            ITree y = x.getParent();
            //z is mapping node which is mapped with x's parent
            ITree z = newMappings.getSrc(y);
            
            //if there is no mapped information of destination node --> it means that inserted
            if (!newMappings.hasDst(x)) {
                int k = findPos(x);
                // Insertion case : insert new node.
                w = new AbstractTree.FakeTree();
                // +1 to last ID
                w.setId(newId());
                // In order to use the real nodes from the second tree, we
                // furnish x instead of w and fake that x has the newly
                // generated ID.
                // Insert Node(ITree Node, get Parent of x, Position)
                Action ins = new Insert(x, origSrcTrees.get(z.getId()), k);
                actions.add(ins);
                //System.out.println(ins);
                //add x to original source with new ID
                origSrcTrees.put(w.getId(), x);
                //mapping with w(newly inserted fake node) and x
                newMappings.link(w, x);
                z.getChildren().add(k, w);
                w.setParent(z);
            } else {
                w = newMappings.getSrc(x);
                actionStatus status = actionStatus.MAI;
                if (!x.equals(origDst)) { // TODO => x != origDst // Case of the root
                    ITree v = w.getParent();
                    if (!w.getLabel().equals(x.getLabel())) {
//                    	updatedNodes.add(origSrcTrees.get(w.getId()));
                    	if(!commonNodes.contains(origSrcTrees.get(w.getId())))
                    		commonNodes.add(origSrcTrees.get(w.getId()));
                        actions.add(new Update(origSrcTrees.get(w.getId()), x.getLabel()));
                        w.setLabel(x.getLabel());
                        status = actionStatus.UPD;
                    }
                    if (!z.equals(v)) {
                        int k = findPos(x);
//                        movedNodes.add(origSrcTrees.get(w.getId()));
                        if(!commonNodes.contains(origSrcTrees.get(w.getId())))
                        	commonNodes.add(origSrcTrees.get(w.getId()));
                        Action mv = new Move(origSrcTrees.get(w.getId()), origSrcTrees.get(z.getId()), k);
                        actions.add(mv);
                        //System.out.println(mv);
                        int oldk = w.positionInParent();
                        z.getChildren().add(k, w);
                        w.getParent().getChildren().remove(oldk);
                        w.setParent(z);
                        status = actionStatus.MOV;
                    }
                    if(status == actionStatus.MAI) {
//                    	maintainedNodes.add(origSrcTrees.get(w.getId()));
                    	if(!commonNodes.contains(origSrcTrees.get(w.getId())))
                    		commonNodes.add(origSrcTrees.get(w.getId()));
                    }
                }
            } 

            //FIXME not sure why :D
            srcInOrder.add(w);
            dstInOrder.add(x);
            alignChildren(w, x);
        }

        for (ITree w : newSrc.postOrder()) {
            if (!newMappings.hasSrc(w)) {
                actions.add(new Delete(origSrcTrees.get(w.getId())));
                //w.getParent().getChildren().remove(w);
            }
        }

        //FIXME should ensure isomorphism.
        return actions;
    }

    private void alignChildren(ITree w, ITree x) {
        srcInOrder.removeAll(w.getChildren());
        dstInOrder.removeAll(x.getChildren());

        List<ITree> s1 = new ArrayList<>();
        for (ITree c: w.getChildren())
            if (newMappings.hasSrc(c))
                if (x.getChildren().contains(newMappings.getDst(c)))
                    s1.add(c);

        List<ITree> s2 = new ArrayList<>();
        for (ITree c: x.getChildren())
            if (newMappings.hasDst(c))
                if (w.getChildren().contains(newMappings.getSrc(c)))
                    s2.add(c);

        List<Mapping> lcs = lcs(s1, s2);

        for (Mapping m : lcs) {
            srcInOrder.add(m.getFirst());
            dstInOrder.add(m.getSecond());
        }

        for (ITree a : s1) {
            for (ITree b: s2 ) {
                if (origMappings.has(a, b)) {
                    if (!lcs.contains(new Mapping(a, b))) {
                        int k = findPos(b);
                        Action mv = new Move(origSrcTrees.get(a.getId()), origSrcTrees.get(w.getId()), k);
                        actions.add(mv);
                        //System.out.println(mv);
                        int oldk = a.positionInParent();
                        w.getChildren().add(k, a);
                        if (k  < oldk ) // FIXME this is an ugly way to patch the index
                            oldk ++;
                        a.getParent().getChildren().remove(oldk);
                        a.setParent(w);
                        srcInOrder.add(a);
                        dstInOrder.add(b);
                    }
                }
            }
        }
    }

    private int findPos(ITree x) {
        ITree y = x.getParent();
        List<ITree> siblings = y.getChildren();

        for (ITree c : siblings) {
            if (dstInOrder.contains(c)) {
                if (c.equals(x)) return 0;
                else break;
            }
        }

        int xpos = x.positionInParent();
        ITree v = null;
        for (int i = 0; i < xpos; i++) {
            ITree c = siblings.get(i);
            if (dstInOrder.contains(c)) v = c;
        }

        //if (v == null) throw new RuntimeException("No rightmost sibling in order");
        if (v == null) return 0;

        ITree u = newMappings.getSrc(v);
        // siblings = u.getParent().getChildren();
        // int upos = siblings.indexOf(u);
        int upos = u.positionInParent();
        // int r = 0;
        // for (int i = 0; i <= upos; i++)
        // if (srcInOrder.contains(siblings.get(i))) r++;
        return upos + 1;
    }

    private int newId() {
        return ++lastId;
    }

    private List<Mapping> lcs(List<ITree> x, List<ITree> y) {
        int m = x.size();
        int n = y.size();
        List<Mapping> lcs = new ArrayList<>();

        int[][] opt = new int[m + 1][n + 1];
        for (int i = m - 1; i >= 0; i--) {
            for (int j = n - 1; j >= 0; j--) {
                if (newMappings.getSrc(y.get(j)).equals(x.get(i))) opt[i][j] = opt[i + 1][j + 1] + 1;
                else  opt[i][j] = Math.max(opt[i + 1][j], opt[i][j + 1]);
            }
        }

        int i = 0, j = 0;
        while (i < m && j < n) {
            if (newMappings.getSrc(y.get(j)).equals(x.get(i))) {
                lcs.add(new Mapping(x.get(i), y.get(j)));
                i++;
                j++;
            } else if (opt[i + 1][j] >= opt[i][j + 1]) i++;
            else j++;
        }

        return lcs;
    }

    public MappingStore getOrigMapping() {
    	return origMappings;
    }
    
    public ArrayList<ITree> getCommonNodes(){
    	return commonNodes;
    }
    
//    public ArrayList<ITree> getUpdatedNodes(){
//    	return updatedNodes;
//    }
//    
//    public ArrayList<ITree> getMovedNodes(){
//    	return movedNodes;
//    }
//    
//    public ArrayList<ITree> getMaintainedNodes(){
//    	return maintainedNodes;
//    }
    
}