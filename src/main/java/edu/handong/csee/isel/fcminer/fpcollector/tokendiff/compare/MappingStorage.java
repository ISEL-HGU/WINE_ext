package edu.handong.csee.isel.fcminer.fpcollector.tokendiff.compare;

import java.util.ArrayList;

import edu.handong.csee.isel.fcminer.fpcollector.tokendiff.ast.ITree;

public class MappingStorage {
	ArrayList<Mapping> mappingStorageF = new ArrayList<>();
	ArrayList<Mapping> mappingStorageV = new ArrayList<>();
	ArrayList<Mapping> mappingStorageB = new ArrayList<>();
	int hash = -1;
	
	public void setHash(int hash) {
		this.hash = hash;	
	}
	
	public int getHash() {
		return hash;
	}
	
	public void add2MappingStorageF(ITree t1, ITree t2) {
		this.mappingStorageF.add(new Mapping(t1, t2));
	}

	public void add2MappingStorageV(ArrayList<Mapping> storageV) {
		this.mappingStorageV.addAll(storageV);
	}
	
	public void add2MappingStorageB(ITree t1, ITree t2) {
		this.mappingStorageB.add(new Mapping(t1, t2));
	}
	
	public ArrayList<Mapping> getMappingStorageV() {
		return mappingStorageV;
	}
	
}
