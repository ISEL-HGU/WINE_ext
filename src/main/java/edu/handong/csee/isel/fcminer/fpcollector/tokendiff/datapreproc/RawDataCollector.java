package edu.handong.csee.isel.fcminer.fpcollector.tokendiff.datapreproc;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;

import edu.handong.csee.isel.fcminer.fpcollector.tokendiff.ast.ITree;
import edu.handong.csee.isel.fcminer.util.OSValidator;

public class RawDataCollector { 	
	int numOfAlarms = 0;
	
	ArrayList<CompareDatas> cDatas = new ArrayList<>();
	
	public ArrayList<CompareDatas> getCompareDatas(){
		return cDatas;
	}
	
	/*
	 * set interval between print progress
	 * unit: second
	 */
	private static final int timeInterval = 60;
	
	public int getNumOfAlarms () {
		return numOfAlarms;
	}
	
	public void run(String resultPath, int numOfAlarmFromSARM) {		
		try {
			Reader outputFile = new FileReader(resultPath);
			MethodFinder methodFinder = new MethodFinder();
			Iterable<CSVRecord> records = CSVFormat.EXCEL.parse(outputFile);
			
			int cnt = 0;
			long startTime = System.currentTimeMillis();
			boolean timerFlag = false;
			
			for (CSVRecord record : records) {									
				if(record.get(0).equals("Detection ID")) continue;
				
				cnt ++;										
				
				String filePath = record.get(1);
				String newFilePath = modifyFilePathToOS(filePath);									
				String startLineNum = record.get(2);
				String endLineNum = record.get(2);

				cDatas.add(dataPreprocess(new RawData(newFilePath, startLineNum, endLineNum, record.get(3)), methodFinder));				
				filePath = null;
				newFilePath = null;
				startLineNum = null;
				endLineNum = null;
				
				long currentTime = System.currentTimeMillis();
				long sec = (currentTime - startTime) / 1000;
				
				if(sec > timeInterval)
					timerFlag = true;
					
				if(numOfAlarmFromSARM != 0)
					printProgress(cnt, numOfAlarmFromSARM);
				else if(timerFlag == true){
					startTime = System.currentTimeMillis();
					timerFlag = false;
					printProgress(cnt);
				}
				
				System.out.println("" + cnt);
			}
			numOfAlarms = cnt;
		} catch(IOException e) {
			e.printStackTrace();
		}		
	}
	
	private CompareDatas dataPreprocess(RawData rawData, MethodFinder methodFinder) {							
	    ProcessedData pData = getProcessedData(rawData, methodFinder); 
	   
	    //the case when the violating line is not in a method but in static block or something.
	    if(pData.getVMethod() != null) {
	    	pData.setVNode(findVNode(rawData, pData.getVMethod()));
	    	rawData =null;
	    	return divide(pData);
	    }
	    
	    else return null;
	}
	
