package edu.handong.csee.isel.fcminer.fpcollector.tokendiff;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import edu.handong.csee.isel.fcminer.fpcollector.tokendiff.ast.ITree;
import edu.handong.csee.isel.fcminer.fpcollector.tokendiff.compare.CodeComparator;
import edu.handong.csee.isel.fcminer.fpcollector.tokendiff.compare.MappingStorage;
import edu.handong.csee.isel.fcminer.fpcollector.tokendiff.datapreproc.CompareData;
import edu.handong.csee.isel.fcminer.fpcollector.tokendiff.datapreproc.MethodFinder;
import edu.handong.csee.isel.fcminer.fpcollector.tokendiff.datapreproc.ProcessedData;
import edu.handong.csee.isel.fcminer.fpcollector.tokendiff.datapreproc.RawData;
import edu.handong.csee.isel.fcminer.fpcollector.tokendiff.datapreproc.RawDataCollector;
import edu.handong.csee.isel.fcminer.fpcollector.tokendiff.datapreproc.RawDatas;
import edu.handong.csee.isel.fcminer.fpcollector.tokendiff.datapreproc.RawDatasStorage;

public class TokenDiffMain {
		
	public ArrayList<MappingStorage> run(String resultPath, int numOfAlarms) {
		//collect violating file path, line number, violating code line
		System.out.println("INFO: Raw Data Collecting is Started");
		long start = System.currentTimeMillis();
		RawDatasStorage rawDataSto = new RawDatasStorage();
		rawDataSto.saveRawDatas(dataCollecting(resultPath, numOfAlarms));				
		
		long end = System.currentTimeMillis();
		long time = (end - start) / 100;
		System.out.println("INFO: Raw Data Collecting is Finished - " + time + " sec.");
		
		//get violating method, violating node
		System.out.println("INFO: Data Pre-Processing is Started");	
		start = System.currentTimeMillis();
		ArrayList<CompareData> cDatas = new ArrayList<>();
		int cnt =0;		
		MethodFinder methodFinder = new MethodFinder();
		
		for(RawDatas rDatas : rawDataSto.getRawDatas()) {
			for(RawData rawData : rDatas.getRawDatas()) {
				cnt ++;
				//the case when the violating line is not in a method but in static block or something.
				CompareData cData = new CompareData();
				cData = dataPreprocess(rawData, methodFinder);
				rawData = null;
				if(cData != null)
					cDatas.add(cData);
					
				System.out.println("" + cnt);
			}			
			printProgress(cnt, rawDataSto.getRawDatas().size());
		}
		
		end = System.currentTimeMillis();
		time = (end - start) / 100;
		System.out.println("INFO: Data Pre-Processing is Finished - " + time + " sec.");		
		
		//compare by using v part, forward part backward part
		System.out.println("INFO: Code Comparison is Started");
		return codeCompare(cDatas);				
	}
	
	private ArrayList<RawData> dataCollecting(String resultPath, int numOfAlarms) {
		RawDataCollector collector = new RawDataCollector();			
		System.out.println("Info: Data Collecting is Started");
		collector.run(resultPath, numOfAlarms);				
		System.out.println("Info: Data Collecting is Finished, # of Alamrs: " + collector.getNumOfAlarms());
		return collector.getRawDatas();
	}
	
	private CompareData dataPreprocess(RawData rawData, MethodFinder methodFinder) {							
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
	
	
	
	private CompareData divide(ProcessedData pData) {	
		CompareData cData = new CompareData();
		
		List<ITree> currents = new ArrayList<>();
		
		currents.add(pData.getVMethod());
        while (currents.size() > 0) {        	
            ITree c = currents.remove(0);
            if(c.getPos() <= pData.getVNode().getPos() && c.getEndPos() <= pData.getVNode().getPos()) {        
//            	cData.addForwardPart(c);
            } else if(c.getPos() >= pData.getVNode().getPos() && c.getEndPos() <= pData.getVNode().getEndPos()) {
            	cData.addVPart(c);
            } else if(c.getPos() >= pData.getVNode().getEndPos()) {
//            	cData.addBackwardPart(c);
            }
            currents.addAll(c.getChildren());
        }
        sort(cData);
        
        return cData;
	}
	
	private void sort(CompareData cData) {
		cData.getForwardPart().sort(new Comparator<ITree>() {
			public int compare(ITree node1, ITree node2) {
				if(node1.getPos() > node2.getPos()) {
					return 1;
				} else if(node1.getPos() == node2.getPos()) {
					if(node1.getDepth() > node2.getDepth()) {
						return 1;
					} else return -1;
				} else return -1;
			}
		});
		
		cData.getVPart().sort(new Comparator<ITree>() {
			public int compare(ITree node1, ITree node2) {
				if(node1.getPos() > node2.getPos()) {
					return 1;
				} else if(node1.getPos() == node2.getPos()) {
					if(node1.getDepth() > node2.getDepth()) {
						return 1;
					} else return -1;
				} else return -1;
			}
		});
		
		cData.getBackwardPart().sort(new Comparator<ITree>() {
			public int compare(ITree node1, ITree node2) {
				if(node1.getPos() > node2.getPos()) {
					return 1;
				} else if(node1.getPos() == node2.getPos()) {
					if(node1.getDepth() > node2.getDepth()) {
						return 1;
					} else return -1;
				} else return -1;
			}
		});
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
	
	private ArrayList<MappingStorage> codeCompare(ArrayList<CompareData> infos) { 
		CodeComparator tokenDiff = new CodeComparator();		
		for(int i = 0 ; i < infos.size(); i ++) {
			if(infos.get(i) == null) continue;
			printProgress(i, infos.size());
			
			tokenDiff.compare(infos.get(i));
			for(int j = i ; j < infos.size(); j++) {
				if(infos.get(j) == null) continue;
				tokenDiff.compare(infos.get(j));
			}

			tokenDiff.clear();
		}				
		return tokenDiff.getMappingStorage();
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
}
