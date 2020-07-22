package edu.handong.csee.isel.fcminer.saresultminer.git;

public class Commit {
	String commitID = "";
	String commitDate = "";
	
	public Commit(String id, String date) {
		this.commitID = id;
		this.commitDate = date;
	}
	
	public String getID() {
		return commitID;
	}
	
	public String getTime() {
		return commitDate;
	}
}
