package edu.handong.csee.isel.fcminer.fpcollector.concretecodepattern;

public class HashedPattern {
	private Integer hash = -1;
	private Integer frequency = -1;
	
	public HashedPattern(Integer hash, Integer frequency) {
		this.hash = hash;
		this.frequency = frequency;
	}
	
	public Integer getHash() {
		return hash;
	}
	
	public void setHash(Integer hash) {
		this.hash = hash;
	}
	
	public Integer getFrequency() {
		return frequency;
	}
	
	public void setFrequency(Integer frequency) {
		this.frequency = frequency;
	}
	
	
}
