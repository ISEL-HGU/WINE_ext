package edu.handong.csee.isel.fcminer.gumtree.core.matchers.heuristic;

import edu.handong.csee.isel.fcminer.gumtree.core.utils.StringAlgorithms;
import edu.handong.csee.isel.fcminer.gumtree.core.matchers.MappingStore;
import edu.handong.csee.isel.fcminer.gumtree.core.matchers.Matcher;
import edu.handong.csee.isel.fcminer.gumtree.core.matchers.Register;
import edu.handong.csee.isel.fcminer.gumtree.core.tree.ITree;
import edu.handong.csee.isel.fcminer.gumtree.core.tree.TreeUtils;

import java.util.List;

@Register(id = "lcs")
public class LcsMatcher extends Matcher {

    public LcsMatcher(ITree src, ITree dst, MappingStore store) {
        super(src, dst, store);
    }

    @Override
    public void match() {
        List<ITree> srcSeq = TreeUtils.preOrder(src);
        List<ITree> dstSeq = TreeUtils.preOrder(dst);
        List<int[]> lcs = StringAlgorithms.lcss(srcSeq, dstSeq);
        System.out.println(lcs.size());
        for (int[] x: lcs) {

            ITree t1 = srcSeq.get(x[0]);
            ITree t2 = dstSeq.get(x[1]);
            addMapping(t1, t2);
        }
    }
}