	private ProcessedData getProcessedData(RawData rawData, MethodFinder methodFinder) {
		StringBuilder builder = new StringBuilder();
		try {
			FileInputStream fs = new FileInputStream(rawData.getPath());			
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
		rawData.setSrc(builder.toString());
		builder = null;
		return methodFinder.findMethod(rawData);				
	}
	
	private ITree findVNode(RawData rawData, ITree vMethod) {
		ITree vNode = null;
		//set vNode based on line number in ITree
        List<ITree> currents = new ArrayList<>();
        currents.add(vMethod);
        while (currents.size() > 0) {        	
            ITree c = currents.remove(0);
            if(contain(c.getNode2String(), rawData.getVLine()) && 
            		(c.getStartLineNum() <= rawData.getStart() && c.getEndLineNum() >= rawData.getEnd())) {        
            	vNode = c;
            }
            currents.addAll(c.getChildren());
        }
		
		return vNode;
	}
	
	//for remove all blank and only compare characters
	private boolean contain(String src, String test) {
		String newSrc = "";
		String newTest = "";
		for(int i = 0; i < src.length(); i ++) {
			if(Character.isSpaceChar(src.charAt(i)) || Character.isWhitespace(src.charAt(i))) {
				continue;
			}
			newSrc += src.charAt(i);
		}
		
		for(int i = 0; i < test.length(); i ++) {
			if(Character.isSpaceChar(test.charAt(i)) || Character.isWhitespace(test.charAt(i))) {
				continue;
			}
			newTest += test.charAt(i);
		}
		
		return newSrc.contains(newTest);
	}
		
	private CompareDatas divide(ProcessedData pData) {					
		List<ITree> currents = new ArrayList<>();
		CompareDatas cDatas = new CompareDatas();
		currents.add(pData.getVMethod());
	    while (currents.size() > 0) {        	
	        ITree c = currents.remove(0);
//	        if(c.getPos() <= pData.getVNode().getPos() && c.getEndPos() <= pData.getVNode().getPos()) {        
//	            	cData.addForwardPart(c);
//	        } else if(c.getPos() >= pData.getVNode().getPos() && c.getEndPos() <= pData.getVNode().getEndPos()) {
	        if(c.getPos() >= pData.getVNode().getPos() && c.getEndPos() <= pData.getVNode().getEndPos()) {	        	
	        	cDatas.addCompareData(new CompareData(c.getParentProps(), c.getType(), c.getPos(), c.getDepth()));
	        }
//	        	cData.addVPart(c);
//	        } else if(c.getPos() >= pData.getVNode().getEndPos()) {
//	            	cData.addBackwardPart(c);
//	        }
	        currents.addAll(c.getChildren());
	    }
	    sort(cDatas);
	        
	    return cDatas;
	}
	
	private void sort(CompareDatas cDatas) {
//		cData.getForwardPart().sort(new Comparator<ITree>() {
//			public int compare(ITree node1, ITree node2) {
//				if(node1.getPos() > node2.getPos()) {
//					return 1;
//				} else if(node1.getPos() == node2.getPos()) {
//					if(node1.getDepth() > node2.getDepth()) {
//						return 1;
//					} else return -1;
//				} else return -1;
//			}
//		});
		
		cDatas.getCompareDatas().sort(new Comparator<CompareData>() {
			public int compare(CompareData node1, CompareData node2) {
				if(node1.getPos() > node2.getPos()) {
					return 1;
				} else if(node1.getPos() == node2.getPos()) {
					if(node1.getDepth() > node2.getDepth()) {
						return 1;
					} else return -1;
				} else return -1;
			}
		});
		
//		cData.getBackwardPart().sort(new Comparator<ITree>() {
//			public int compare(ITree node1, ITree node2) {
//				if(node1.getPos() > node2.getPos()) {
//					return 1;
//				} else if(node1.getPos() == node2.getPos()) {
//					if(node1.getDepth() > node2.getDepth()) {
//						return 1;
//					} else return -1;
//				} else return -1;
//			}
//		});
	}
	
	private void printProgress(int cnt) {		
			System.out.println("INFO: Current Progress is "  + cnt);	
	}
	
	private void printProgress(int cnt, int total) {
		if(total / 10 == cnt) {
			System.out.print("10%...");
		}
		else if(total * 2 / 10 == cnt) {
			System.out.print("20%...");
		}
		else if(total * 3 / 10 == cnt) {
			System.out.print("30%...");
		}
		else if(total * 4 / 10 == cnt) {
			System.out.print("40%...");
		}
		else if(total * 5/ 10 == cnt) {
			System.out.print("50%...");
		}
		else if(total * 6 / 10 == cnt) {
			System.out.print("60%...");
		}
		else if(total * 7 / 10 == cnt) {
			System.out.print("70%...");
		}
		else if(total * 8 / 10 == cnt) {
			System.out.print("80%...");
		}
		else if(total * 9 / 10 == cnt) {
			System.out.print("90%...");
		}
		else if(total-1 == cnt) {
			System.out.print("done!\n");
		}		
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
		
		return filePath;
	}	
}