package edu.handong.csee.isel.fcminer.gumtree.core.matchers;

import edu.handong.csee.isel.fcminer.gumtree.core.matchers.MappingStore;
import edu.handong.csee.isel.fcminer.gumtree.core.matchers.Matcher;
import edu.handong.csee.isel.fcminer.gumtree.core.tree.ITree;

public class CompositeMatcher extends Matcher {

    protected final Matcher[] matchers;

    public CompositeMatcher(ITree src, ITree dst, MappingStore store, Matcher[] matchers) {
        super(src, dst, store);
        this.matchers = matchers;
    }

    @Override
    public void match() {
        for (Matcher matcher : matchers) {
            matcher.match();
        }
    }

}
