package edu.handong.csee.isel.fcminer.fpcollector.tokendiff;

import edu.handong.csee.isel.fcminer.gumtree.core.tree.ITree;

public class Mapping {
	Pair<ITree, ITree> mapping;
	
	public Mapping(ITree t1, ITree t2) {
		this.mapping = new Pair<ITree, ITree>(t1, t2);
	}

}
