package edu.handong.csee.isel.fcminer.fpcollector.tokendiff.pattern;

import java.util.ArrayList;

import edu.handong.csee.isel.fcminer.fpcollector.tokendiff.datapreproc.Info;

public class Patterns {
	ArrayList<Pattern> patterns = new ArrayList<>();
	ArrayList<Info> variables = new ArrayList<>();
	Info fixed;
	
	public void setFixed(Info info) {
		this.fixed = info;
	}
	
	public void addVariables(Info info) {
		this.variables.add(info);
	}
	
	public ArrayList<Info> getVariables() {
		return variables;
	}
	
	public void addPattern(Pattern pattern) {
		patterns.add(pattern);
	}
	
	public Info getFixed() {
		return fixed;
	}
	
	public ArrayList<Pattern> getPatterns(){
		return patterns;
	}
}
