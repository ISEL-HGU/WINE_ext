package edu.handong.csee.isel.fcminer.gumtree.core.matchers.heuristic.gt;

import edu.handong.csee.isel.fcminer.gumtree.core.matchers.MappingStore;
import edu.handong.csee.isel.fcminer.gumtree.core.tree.ITree;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Match the nodes using a bottom-up approach. It browse the nodes of the source and destination trees
 * using a post-order traversal, testing if the two selected trees might be mapped. The two trees are mapped 
 * if they are mappable and have a dice coefficient greater than SIM_THRESHOLD. Whenever two trees are mapped
 * a exact ZS algorithm is applied to look to possibly forgotten nodes.
 */
public class CompleteBottomUpMatcher extends AbstractBottomUpMatcher {

    public CompleteBottomUpMatcher(ITree src, ITree dst, MappingStore store) {
        super(src, dst, store);
    }

    @Override
    public void match() {
        for (ITree t: src.postOrder())  {
            if (t.isRoot()) {
                addMapping(t, this.dst);
                lastChanceMatch(t, this.dst);
                break;
            } else if (!(isSrcMatched(t) || t.isLeaf())) {
                List<ITree> srcCandidates = t.getParents().stream()
                        .filter(p -> p.getType() == t.getType())
                        .collect(Collectors.toList());

                List<ITree> dstCandidates = getDstCandidates(t);
                ITree srcBest = null;
                ITree dstBest = null;
                double max = -1D;
                for (ITree srcCand: srcCandidates) {
                    for (ITree dstCand: dstCandidates) {

                        double sim = jaccardSimilarity(srcCand, dstCand);
                        if (sim > max && sim >= SIM_THRESHOLD) {
                            max = sim;
                            srcBest = srcCand;
                            dstBest = dstCand;
                        }
                    }
                }

                if (srcBest != null) {
                    lastChanceMatch(srcBest, dstBest);
                    addMapping(srcBest, dstBest);
                }
            }
        }
    }
}
