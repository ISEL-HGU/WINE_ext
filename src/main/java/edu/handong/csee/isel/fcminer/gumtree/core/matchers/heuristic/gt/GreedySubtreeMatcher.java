package edu.handong.csee.isel.fcminer.gumtree.core.matchers.heuristic.gt;

import edu.handong.csee.isel.fcminer.gumtree.core.matchers.Mapping;
import edu.handong.csee.isel.fcminer.gumtree.core.matchers.MappingStore;
import edu.handong.csee.isel.fcminer.gumtree.core.matchers.MultiMappingStore;
import edu.handong.csee.isel.fcminer.gumtree.core.tree.ITree;

import java.util.*;

public class GreedySubtreeMatcher extends AbstractSubtreeMatcher {

    public GreedySubtreeMatcher(ITree src, ITree dst, MappingStore store) {
        super(src, dst, store);
    }

    @Override
    public void filterMappings(MultiMappingStore multiMappings) {
        // Select unique mappings first and extract ambiguous mappings.
        List<Mapping> ambiguousList = new ArrayList<>();
        Set<ITree> ignored = new HashSet<>();
        for (ITree src: multiMappings.getSrcs()) {
            if (multiMappings.isSrcUnique(src))
                addMappingRecursively(src, multiMappings.getDst(src).iterator().next());
            else if (!ignored.contains(src)) {
                Set<ITree> adsts = multiMappings.getDst(src);
                Set<ITree> asrcs = multiMappings.getSrc(multiMappings.getDst(src).iterator().next());
                for (ITree asrc : asrcs)
                    for (ITree adst: adsts)
                        ambiguousList.add(new Mapping(asrc, adst));
                ignored.addAll(asrcs);
            }
        }

        // Rank the mappings by score.
        Set<ITree> srcIgnored = new HashSet<>();
        Set<ITree> dstIgnored = new HashSet<>();
        Collections.sort(ambiguousList, new SiblingsMappingComparator(ambiguousList, mappings, getMaxTreeSize()));

        // Select the best ambiguous mappings
        retainBestMapping(ambiguousList, srcIgnored, dstIgnored);
    }

}