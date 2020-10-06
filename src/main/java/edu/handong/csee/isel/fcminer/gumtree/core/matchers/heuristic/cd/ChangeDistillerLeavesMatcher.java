package edu.handong.csee.isel.fcminer.gumtree.core.matchers.heuristic.cd;

import edu.handong.csee.isel.fcminer.gumtree.core.matchers.Mapping;
import edu.handong.csee.isel.fcminer.gumtree.core.matchers.MappingStore;
import edu.handong.csee.isel.fcminer.gumtree.core.matchers.Matcher;
import edu.handong.csee.isel.fcminer.gumtree.core.tree.ITree;
import edu.handong.csee.isel.fcminer.gumtree.core.tree.TreeUtils;
import org.simmetrics.StringMetrics;

import java.util.*;

public class ChangeDistillerLeavesMatcher extends Matcher {

    public static final double LABEL_SIM_THRESHOLD = Double.parseDouble(System.getProperty("gt.cd.lsim", "0.5"));

    public ChangeDistillerLeavesMatcher(ITree src, ITree dst, MappingStore store) {
        super(src, dst, store);
    }

    @Override
    public void match() {
        List<Mapping> leavesMappings = new ArrayList<>();
        List<ITree> dstLeaves = retainLeaves(TreeUtils.postOrder(dst));
        for (Iterator<ITree> srcLeaves = TreeUtils.leafIterator(
                TreeUtils.postOrderIterator(src)); srcLeaves.hasNext();) {
            ITree srcLeaf = srcLeaves.next();
            for (ITree dstLeaf: dstLeaves) {
                if (isMappingAllowed(srcLeaf, dstLeaf)) {
                    double sim = StringMetrics.qGramsDistance().compare(srcLeaf.getLabel(), dstLeaf.getLabel());
                    if (sim > LABEL_SIM_THRESHOLD)
                        leavesMappings.add(new Mapping(srcLeaf, dstLeaf));
                }
            }
        }

        Set<ITree> ignoredSrcTrees = new HashSet<>();
        Set<ITree> ignoredDstTrees = new HashSet<>();
        Collections.sort(leavesMappings, new LeafMappingComparator());
        while (leavesMappings.size() > 0) {
            Mapping bestMapping = leavesMappings.remove(0);
            if (!(ignoredSrcTrees.contains(bestMapping.getFirst())
                    || ignoredDstTrees.contains(bestMapping.getSecond()))) {
                addMapping(bestMapping.getFirst(),bestMapping.getSecond());
                ignoredSrcTrees.add(bestMapping.getFirst());
                ignoredDstTrees.add(bestMapping.getSecond());
            }
        }
    }

    public List<ITree> retainLeaves(List<ITree> trees) {
        Iterator<ITree> treeIterator = trees.iterator();
        while (treeIterator.hasNext()) {
            ITree tree = treeIterator.next();
            if (!tree.isLeaf())
                treeIterator.remove();
        }
        return trees;
    }

    private static class LeafMappingComparator implements Comparator<Mapping> {

        @Override
        public int compare(Mapping m1, Mapping m2) {
            return Double.compare(sim(m1), sim(m2));
        }

        public double sim(Mapping m) {
            return StringMetrics.qGramsDistance().compare(m.getFirst().getLabel(), m.getSecond().getLabel());
        }

    }
}