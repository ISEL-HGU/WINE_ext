package edu.handong.csee.isel.fcminer.gumtree.core.matchers;

import edu.handong.csee.isel.fcminer.gumtree.core.matchers.CompositeMatcher;
import edu.handong.csee.isel.fcminer.gumtree.core.matchers.MappingStore;
import edu.handong.csee.isel.fcminer.gumtree.core.matchers.Matcher;
import edu.handong.csee.isel.fcminer.gumtree.core.matchers.heuristic.cd.ChangeDistillerBottomUpMatcher;
import edu.handong.csee.isel.fcminer.gumtree.core.matchers.heuristic.cd.ChangeDistillerLeavesMatcher;
import edu.handong.csee.isel.fcminer.gumtree.core.matchers.heuristic.cd.ChangeDistillerParallelLeavesMatcher;
import edu.handong.csee.isel.fcminer.gumtree.core.matchers.heuristic.gt.GreedyBottomUpMatcher;
import edu.handong.csee.isel.fcminer.gumtree.core.matchers.heuristic.gt.GreedySubtreeMatcher;
import edu.handong.csee.isel.fcminer.gumtree.core.matchers.optimal.rted.RtedMatcher;
import edu.handong.csee.isel.fcminer.gumtree.core.matchers.optimizations.CrossMoveMatcherThetaF;
import edu.handong.csee.isel.fcminer.gumtree.core.matchers.optimizations.IdenticalSubtreeMatcherThetaA;
import edu.handong.csee.isel.fcminer.gumtree.core.matchers.optimizations.InnerNodesMatcherThetaD;
import edu.handong.csee.isel.fcminer.gumtree.core.matchers.optimizations.LcsOptMatcherThetaB;
import edu.handong.csee.isel.fcminer.gumtree.core.matchers.optimizations.LeafMoveMatcherThetaE;
import edu.handong.csee.isel.fcminer.gumtree.core.matchers.optimizations.UnmappedLeavesMatcherThetaC;
import edu.handong.csee.isel.fcminer.gumtree.core.tree.ITree;

import java.util.Arrays;
import java.util.HashSet;
import java.util.concurrent.ExecutorService;

public class OptimizedVersions {

    public static class CdabcdefSeq extends CompositeMatcher {

        /**
         * Instantiates the sequential ChangeDistiller version with Theta A-F.
         *
         * @param src the src
         * @param dst the dst
         * @param store the store
         */
        public CdabcdefSeq(ITree src, ITree dst, MappingStore store) {
            super(src, dst, store,
                    new Matcher[] { new IdenticalSubtreeMatcherThetaA(src, dst, store),
                            new ChangeDistillerLeavesMatcher(src, dst, store),
                            new ChangeDistillerBottomUpMatcher(src, dst, store),
                            new LcsOptMatcherThetaB(src, dst, store),
                            new UnmappedLeavesMatcherThetaC(src, dst, store),
                            new InnerNodesMatcherThetaD(src, dst, store),
                            new LeafMoveMatcherThetaE(src, dst, store),
                            new CrossMoveMatcherThetaF(src, dst, store) });
        }
    }

    public static class CdabcdefPar extends CompositeMatcher {

        /**
         * Instantiates the parallel ChangeDistiller version with Theta A-F.
         *
         * @param src the src
         * @param dst the dst
         * @param store the store
         */
        public CdabcdefPar(ITree src, ITree dst, MappingStore store) {
            super(src, dst, store,
                    new Matcher[] { new IdenticalSubtreeMatcherThetaA(src, dst, store),
                            new ChangeDistillerParallelLeavesMatcher(src, dst, store),
                            new ChangeDistillerBottomUpMatcher(src, dst, store),
                            new LcsOptMatcherThetaB(src, dst, store),
                            new UnmappedLeavesMatcherThetaC(src, dst, store),
                            new InnerNodesMatcherThetaD(src, dst, store),
                            new LeafMoveMatcherThetaE(src, dst, store),
                            new CrossMoveMatcherThetaF(src, dst, store)

                    });
        }
    }

    public static class Gtbcdef extends CompositeMatcher {

        /**
         * Instantiates GumTree with Theta B-F.
         *
         * @param src the src
         * @param dst the dst
         * @param store the store
         */
        public Gtbcdef(ITree src, ITree dst, MappingStore store) {
            super(src, dst, store,
                    new Matcher[] { new GreedySubtreeMatcher(src, dst, store),
                            new GreedyBottomUpMatcher(src, dst, store),
                            new LcsOptMatcherThetaB(src, dst, store),
                            new UnmappedLeavesMatcherThetaC(src, dst, store),
                            new InnerNodesMatcherThetaD(src, dst, store),
                            new LeafMoveMatcherThetaE(src, dst, store),
                            new CrossMoveMatcherThetaF(src, dst, store) });
        }
    }

    public static class Rtedacdef extends CompositeMatcher {

        /**
         * Instantiates RTED with Theta A-F.
         *
         * @param src the src
         * @param dst the dst
         * @param store the store
         */
        public Rtedacdef(ITree src, ITree dst, MappingStore store) {
            super(src, dst, store,
                    new Matcher[] { new IdenticalSubtreeMatcherThetaA(src, dst, store),
                            new RtedMatcher(src, dst, store),
                            new LcsOptMatcherThetaB(src, dst, store),
                            new UnmappedLeavesMatcherThetaC(src, dst, store),
                            new InnerNodesMatcherThetaD(src, dst, store),
                            new LeafMoveMatcherThetaE(src, dst, store),
                            new CrossMoveMatcherThetaF(src, dst, store)

                    });
        }
    }

}
