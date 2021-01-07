package edu.handong.csee.isel.fcminer.fpcollector.tokendiff.datapreproc;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jgit.api.Git;

import edu.handong.csee.isel.fcminer.util.OSValidator;

public class InfoCollector { 
	static final int VAR = 0;
	static final int FIELD = 1;
	static final int VARNODE = 2;
	static final int FIELDNODE = 3;
	static final int STRINGNODE = 4;
	
	public ArrayList<ASTNode> fpcPattern = new ArrayList<>();
	public ArrayList<ASTNode> tpcPattern = new ArrayList<>();
	ArrayList<Info> infos = new ArrayList<>();
	
	public void clear() {
		fpcPattern = null;
		tpcPattern = null;
		infos = null;
	}
	
	public void run(String resultPath, Git git, String projectName) {		
		try {
			Reader outputFile = new FileReader(resultPath);
			
			Iterable<CSVRecord> records = CSVFormat.EXCEL.parse(outputFile);
			
			for (CSVRecord record : records) {						
				if(record.get(0).equals("Detection ID")) continue;
				if(!record.get(1).equals(projectName)) continue;
				
				Info info = new Info();			
	
				String filePath = record.get(-1);
				String newFilePath = modifyFilePathToOS(filePath);	
				
				info.setPath(newFilePath);
				info.setStart(record.get(-1));
				info.setEnd(record.get(-1));
				info = getSrcFromPath(newFilePath, info);				
	        	
				infos.add(info);
			}	
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	private Info getSrcFromPath(String path, Info info) {
		String src = "";
		
		File f = new File(path);
		try {
			FileReader fReader =new FileReader(f);
			BufferedReader fBufReader = new BufferedReader(fReader);
			String str = "";
			int cnt = 0;
			while((str = fBufReader.readLine()) != null) {
				cnt++;
				src += str;
				if(info.getStart() <= cnt && info.getEnd() <= cnt) {
					info.addVLine(str);
				}
			}
			fBufReader.close();
		} 
		catch (IOException e) {
				e.printStackTrace();
		}
		info.setSrc(src);
		
		
		return info;
	}

	private String modifyFilePathToOS(String filePath) {
		String osName = OSValidator.getOS();
		String newFilePath = "";
		
		if(filePath.contains("\\") && osName.equals("linux")) {
			for(int i = 0; i < filePath.length(); i++) {
				if(filePath.charAt(i) == '\\'){
					newFilePath += '/';
				} else {
					newFilePath += filePath.charAt(i);
				}
			}
			filePath = "" + newFilePath;
		} else if(filePath.contains("/") && osName.equals("window")) {
			for(int i = 0; i < filePath.length(); i++) {
				if(filePath.charAt(i) == '/'){
					newFilePath += '\\';
				} else {
					newFilePath += filePath.charAt(i);
				}
			}
		}
		
		return newFilePath;
	}

	public ArrayList<Info> getInfos(){
		return infos;
	}
	
}