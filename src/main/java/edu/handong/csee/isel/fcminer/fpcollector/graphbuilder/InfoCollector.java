package edu.handong.csee.isel.fcminer.fpcollector.graphbuilder;

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
import org.eclipse.jgit.api.Git;
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

	public ArrayList<ControlNode> graphs = new ArrayList<>();
	
	public void run(String resultPath, Git git) throws IOException {		
		Reader outputFile = new FileReader(resultPath);
		Iterable<CSVRecord> records = CSVFormat.EXCEL.parse(outputFile);		
		for (CSVRecord record : records) {			
			if(record.get(0).equals("Detection ID")) continue;		
			String label = record.get(17);
			String VFCID = record.get(11);			
			String LDCID = record.get(8);
			String LDCLineNum = record.get(10);
			String VICID = record.get(5);
			String VICLineNum = record.get(7);
			String filePath = record.get(4);
			
			Info info = new Info();			
			info.path = filePath;					
			if(label.equals("Maintained")) {
				try {
					if(LDCID.equals("")) {
						git.checkout().setForced(true).setName(VICID).call();
						info.start = Integer.parseInt(getScope(VICLineNum, 0, info.sourceByLine));
						info.end = Integer.parseInt(getScope(VICLineNum, 1, info.sourceByLine));
					}
					else {
						git.checkout().setForced(true).setName(LDCID).call();
						info.start = Integer.parseInt(getScope(LDCLineNum, 0, info.sourceByLine));
						info.end = Integer.parseInt(getScope(LDCLineNum, 1, info.sourceByLine));
					}					
				} catch (RefAlreadyExistsException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (RefNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InvalidRefNameException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (CheckoutConflictException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (GitAPIException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			
		    
		    info.sourceByLine = new ArrayList<>(Arrays.asList(getSourceByLine(getSource(info.path))));		    		   
		    //In case of String Literal
		    info.varNames.add(getVarNameByDoubleQuotation(record.get(2)));
		    //For String Literal
        	if(info.varNames.get(0) != null && info.varNodes.size() == 0) {
        		info.varNodes.addAll(getStringNode(info));
        	} 
		    //In case of DFA
        	else if(info.varNames.get(0) == null) {
		    	info.varNames.remove(0);
		    	info.varNames.add(getVarNameBySingleQuotation(record.get(2)));
		    	if(info.varNames.get(0) != null) {
		    		info.varNodes.addAll(getVarList(info));
		    		if(info.varNodes.size() == 0) {
		    			info.fieldNames.addAll(info.varNames);
		    			info.fieldNodes.addAll(getFieldList(info));
		    		}
		    	}
		    	else {
		    		//for other rules
		    		info.varNames.remove(0);
	        		info.varNodes.addAll(getVarList(info));
	            	info.fieldNodes.addAll(getFieldList(info));
	            	info.nodesToStrings();
		    	}
		    }
		    
        	if(info.varNodes.size() == 0 && info.fieldNodes.size() == 0) {
//        		System.out.println("Something Goes Wrong");
        	}
		    
		    GraphBuilder graphBuilder = new GraphBuilder(info);
			graphBuilder.run();
			graphs.add(graphBuilder.root);
		}
	}	
	
	private String getSource(String file_path) throws IOException {
		File f = new File(file_path);
		BufferedReader br = new BufferedReader(new FileReader(f));
		StringBuilder builder = new StringBuilder(1000);
	
		char[] buf = new char[1024];
		String source;
		int num = 0;
		while((num = br.read(buf)) != -1) {
			String readData = String.valueOf(buf, 0, num);
			builder.append(readData);
		}
		source = builder.toString();
		br.close();

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
	
	private String getVarNameByDoubleQuotation(String token) {		
		int start = 0;
		int end = 0;
		int flag = 0;
		for(int i = 0 ; i < token.length(); i ++) {
			if(flag == 0 && i< token.length()-1 && token.charAt(i)=='"') {
				if(token.charAt(i-1) != '\\') {					
					start = i+1;
					flag = 1;
				}
			} else if(flag == 1 && i< token.length()-1 && token.charAt(i)=='"') {
				if(token.charAt(i-1) != '\\') {
					end = i;
					flag = 2;
				}
			}
		}
		
		if(start == 0 && end == 0) {
			return null;
		}
		
		String newToken =token.substring(start, end);	
		return newToken;
	}
	
	private String getVarNameBySingleQuotation(String token) {		
		String[] tokenList = token.split("'");
		if(tokenList.length == 1) {
			return null;
		}
		return tokenList[3];
	}
	
	private ArrayList<ASTNode> getStringNode(Info info){
		GraphBuilder tempParser = new GraphBuilder(info);
		return tempParser.getViolatedVariableList(String.join("\n", info.sourceByLine), STRINGNODE);
	}
	
	private ArrayList<ASTNode> getVarList(Info info){
		GraphBuilder tempParser = new GraphBuilder(info);
		return tempParser.getViolatedVariableList(String.join("\n", info.sourceByLine), VAR);
	}
	
	private ArrayList<ASTNode> getFieldList(Info info){
		GraphBuilder tempParser = new GraphBuilder(info);
		return tempParser.getViolatedVariableList(String.join("\n", info.sourceByLine), FIELD);
	}
}