package edu.handong.csee.isel.fcminer.saresultminer.sat.pmd;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import edu.handong.csee.isel.fcminer.util.Result;

public class Alarm {
	int detectionIDInResult = 0;
	String dir = "";
	String lineNum = "";
	String endNum = "";
	String code = "";
	String status = "";
	
	public Alarm() {
		
	}
	
	public Alarm(String alarm) {		
			dir = alarm.split(":")[0];
			dir = "." + dir.split("FPC_Miner")[1];
			lineNum = alarm.split(":")[1];			
			readFile(dir, lineNum);
	}
	
	public Alarm(Result result) {
		detectionIDInResult = result.getDetectionID();
		dir =result.getFilePath();
		if(!result.getLDCLineNum().equals("")) {
			lineNum = result.getLDCLineNum();
			status = "LDC";
		}
		else lineNum = result.getVICLineNum();
		
		if(!result.getVFCID().equals("")) {
			status = "VFC";
		}
		code = result.getOriginCode();
	}
	
	public Alarm(Alarm alarm) {
		this.detectionIDInResult = alarm.getDetectionIDInResult();
		this.dir = alarm.dir;
		this.lineNum = alarm.lineNum;
		this.code = alarm.code;
		this.status = alarm.status;
	}
	
	public Alarm(String path, String lineNum, String code) {
		dir = path;
		this.lineNum = lineNum;
		this.code = code;
	}

	public String getEndNum(){
		return endNum;
	}

	public String getStatus() {
		return status;
	}
	
	public void setStatus(String status) {
		this.status = status;
	}
	
	public int getDetectionIDInResult() {
		return detectionIDInResult;
	}
	
	public void setDetectionIDInResult(int id) {
		detectionIDInResult = id;
	}
	
	public String getDir() {
		return dir;
	}
	
	public void setDir(String dir) {
		this.dir = dir;
	}
	
	public String getLineNum() {
		return lineNum;
	}
	
	public String getCode() {
		return code;
	}
	
	public void setCode(String code) {
		this.code = code;
	}
	
	public void setLineNum(String num) {
		this.lineNum = num;
	}
	
	private void readFile(String dir, String lineNum) {
		File f = new File(dir);
		try {
			FileReader fReader =new FileReader(f);
			BufferedReader fBufReader = new BufferedReader(fReader);
			String str = "";
			int num = 1;
			while((str = fBufReader.readLine()) != null) {				
				if(num == Integer.parseInt(lineNum)) {
					code = str;
					break;
				}
				num++;
			}
			fBufReader.close();
		} 
		catch (IOException e) {
				e.printStackTrace();
		}		
	}
}
