package edu.handong.csee.isel.fcminer.gumtree.core.matchers.heuristic.gt;

import edu.handong.csee.isel.fcminer.gumtree.core.matchers.Mapping;
import edu.handong.csee.isel.fcminer.gumtree.core.matchers.MappingStore;
import edu.handong.csee.isel.fcminer.gumtree.core.tree.ITree;

import java.util.*;

public final class SiblingsMappingComparator extends AbstractMappingComparator {

    private Map<ITree, List<ITree>> srcDescendants = new HashMap<>();

    private Map<ITree, Set<ITree>> dstDescendants = new HashMap<>();

    public SiblingsMappingComparator(List<Mapping> ambiguousMappings, MappingStore mappings, int maxTreeSize) {
        super(ambiguousMappings, mappings, maxTreeSize);
        for (Mapping ambiguousMapping: ambiguousMappings)
            similarities.put(ambiguousMapping, similarity(ambiguousMapping.getFirst(), ambiguousMapping.getSecond()));
    }

    @Override
    protected double similarity(ITree src, ITree dst) {
        return 100D * siblingsJaccardSimilarity(src.getParent(), dst.getParent())
                +  10D * posInParentSimilarity(src, dst) + numberingSimilarity(src , dst);
    }

    protected double siblingsJaccardSimilarity(ITree src, ITree dst) {
        double num = (double) numberOfCommonDescendants(src, dst);
        double den = (double) srcDescendants.get(src).size() + (double) dstDescendants.get(dst).size() - num;
        return num / den;
    }

    protected int numberOfCommonDescendants(ITree src, ITree dst) {
        if (!srcDescendants.containsKey(src))
            srcDescendants.put(src, src.getDescendants());
        if (!dstDescendants.containsKey(dst))
            dstDescendants.put(dst, new HashSet<>(dst.getDescendants()));

        int common = 0;

        for (ITree t: srcDescendants.get(src)) {
            ITree m = mappings.getDst(t);
            if (m != null && dstDescendants.get(dst).contains(m))
                common++;
        }

        return common;
    }

}