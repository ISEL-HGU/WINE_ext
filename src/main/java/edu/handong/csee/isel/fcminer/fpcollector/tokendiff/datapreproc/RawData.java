package edu.handong.csee.isel.fcminer.fpcollector.tokendiff.datapreproc;

public class RawData {
	//Raw Data	
	private String path;
	private int start;
	private int end;
	private String vLine = "";
	private String src;
	
	public void clear() {
		path = null;
		vLine = null;
		src = null;		
	}
	
	public RawData(String path, String start, String end, String vLine){
		this.path = path;
		this.start = Integer.valueOf(start);
		this.end = Integer.valueOf(end);
		vLine = vLine.replaceAll("(?s)/\\*(.)*?\\*/", "");
		vLine = vLine.replaceAll("/\\*.*", "");
		this.vLine = this.vLine + vLine.split("//")[0].trim();
	}
	
	public int getStart() {
		return start;
	}

	public void setStart(String start) {		
		this.start = Integer.valueOf(start);
	}

	public int getEnd() {
		return end;
	}

	public void setEnd(String end) {
		this.end = Integer.valueOf(end);
	}
	
	public void addVLine(String vLine) {
		this.vLine = this.vLine + vLine.split("//")[0].trim();
	}
	
	public String getVLine() {
		return vLine;
	}
	
	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getSrc() {
		return src;
	}

	public void setSrc(String src) {
		this.src = src;
	}
}
