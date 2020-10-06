package edu.handong.csee.isel.fcminer.gumtree.core.matchers.heuristic.gt;

import edu.handong.csee.isel.fcminer.gumtree.core.matchers.Mapping;
import edu.handong.csee.isel.fcminer.gumtree.core.matchers.MappingStore;
import edu.handong.csee.isel.fcminer.gumtree.core.tree.ITree;
import edu.handong.csee.isel.fcminer.gumtree.core.utils.StringAlgorithms;

import java.util.*;

public final class ParentsMappingComparator extends AbstractMappingComparator {

    public ParentsMappingComparator(List<Mapping> ambiguousMappings, MappingStore mappings, int maxTreeSize) {
        super(ambiguousMappings, mappings, maxTreeSize);
        for (Mapping ambiguousMapping: ambiguousMappings)
            similarities.put(ambiguousMapping, similarity(ambiguousMapping.getFirst(), ambiguousMapping.getSecond()));
    }

    @Override
    protected double similarity(ITree src, ITree dst) {
        return 100D * parentsJaccardSimilarity(src, dst)
                + 10D * posInParentSimilarity(src, dst) + numberingSimilarity(src , dst);
    }

    protected double parentsJaccardSimilarity(ITree src, ITree dst) {
        List<ITree> srcParents = src.getParents();
        List<ITree> dstParents = dst.getParents();
        double numerator = (double) StringAlgorithms.lcss(srcParents, dstParents).size();
        double denominator = (double) srcParents.size() + (double) dstParents.size() - numerator;
        return numerator / denominator;
    }

}