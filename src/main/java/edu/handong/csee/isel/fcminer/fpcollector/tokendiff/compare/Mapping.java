package edu.handong.csee.isel.fcminer.fpcollector.tokendiff.compare;

import edu.handong.csee.isel.fcminer.fpcollector.tokendiff.ast.ITree;

public class Mapping {
	Pair<ITree, ITree> mapping;
	
	public Mapping(ITree t1, ITree t2) {
		this.mapping = new Pair<ITree, ITree>(t1, t2);
	}

}
