package edu.handong.csee.isel.fcminer.fpcollector.concretecodepattern;

import java.util.ArrayList;

public class CodePatternSet {
	private String code;
	private ArrayList<HashedPattern> patterns = new ArrayList<>();
	private int lowFrequency = 999999999;
	
	public CodePatternSet(String code, HashedPattern hashedPattern) {
		this.code = code;
		patterns.add(hashedPattern);
	}
	
	public void addPattern(HashedPattern hasedPattern) {
		patterns.add(hasedPattern);
	}
	
	public String getCode() {
		return code;
	}
	
	public ArrayList<HashedPattern> getPatterns() {
		return patterns;
	}
	
	public void findLowFrequency() {
		for(HashedPattern hp : patterns) {
			if(hp.getFrequency() < lowFrequency)
				lowFrequency = hp.getFrequency();
		}
	}
	
	public int getLowFrequency() {
		return lowFrequency;
	}
	
	public ArrayList<Integer> getOnlyPatternHash() {
		ArrayList<Integer> ps = new ArrayList<>();
		for(HashedPattern hp : patterns) {
			ps.add(hp.getHash());
		}
		
		return ps;
	}
}
