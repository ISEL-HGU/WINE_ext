package edu.handong.csee.isel.fcminer.gumtree.core.matchers.optimal.rted;

import edu.handong.csee.isel.fcminer.gumtree.core.matchers.MappingStore;
import edu.handong.csee.isel.fcminer.gumtree.core.matchers.Matcher;
import edu.handong.csee.isel.fcminer.gumtree.core.tree.ITree;
import edu.handong.csee.isel.fcminer.gumtree.core.tree.TreeUtils;

import java.util.ArrayDeque;
import java.util.List;

public class RtedMatcher extends Matcher {

    public RtedMatcher(ITree src, ITree dst, MappingStore store) {
        super(src, dst, store);
    }

    @Override
    public void match() {
        RtedAlgorithm a = new RtedAlgorithm(1D, 1D, 1D);
        a.init(src, dst);
        a.computeOptimalStrategy();
        a.nonNormalizedTreeDist();
        ArrayDeque<int[]> arrayMappings = a.computeEditMapping();
        List<ITree> srcs = TreeUtils.postOrder(src);
        List<ITree> dsts = TreeUtils.postOrder(dst);
        for (int[] m: arrayMappings) {
            if (m[0] != 0 && m[1] != 0) {
                ITree src = srcs.get(m[0] - 1);
                ITree dst = dsts.get(m[1] - 1);
                if (isMappingAllowed(src, dst))
                    addMapping(src, dst);
            }
        }
    }
}
