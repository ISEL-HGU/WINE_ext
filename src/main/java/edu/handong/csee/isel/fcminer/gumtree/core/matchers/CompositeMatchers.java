package edu.handong.csee.isel.fcminer.gumtree.core.matchers;

import edu.handong.csee.isel.fcminer.gumtree.core.gen.Registry;
import edu.handong.csee.isel.fcminer.gumtree.core.matchers.heuristic.cd.ChangeDistillerBottomUpMatcher;
import edu.handong.csee.isel.fcminer.gumtree.core.matchers.heuristic.cd.ChangeDistillerLeavesMatcher;
import edu.handong.csee.isel.fcminer.gumtree.core.matchers.heuristic.gt.CompleteBottomUpMatcher;
import edu.handong.csee.isel.fcminer.gumtree.core.matchers.heuristic.XyBottomUpMatcher;
import edu.handong.csee.isel.fcminer.gumtree.core.matchers.heuristic.gt.CliqueSubtreeMatcher;
import edu.handong.csee.isel.fcminer.gumtree.core.matchers.heuristic.gt.GreedyBottomUpMatcher;
import edu.handong.csee.isel.fcminer.gumtree.core.matchers.heuristic.gt.GreedySubtreeMatcher;
import edu.handong.csee.isel.fcminer.gumtree.core.tree.ITree;

public class CompositeMatchers {

    @Register(id = "gumtree", defaultMatcher = true, priority = Registry.Priority.HIGH)
    public static class ClassicGumtree extends CompositeMatcher {

        public ClassicGumtree(ITree src, ITree dst, MappingStore store) {
            super(src, dst, store, new Matcher[]{
                    new GreedySubtreeMatcher(src, dst, store),
                    new GreedyBottomUpMatcher(src, dst, store)
            });
        }
    }

    @Register(id = "gumtree-topdown", defaultMatcher = true, priority = Registry.Priority.HIGH)
    public static class GumtreeTopDown extends CompositeMatcher {

        public GumtreeTopDown(ITree src, ITree dst, MappingStore store) {
            super(src, dst, store, new Matcher[]{
                    new GreedySubtreeMatcher(src, dst, store)
            });
        }
    }

    @Register(id = "gumtree-complete")
    public static class CompleteGumtreeMatcher extends CompositeMatcher {

        public CompleteGumtreeMatcher(ITree src, ITree dst, MappingStore store) {
            super(src, dst, store, new Matcher[]{
                    new CliqueSubtreeMatcher(src, dst, store),
                    new CompleteBottomUpMatcher(src, dst, store)
            });
        }
    }

    @Register(id = "change-distiller")
    public static class ChangeDistiller extends CompositeMatcher {

        public ChangeDistiller(ITree src, ITree dst, MappingStore store) {
            super(src, dst, store, new Matcher[]{
                    new ChangeDistillerLeavesMatcher(src, dst, store),
                    new ChangeDistillerBottomUpMatcher(src, dst, store)
            });
        }
    }

    @Register(id = "xy")
    public static class XyMatcher extends CompositeMatcher {

        public XyMatcher(ITree src, ITree dst, MappingStore store) {
            super(src, dst, store, new Matcher[]{
                    new GreedySubtreeMatcher(src, dst, store),
                    new XyBottomUpMatcher(src, dst, store)
            });
        }
    }
}