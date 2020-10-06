package edu.handong.csee.isel.fcminer.gumtree.core.matchers.heuristic.gt;

import edu.handong.csee.isel.fcminer.gumtree.core.matchers.Mapping;
import edu.handong.csee.isel.fcminer.gumtree.core.matchers.MappingStore;
import edu.handong.csee.isel.fcminer.gumtree.core.tree.ITree;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class AbstractMappingComparator implements Comparator<Mapping> {

    protected List<Mapping> ambiguousMappings;

    protected Map<Mapping, Double> similarities = new HashMap<>();

    protected int maxTreeSize;

    protected MappingStore mappings;

    public AbstractMappingComparator(List<Mapping> ambiguousMappings, MappingStore mappings, int maxTreeSize) {
        this.maxTreeSize = maxTreeSize;
        this.mappings = mappings;
        this.ambiguousMappings = ambiguousMappings;
    }

    @Override
    public int compare(Mapping m1, Mapping m2) {
        if (similarities.get(m2).compareTo(similarities.get(m1)) != 0) {
            return Double.compare(similarities.get(m2), similarities.get(m1));
        }
        if (m1.first.getId() != m2.first.getId()) {
            return Integer.compare(m1.first.getId(), m2.first.getId());
        }
        return Integer.compare(m1.second.getId(), m2.second.getId());
    }

    protected abstract double similarity(ITree src, ITree dst);

    protected double posInParentSimilarity(ITree src, ITree dst) {
        int posSrc = (src.isRoot()) ? 0 : src.getParent().getChildPosition(src);
        int posDst = (dst.isRoot()) ? 0 : dst.getParent().getChildPosition(dst);
        int maxSrcPos =  (src.isRoot()) ? 1 : src.getParent().getChildren().size();
        int maxDstPos =  (dst.isRoot()) ? 1 : dst.getParent().getChildren().size();
        int maxPosDiff = Math.max(maxSrcPos, maxDstPos);
        return 1D - ((double) Math.abs(posSrc - posDst) / (double) maxPosDiff);
    }

    protected double numberingSimilarity(ITree src, ITree dst) {
        return 1D - ((double) Math.abs(src.getId() - dst.getId())
                / (double) maxTreeSize);
    }

}
