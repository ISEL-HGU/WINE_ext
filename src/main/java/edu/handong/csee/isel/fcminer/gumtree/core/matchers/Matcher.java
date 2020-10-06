package edu.handong.csee.isel.fcminer.gumtree.core.matchers;

import edu.handong.csee.isel.fcminer.gumtree.core.tree.ITree;
import org.atteo.classindex.IndexSubclasses;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

@IndexSubclasses
public abstract class Matcher {

    public static final Logger LOGGER = Logger.getLogger("edu.handong.csee.isel.fcminer.gumtree.core.matchers");

    protected final ITree src;

    protected final ITree dst;

    protected final MappingStore mappings;

    public Matcher(ITree src, ITree dst, MappingStore mappings) {
        this.src = src;
        this.dst = dst;
        this.mappings = mappings;
    }

    public abstract void match();

    public MappingStore getMappings() {
        return mappings;
    }

    public Set<Mapping> getMappingsAsSet() {
        return mappings.asSet();
    }

    public ITree getSrc() {
        return src;
    }

    public ITree getDst() {
        return dst;
    }

    protected void addMapping(ITree src, ITree dst) {
        mappings.link(src, dst);
    }

    protected void addMappingRecursively(ITree src, ITree dst) {
        List<ITree> srcTrees = src.getTrees();
        List<ITree> dstTrees = dst.getTrees();
        for (int i = 0; i < srcTrees.size(); i++) {
            ITree currentSrcTree = srcTrees.get(i);
            ITree currentDstTree = dstTrees.get(i);
            addMapping(currentSrcTree, currentDstTree);
        }
    }

    protected double chawatheSimilarity(ITree src, ITree dst) {
        int max = Math.max(src.getDescendants().size(), dst.getDescendants().size());
        return (double) numberOfCommonDescendants(src, dst) / (double) max;
    }

    protected double diceSimilarity(ITree src, ITree dst) {
        double c = (double) numberOfCommonDescendants(src, dst);
        return (2D * c) / ((double) src.getDescendants().size() + (double) dst.getDescendants().size());
    }

    protected double jaccardSimilarity(ITree src, ITree dst) {
        double num = (double) numberOfCommonDescendants(src, dst);
        double den = (double) src.getDescendants().size() + (double) dst.getDescendants().size() - num;
        return num / den;
    }

    protected int numberOfCommonDescendants(ITree src, ITree dst) {
        Set<ITree> dstDescendants = new HashSet<>(dst.getDescendants());
        int common = 0;

        for (ITree t : src.getDescendants()) {
            ITree m = mappings.getDst(t);
            if (m != null && dstDescendants.contains(m))
                common++;
        }

        return common;
    }

    public boolean isMappingAllowed(ITree src, ITree dst) {
        return src.hasSameType(dst) && !(mappings.hasSrc(src) || mappings.hasDst(dst));
    }
}
