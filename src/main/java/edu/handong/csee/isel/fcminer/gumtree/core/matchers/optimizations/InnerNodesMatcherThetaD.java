package edu.handong.csee.isel.fcminer.gumtree.core.matchers.optimizations;

import edu.handong.csee.isel.fcminer.gumtree.core.matchers.Mapping;
import edu.handong.csee.isel.fcminer.gumtree.core.matchers.MappingStore;
import edu.handong.csee.isel.fcminer.gumtree.core.matchers.Matcher;
import edu.handong.csee.isel.fcminer.gumtree.core.tree.ITree;

import java.util.Collections;
import java.util.Comparator;
import java.util.IdentityHashMap;
import java.util.LinkedList;
import java.util.Map.Entry;

/**
 * This implements the unmapped leaves optimization (Theta C), the inner node repair optimization
 * (Theta D) and the leaf move optimization (Theta E).
 *
 */
public class InnerNodesMatcherThetaD extends Matcher {

    private class ChangeMapComparator
            implements Comparator<Entry<ITree, IdentityHashMap<ITree, Integer>>> {

        @Override
        public int compare(Entry<ITree, IdentityHashMap<ITree, Integer>> o1,
                Entry<ITree, IdentityHashMap<ITree, Integer>> o2) {

            return Integer.compare(o1.getKey().getId(), o2.getKey().getId());
        }

    }

    /**
     * Instantiates a new matcher for Theta A-E.
     *
     * @param src the src
     * @param dst the dst
     * @param store the store
     */
    public InnerNodesMatcherThetaD(ITree src, ITree dst, MappingStore store) {
        super(src, dst, store);
    }

    @Override
    protected void addMapping(ITree src, ITree dst) {
        assert (src != null);
        assert (dst != null);
        super.addMapping(src, dst);
    }

    private boolean allowedMatching(ITree key, ITree maxNodePartner) {
        while (key != null) {
            if (key == maxNodePartner) {
                return false;
            }
            key = key.getParent();
        }
        return true;
    }


    /**
     * Match.
     */
    @Override
    public void match() {
        thetaD();
    }

    private void thetaD() {
        IdentityHashMap<ITree, IdentityHashMap<ITree, Integer>> parentCount =
                new IdentityHashMap<>();
        for (Mapping pair : mappings.asSet()) {
            ITree parent = pair.first.getParent();
            ITree parentPartner = pair.second.getParent();
            if (parent != null && parentPartner != null) {
                IdentityHashMap<ITree, Integer> countMap = parentCount.get(parent);
                if (countMap == null) {
                    countMap = new IdentityHashMap<>();
                    parentCount.put(parent, countMap);
                }
                Integer count = countMap.get(parentPartner);
                if (count == null) {
                    count = new Integer(0);
                }
                countMap.put(parentPartner, count + 1);
            }
        }

        LinkedList<Entry<ITree, IdentityHashMap<ITree, Integer>>> list =
                new LinkedList<>(parentCount.entrySet());
        Collections.sort(list, new ChangeMapComparator());

        for (Entry<ITree, IdentityHashMap<ITree, Integer>> countEntry : list) {
            int max = Integer.MIN_VALUE;
            int maxCount = 0;
            ITree maxNode = null;
            for (Entry<ITree, Integer> newNodeEntry : countEntry.getValue().entrySet()) {
                if (newNodeEntry.getValue() > max) {
                    max = newNodeEntry.getValue();
                    maxCount = 1;
                    maxNode = newNodeEntry.getKey();
                } else if (newNodeEntry.getValue() == max) {
                    maxCount++;
                }
            }
            if (maxCount == 1) {
                if (mappings.getDst(countEntry.getKey()) != null
                        && mappings.getSrc(maxNode) != null) {
                    ITree partner = mappings.getDst(countEntry.getKey());
                    ITree maxNodePartner = mappings.getSrc(maxNode);
                    if (partner != maxNode) {
                        if (max > countEntry.getKey().getChildren().size() / 2
                                || countEntry.getKey().getChildren().size() == 1) {
                            ITree parentPartner = mappings.getDst(countEntry.getKey().getParent());

                            if (parentPartner != null && parentPartner == partner.getParent()) {
                                continue;
                            }
                            if (allowedMatching(countEntry.getKey(), maxNodePartner)) {
                                if (countEntry.getKey().getType() == maxNode.getType()) {
                                    if (maxNodePartner != null) {
                                        mappings.unlink(maxNodePartner, maxNode);
                                    }
                                    if (partner != null) {
                                        mappings.unlink(countEntry.getKey(), partner);
                                    }
                                    addMapping(countEntry.getKey(), maxNode);
                                }
                                if (maxNodePartner != null) {
                                    if (maxNodePartner.getType() == partner.getType()) {
                                        addMapping(maxNodePartner, partner);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

}
