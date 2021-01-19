package edu.handong.csee.isel.fcminer.fpcollector.concretecodepattern;

public class CodePatternPair {
	private Integer hashedPattern = -1;
	private String code;
	
	public CodePatternPair(Integer hash, String code) {
		this.hashedPattern = hash;
		this.code = code;
	}
	
	public Integer getHashedPattern() {
		return hashedPattern;
	}
	public void setHashedPattern(Integer hash) {
		this.hashedPattern = hash;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	
	
}
