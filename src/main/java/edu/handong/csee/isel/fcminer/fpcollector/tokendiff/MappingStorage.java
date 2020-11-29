package edu.handong.csee.isel.fcminer.fpcollector.tokendiff;

import java.util.ArrayList;

import edu.handong.csee.isel.fcminer.gumtree.core.tree.ITree;

public class MappingStorage {
	ArrayList<Mapping> mappingStorageF = new ArrayList<>();
	ArrayList<Mapping> mappingStorageV = new ArrayList<>();
	ArrayList<Mapping> mappingStorageB = new ArrayList<>();
	
	public void add2MappingStorageF(ITree t1, ITree t2) {
		this.mappingStorageF.add(new Mapping(t1, t2));
	}

	public void add2MappingStorageV(ITree t1, ITree t2) {
		this.mappingStorageF.add(new Mapping(t1, t2));
	}
	
	public void add2MappingStorageB(ITree t1, ITree t2) {
		this.mappingStorageF.add(new Mapping(t1, t2));
	}
	
}
