package edu.handong.csee.isel.fcminer.saresultminer.sat.pmd;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import edu.handong.csee.isel.fcminer.util.Result;

public class Alarm {
	int detectionIDInResult = 0;
	String dir = "";
	String startNum = "";
	String endNum = "";
	String code = "";
	String status = "";
	String rule = "";
	
	public Alarm() {
		
	}
	
	public Alarm(String alarm) {		
			dir = alarm.split(":")[0];
			dir = "./TargetProjects" + dir.split("TargetProjects")[1];
			startNum = alarm.split(":")[1];
			readFile(dir, startNum);
	}
	
	public Alarm(Result result) {
		detectionIDInResult = result.getDetectionID();
		dir =result.getFilePath();
		if(!result.getLDCLineNum().equals("")) {
			startNum = result.getLDCLineNum();
			status = "LDC";
		}
		else startNum = result.getVICLineNum();
		
		if(!result.getVFCID().equals("")) {
			status = "VFC";
		}
		code = result.getOriginCode();
	}
	
	public Alarm(Alarm alarm) {
		this.detectionIDInResult = alarm.getDetectionIDInResult();
		this.dir = alarm.dir;
		this.startNum = alarm.startNum;
		this.code = alarm.code;
		this.status = alarm.status;
	}
	
	public Alarm(String path, String lineNum, String code) {
		dir = path;
		this.startNum = lineNum;
		this.code = code;
	}

	public Alarm(String path, long sNum, long eNum, String rule){
		dir = path;
		this.startNum = "" + sNum;
		this.endNum = "" + eNum;
		this.rule = rule;
		readMultiLines(dir, startNum, endNum);
	}

	public Alarm(String path, String sNum, String eNum, String rule, String code){
		dir = path;
		startNum = sNum;
		endNum = eNum;
		this.rule = rule;
		this.code = code;
	}

	public String getEndNum(){
		return endNum;
	}

	public String getRule(){
		return rule;
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
	
	public String getStartLineNum() {
		return startNum;
	}
	
	public String getCode() {
		return code;
	}
	
	public void setCode(String code) {
		this.code = code;
	}
	
	public void setLineNum(String num) {
		this.startNum = num;
	}

	private void readFile(String dir, String startNum) {
		File f = new File(dir);
		try {
			FileReader fReader =new FileReader(f);
			BufferedReader fBufReader = new BufferedReader(fReader);
			String str = "";
			int num = 1;
			while((str = fBufReader.readLine()) != null) {
				if(Integer.parseInt(startNum) == num) {
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

	private void readMultiLines(String dir, String startNum, String endNum) {
		File f = new File(dir);
		try {
			FileReader fReader =new FileReader(f);
			BufferedReader fBufReader = new BufferedReader(fReader);
			String str = "";
			int num = 1;
			while((str = fBufReader.readLine()) != null) {				
				if(Integer.parseInt(startNum) <= num && num <= Integer.parseInt(endNum)) {
					code += str + "\n";
				}
				else if(num > Integer.parseInt(endNum)){
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
