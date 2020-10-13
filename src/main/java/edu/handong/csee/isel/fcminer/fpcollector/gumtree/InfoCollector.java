package edu.handong.csee.isel.fcminer.fpcollector.gumtree;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Arrays;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.ResetCommand.ResetType;
import org.eclipse.jgit.api.errors.CheckoutConflictException;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.InvalidRefNameException;
import org.eclipse.jgit.api.errors.RefAlreadyExistsException;
import org.eclipse.jgit.api.errors.RefNotFoundException;

public class InfoCollector { 
	static final int VAR = 0;
	static final int FIELD = 1;
	static final int VARNODE = 2;
	static final int FIELDNODE = 3;
	static final int STRINGNODE = 4;
	
	public ArrayList<ASTNode> fpcPattern = new ArrayList<>();
	public ArrayList<ASTNode> tpcPattern = new ArrayList<>();
	ArrayList<Info> infos = new ArrayList<>();
	
	public void run(String resultPath, Git git, String projectName) throws IOException {
		System.out.println("INFO: Information Collecting is Started");
		Reader outputFile = new FileReader(resultPath);
		Iterable<CSVRecord> records = CSVFormat.EXCEL.parse(outputFile);		
		for (CSVRecord record : records) {			
			Info info = new Info();
			if(record.get(0).equals("Detection ID")) continue;
			if(!record.get(1).equals(projectName)) continue;
			int cnt = 0;
			String label = record.get(18);
			String VFCID = record.get(12);			
			String LDCID = record.get(9);
			String LDCLineNum = record.get(11);
			String VICID = record.get(6); 
			String VICLineNum = record.get(8);
			String filePath = record.get(5);
			info.path = filePath;			
			
			info = getStartEndLineNumber(git, info, label, VFCID, LDCID, LDCLineNum, VICID, VICLineNum, filePath);
			if(info == null) continue;
        	
        	infos.add(info);
		}
		System.out.println("INFO: Information Collecting is Finished");
	}
	
	public ArrayList<Info> getInfos(){
		return infos;
	}
	
	private String getSource(String file_path) {
		File f = new File(file_path);
		String source = "";
		try {
		BufferedReader br = new BufferedReader(new FileReader(f));
		StringBuilder builder = new StringBuilder(1000);
		char[] buf = new char[1024];
		int num = 0;
			while((num = br.read(buf)) != -1) {
				String readData = String.valueOf(buf, 0, num);
				builder.append(readData);
			}
		source = builder.toString();
		br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return source;
	}
	
	private String[] getSourceByLine(String source) {
		return source.split("\n");
	}
	
	private String getScope(String scope, int op, ArrayList<String> sourceByLine) {
		String[] scopeList = scope.split("-");
		if(scopeList.length == 1) {
			return getOneLineScope(Integer.parseInt(scopeList[0]), op, sourceByLine);
		}
		return scopeList[op];
	}
	
	private String getOneLineScope(int scope, int op, ArrayList<String> sourceByLine) {
		if (op == 0) {
			int i = scope - 2;
			while(!sourceByLine.get(i).contains(";")
					&& !sourceByLine.get(i).contains("//")
					&& !sourceByLine.get(i).contains("{")
					&& !sourceByLine.get(i).contains("}")
					) {
				i--;
			}
			return (i + 2) + "";
		} else {
			int i = scope - 1;
			while(!sourceByLine.get(i).contains(";")
					&& !sourceByLine.get(i).contains("//")
					&& !sourceByLine.get(i).contains("{")
					&& !sourceByLine.get(i).contains("}")) {
				i++;
			}
			return (i + 1) + "";
		}
	}
	
	private Info getStartEndLineNumber(Git git, Info info, String label, String VFCID, String LDCID, 
			String LDCLineNum, String VICID, String VICLineNum, String filePath) {
		if(label.equals("FPC") || label.equals("Unaffected Change")) {
			try {
				if(label.equals("FPC")) {
					if(LDCID.equals("")) {
						git.reset().setMode(ResetType.HARD).call();
						git.checkout().setForced(true).setName(VICID).call();
						info.sourceByLine = new ArrayList<>(Arrays.asList(getSourceByLine(getSource(info.path))));
						info.start = Integer.parseInt(getScope(VICLineNum, 0, info.sourceByLine));
						info.end = Integer.parseInt(getScope(VICLineNum, 1, info.sourceByLine));
						return info;
					}
					else {
						git.reset().setMode(ResetType.HARD).call();
						git.checkout().setForced(true).setName(LDCID).call();
						info.sourceByLine = new ArrayList<>(Arrays.asList(getSourceByLine(getSource(info.path))));
						info.start = Integer.parseInt(getScope(LDCLineNum, 0, info.sourceByLine));
						info.end = Integer.parseInt(getScope(LDCLineNum, 1, info.sourceByLine));
						return info;
					}
				} else {
					git.reset().setMode(ResetType.HARD).call();
					git.checkout().setForced(true).setName(VFCID).call();
					info.sourceByLine = new ArrayList<>(Arrays.asList(getSourceByLine(getSource(info.path))));
					info.start = Integer.parseInt(getScope(LDCLineNum, 0, info.sourceByLine));
					info.end = Integer.parseInt(getScope(LDCLineNum, 1, info.sourceByLine));
					return info;
				}
			} catch (GitAPIException e ) {
				e.printStackTrace();
			} 
		} else if(label.equals("Direct Fix")) {
			try {
				if(LDCID.equals("")) {
					git.reset().setMode(ResetType.HARD).call();
					git.checkout().setForced(true).setName(VICID).call();
					info.sourceByLine = new ArrayList<>(Arrays.asList(getSourceByLine(getSource(info.path))));
					info.start = Integer.parseInt(getScope(VICLineNum, 0, info.sourceByLine));
					info.end = Integer.parseInt(getScope(VICLineNum, 1, info.sourceByLine));
					return info;
				}
				else {
					git.reset().setMode(ResetType.HARD).call();
					git.checkout().setForced(true).setName(LDCID).call();
					info.sourceByLine = new ArrayList<>(Arrays.asList(getSourceByLine(getSource(info.path))));
					info.start = Integer.parseInt(getScope(LDCLineNum, 0, info.sourceByLine));
					info.end = Integer.parseInt(getScope(LDCLineNum, 1, info.sourceByLine));
					return info;
				}
			} catch (GitAPIException e) {
				e.printStackTrace();
			}
		} 
		return null;
	}
	
}