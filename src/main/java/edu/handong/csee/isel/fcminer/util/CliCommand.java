package edu.handong.csee.isel.fcminer.util;

import edu.handong.csee.isel.fcminer.util.CliOptions.RunState;

public class CliCommand {
	private String rule = "";
	private String addressPath = "";
	private String outputPath = "";
	private String pmd = "";
	private boolean semgrep = false;
	private RunState state;
	
	public void setRule(String rule) {
		this.rule = rule;
	}
	
	public String getRule() {
		return rule;
	}
	
	public void setAddressPath(String addressPath) {
		this.addressPath = addressPath;
	}
	
	public String getAddressPath() {
		return addressPath;
	}
	
	public void setOutputPath(String outputPath) {
		this.outputPath = outputPath;
	}
	
	public String getOutputPath() {
		return outputPath;
	}
	
	public void setPMD(String pmd) {
		this.pmd = pmd;
	}
	
	public String getPMD() {
		return pmd;
	}
	
	public void setState(RunState state) {
		this.state = state;
	}
	
	public RunState getState() {
		return state;
	}

	public void setSemgrep(boolean semgrep){
		this.semgrep = semgrep;
	}

	public boolean getSemgrep(){
		return semgrep;
	}
}
