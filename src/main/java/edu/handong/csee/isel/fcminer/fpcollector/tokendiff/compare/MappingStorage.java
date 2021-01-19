package edu.handong.csee.isel.fcminer.fpcollector.tokendiff.compare;

import java.util.ArrayList;

import edu.handong.csee.isel.fcminer.fpcollector.tokendiff.datapreproc.CompareData;

public class MappingStorage {
	ArrayList<Mapping> mappingStorageF = new ArrayList<>();
	
	ArrayList<Mapping> mappingStorageV = new ArrayList<>();
	
	ArrayList<Mapping> mappingStorageB = new ArrayList<>();
	
	int hash = -1;
	
	Pair<String, String> vLineCodePair;
	
	Pair<String, String> vNodeCodePair;
	
	public void setHash(int hash) {
		this.hash = hash;	
	}
	
	public int getHash() {
		return hash;
	}
	
	public void setVLineCodes(String code1, String code2) {
		this.vLineCodePair = new Pair<String, String>(code1, code2);
	}
	
	public Pair<String, String> getVLineCodes() {
		return vLineCodePair;
	}
	
	public void setVNodeCodes(String code1, String code2) {
		this.vLineCodePair = new Pair<String, String>(code1, code2);
	}
	
	public Pair<String, String> getVNodeCodes() {
		return vLineCodePair;
	}
	
	public void add2MappingStorageF(CompareData t1, CompareData t2) {
		this.mappingStorageF.add(new Mapping(t1, t2));
	}

	public void add2MappingStorageV(Mapping mappingV) {
		this.mappingStorageV.add(mappingV);
	}
	
	public void add2MappingStorageB(CompareData t1, CompareData t2) {
		this.mappingStorageB.add(new Mapping(t1, t2));
	}
	
	public ArrayList<Mapping> getMappingStorageV() {
		return mappingStorageV;
	}
	
}
