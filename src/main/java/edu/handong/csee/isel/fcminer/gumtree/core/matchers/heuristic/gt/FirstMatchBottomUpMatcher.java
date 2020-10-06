package edu.handong.csee.isel.fcminer.gumtree.core.matchers.heuristic.gt;

import edu.handong.csee.isel.fcminer.gumtree.core.matchers.MappingStore;
import edu.handong.csee.isel.fcminer.gumtree.core.tree.ITree;

/**
 * Match the nodes using a bottom-up approach. It browse the nodes of the source and destination trees
 * using a post-order traversal, testing if the two selected trees might be mapped. The two trees are mapped 
 * if they are mappable and have a dice coefficient greater than SIM_THRESHOLD. Whenever two trees are mapped
 * a exact ZS algorithm is applied to look to possibly forgotten nodes.
 */
public class FirstMatchBottomUpMatcher extends AbstractBottomUpMatcher {

    public FirstMatchBottomUpMatcher(ITree src, ITree dst, MappingStore store) {
        super(src, dst, store);
    }

    @Override
    public void match() {
        match(removeMatched(src, true), removeMatched(dst, false));
    }

    private void match(ITree src, ITree dst) {
        for (ITree s: src.postOrder())  {
            for (ITree d: dst.postOrder()) {
                if (isMappingAllowed(s, d) && !(s.isLeaf() || d.isLeaf())) {
                    double sim = jaccardSimilarity(s, d);
                    if (sim >= SIM_THRESHOLD || (s.isRoot() && d.isRoot()) ) {
                        if (!(areDescendantsMatched(s, true) || areDescendantsMatched(d, false)))
                            lastChanceMatch(s, d);
                        addMapping(s, d);
                        break;
                    }
                }
            }
        }
    }

    /**
     * Indicate whether or not all the descendants of the trees are already mapped.
     */
    public boolean areDescendantsMatched(ITree tree, boolean isSrc) {
        for (ITree c: tree.getDescendants())
            if (!((isSrc && isSrcMatched(c)) || (!isSrc && isDstMatched(tree)))) // FIXME ugly but this class is unused
                return false;
        return true;
    }

}