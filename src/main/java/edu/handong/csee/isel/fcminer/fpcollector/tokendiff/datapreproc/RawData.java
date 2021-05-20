package edu.handong.csee.isel.fcminer.fpcollector.tokendiff.datapreproc;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;

public class RawData {
	//Raw Data	
	private String path;
	private int start;
	private int end;
	private int vLineNum;
	private String vLine = "";
	private String src;
	
	public void clear() {
		path = null;
		vLine = null;
		src = null;		
	}
	
	public RawData(String path, String start, String end, String vLine){
		this.path = path;
		this.vLineNum = Integer.parseInt(start);
		readSrc();		
		setStartEnd(start, end);
		vLine = vLine.replaceAll("(?s)/\\*(.)*?\\*/", "");
		vLine = vLine.replaceAll("/\\*.*", "");
		
		this.vLine = this.vLine + vLine.split("//")[0].trim();
	}
	
	private void readSrc() {						
		StringBuilder builder = new StringBuilder();
		
		try {
			FileInputStream fs = new FileInputStream(this.path);			
			BufferedReader reader = new BufferedReader(new InputStreamReader(fs));
			
			char[] buf = new char[8192];
			int read;
					
			while((read = reader.read(buf, 0, buf.length)) > 0) {				
				builder.append(buf, 0, read);
			}
			reader.close();
		}
		
		catch (IOException e) {
			e.printStackTrace();
		}
		this.src = builder.toString();
		builder = null;
	}
	
	private void setStartEnd(String start, String end) {
		// -1 due to the index of ArrayList
		int tmpStart = Integer.parseInt(start) - 1;
		int tmpEnd = Integer.parseInt(end) - 1;
		
		ArrayList<String> splitByNewLine = new ArrayList<>(Arrays.asList(src.split("\n")));
				
		String tmpCode = splitByNewLine.get(tmpStart).trim();		
		char lastIdx = tmpCode.charAt(tmpCode.length()-1);		
		
		//if detected line has a terminate character
		if(lastIdx == ';' || lastIdx == '}') {
			//check forward code-line
			do {
				tmpStart --;
				tmpCode = splitByNewLine.get(tmpStart).trim();
				if(tmpCode.length() > 0)
					lastIdx = tmpCode.charAt(tmpCode.length()-1);
			} while(lastIdx != ';' && lastIdx != '}' && !tmpCode.endsWith("*/") && !tmpCode.startsWith("//"));
			
			//if previous line has a terminate character --> current line is enough to analyze
			if(tmpStart == Integer.parseInt(start) - 2) {
				this.start = Integer.parseInt(start);
				this.end = Integer.parseInt(end);
			}
			//if previous line doesn't have a terminate character --> current line is continuous from previous line
			else {
				// +1 because index of ArrayList 
				this.start = tmpStart + 1;
				this.end = Integer.parseInt(end);
			}			
		} 
		//if detected line doesn't have a terminate character
		else {
			//check backward code-line
			do {
				tmpEnd ++;
				tmpCode = splitByNewLine.get(tmpEnd).trim();
				if(tmpCode.length() > 0)
					lastIdx = tmpCode.charAt(tmpCode.length()-1);
			} while(lastIdx != ';' && lastIdx != '}' && !tmpCode.endsWith("*/") && !tmpCode.startsWith("//"));
			
			//if backward line has a terminate character --> the statement is terminated in tmpEnd line 
			this.end = tmpEnd + 1;
			this.start = Integer.parseInt(start);
		}
		
//		System.out.println(splitByNewLine.get(this.start -1));
//		System.out.println(splitByNewLine.get(this.end-1));
		System.out.println("");
	}

	public int getVLineNum(){
		return vLineNum;
	}

	public int getStart() {
		return start;
	}

	public void setStart(String start) {		
		this.start = Integer.valueOf(start);
	}

	public void setStart(int start) {
		this.start = start;
	}

	public int getEnd() {
		return end;
	}

	public void setEnd(String end) {
		this.end = Integer.valueOf(end);
	}

	public void setEnd(int end) {
		this.end = end;
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
