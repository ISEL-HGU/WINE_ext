package edu.handong.csee.isel.fcminer.gumtree.core.matchers.heuristic.cd;

import edu.handong.csee.isel.fcminer.gumtree.core.matchers.MappingStore;
import edu.handong.csee.isel.fcminer.gumtree.core.matchers.Matcher;
import edu.handong.csee.isel.fcminer.gumtree.core.tree.ITree;
import edu.handong.csee.isel.fcminer.gumtree.core.tree.TreeUtils;

import java.util.List;

public class ChangeDistillerBottomUpMatcher extends Matcher {

    public static final double STRUCT_SIM_THRESHOLD_1 =  Double.parseDouble(System.getProperty("gt.cd.ssim1", "0.6"));

    public static final double STRUCT_SIM_THRESHOLD_2 = Double.parseDouble(System.getProperty("gt.cd.ssim2", "0.4"));

    public static final int MAX_NUMBER_OF_LEAVES = Integer.parseInt(System.getProperty("gt.cd.ml", "4"));

    public ChangeDistillerBottomUpMatcher(ITree src, ITree dst, MappingStore store) {
        super(src, dst, store);
    }

    @Override
    public void match() {
        List<ITree> dstTrees = TreeUtils.postOrder(this.dst);
        for (ITree currentSrcTree: this.src.postOrder()) {
            int numberOfLeaves = numberOfLeaves(currentSrcTree);
            for (ITree currentDstTree: dstTrees) {
                if (isMappingAllowed(currentSrcTree, currentDstTree)
                        && !(currentSrcTree.isLeaf() || currentDstTree.isLeaf())) {
                    double similarity = chawatheSimilarity(currentSrcTree, currentDstTree);
                    if ((numberOfLeaves > MAX_NUMBER_OF_LEAVES && similarity >= STRUCT_SIM_THRESHOLD_1)
                            || (numberOfLeaves <= MAX_NUMBER_OF_LEAVES && similarity >= STRUCT_SIM_THRESHOLD_2)) {
                        addMapping(currentSrcTree, currentDstTree);
                        break;
                    }
                }
            }
        }
    }

    private int numberOfLeaves(ITree root) {
        int numberOfLeaves = 0;
        for (ITree tree : root.getDescendants())
            if (tree.isLeaf())
                numberOfLeaves++;
        return numberOfLeaves;
    }
}
