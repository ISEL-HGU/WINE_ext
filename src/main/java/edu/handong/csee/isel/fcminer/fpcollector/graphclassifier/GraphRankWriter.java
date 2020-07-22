package edu.handong.csee.isel.fcminer.fpcollector.graphclassifier;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Map.Entry;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

import edu.handong.csee.isel.fcminer.fpcollector.graphanalyzer.GraphInfo;

public class GraphRankWriter {
	public String fileName;
	
	public void writeRankGraphTotalNum(GraphNodeNumClusterer fpcGraphCompartor, GraphNodeNumClusterer tpcGraphCompartor, String projectName) {
		 fileName = "./" + projectName + "NumRankGraphRepresentation.csv";
		 
		 ArrayList<Entry<Integer, ArrayList<GraphInfo>>> fpcGraphs = fpcGraphCompartor.clusterByTotalNumRank;
		 ArrayList<Entry<Integer, ArrayList<GraphInfo>>> tpcGraphs = tpcGraphCompartor.clusterByTotalNumRank;
		 int fpcTotalSize = fpcGraphCompartor.totalGraphSize;
		 int tpcTotalSize = tpcGraphCompartor.totalGraphSize;
		 
		try(
			BufferedWriter writer = Files.newBufferedWriter(Paths.get(fileName));
			CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT
									.withHeader("Information \\ EXIST", "TPC: O / FPC: O", "TPC: O / FPC: X", "TPC: X / FPC: O", "TPC: X / FPC: X"));
			) {
			String TPCFPC = "";
			String TPC = "";
			String FPC = "";
			String None = "None";
					
			for (Entry<Integer, ArrayList<GraphInfo>> fpcGraph : fpcGraphs) {
				boolean existTPC = false;
				for (Entry<Integer, ArrayList<GraphInfo>> tpcGraph : tpcGraphs) {
					if (tpcGraph.getKey().equals(fpcGraph.getKey())) {
						existTPC = true;
						TPCFPC += "Number of Size "+ fpcGraph.getKey() 
										+ " : (fpc: " + fpcGraph.getValue().size() 
										+ " / " + (double)fpcGraph.getValue().size()/fpcTotalSize*100 
										+ ") (tpc: " + tpcGraph.getValue().size() 
										+ " / " + (double)tpcGraph.getValue().size()/tpcTotalSize*100 + ")\n";
						tpcGraphs.remove(tpcGraph);
						break;
					}
				}
				if (!existTPC) 
					FPC += "Number of Size "+ fpcGraph.getKey() 
								+ " : (fpc: " + fpcGraph.getValue().size() 
								+ " / " + (double)fpcGraph.getValue().size()/fpcTotalSize*100 + ")\n";
			}
			
			for (Entry<Integer, ArrayList<GraphInfo>> tpcGraph : tpcGraphs) {
				TPC += "Number of Size "+ tpcGraph.getKey() 
							+ " : (tpc: " + tpcGraph.getValue().size() 
							+ " / " + (double)tpcGraph.getValue().size()/tpcTotalSize*100 + ")\n";
			}
			
			csvPrinter.printRecord("Information", TPCFPC, TPC, FPC, None);
			writer.flush();
			writer.close();
		}catch(IOException e){
			e.printStackTrace();
		} 
	}
	
	public void writeRankGraph(GraphNodeClusterer fpcGraphCompartor, GraphNodeClusterer tpcGraphCompartor, String projectName) {
		fileName = "./" + projectName + "NodeRankGraphRepresentation.csv";
		 
		ArrayList<Entry<String, ArrayList<GraphInfo>>> fpcGraphs = fpcGraphCompartor.clusterByTotalNodeRank;
		ArrayList<Entry<String, ArrayList<GraphInfo>>> tpcGraphs = tpcGraphCompartor.clusterByTotalNodeRank;
		int fpcTotalSize = fpcGraphCompartor.totalGraphSize;
		int tpcTotalSize = tpcGraphCompartor.totalGraphSize;
		 
		try(
			BufferedWriter writer = Files.newBufferedWriter(Paths.get(fileName));
			CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT
									.withHeader("Information \\ EXIST", "TPC: O / FPC: O", "TPC: O / FPC: X", "TPC: X / FPC: O", "TPC: X / FPC: X"));
			) {
			String TPCFPC = "";
			String TPC = "";
			String FPC = "";
			String None = "None";
			NodeInterpreter interpreter = new NodeInterpreter();
			for (Entry<String, ArrayList<GraphInfo>> fpcGraph : fpcGraphs) {
				boolean existTPC = false;
				for (Entry<String, ArrayList<GraphInfo>> tpcGraph : tpcGraphs) {
					if (tpcGraph.getKey().equals(fpcGraph.getKey())) {
						existTPC = true;						
						TPCFPC += "Pattern : "+ interpreter.interpret(fpcGraph.getKey()) 
										+ " : (fpc: " + fpcGraph.getValue().size() 
										+ " / " + (double)fpcGraph.getValue().size()/fpcTotalSize*100 
										+ ") (tpc: " + tpcGraph.getValue().size() 
										+ " / " + (double)tpcGraph.getValue().size()/tpcTotalSize*100 + ")\n";
						tpcGraphs.remove(tpcGraph);
						break;
					}
				}
				if (!existTPC) 
					FPC += "Pattern : "+ interpreter.interpret(fpcGraph.getKey()) 
								+ " : (fpc: " + fpcGraph.getValue().size() 
								+ " / " + (double)fpcGraph.getValue().size()/fpcTotalSize*100 + ")\n";
			}			
			for (Entry<String, ArrayList<GraphInfo>> tpcGraph : tpcGraphs) {
				TPC += "Pattern : "+ interpreter.interpret(tpcGraph.getKey()) 
							+ " : (tpc: " + tpcGraph.getValue().size() 
							+ " / " + (double)tpcGraph.getValue().size()/tpcTotalSize*100 + ")\n";
			}			
			csvPrinter.printRecord("Information", TPCFPC, TPC, FPC, None);
			writer.flush();
			writer.close();
		}catch(IOException e){
			e.printStackTrace();
		} 
	}
}
