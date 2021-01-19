package edu.handong.csee.isel.fcminer.fpcollector.concretecodepattern;

import java.util.ArrayList;

public class CodePatternSet {
	private String code;
	private ArrayList<Integer> patterns = new ArrayList<>();
	
	public CodePatternSet(String code, Integer hashedPattern) {
		this.code = code;
		patterns.add(hashedPattern);
	}
	
	public void addPattern(Integer hasedPattern) {
		patterns.add(hasedPattern);
	}
	
	public String getCode() {
		return code;
	}
	
	public ArrayList<Integer> getPatterns() {
		return patterns;
	}
}
