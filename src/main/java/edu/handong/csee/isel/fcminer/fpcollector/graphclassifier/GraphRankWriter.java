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
import edu.handong.csee.isel.fcminer.util.GraphResult;

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
		 
		ArrayList<GraphResult> fpcGraphs = fpcGraphCompartor.clusterByTotalNodeRank;
		ArrayList<GraphResult> tpcGraphs = tpcGraphCompartor.clusterByTotalNodeRank;
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
			for (GraphResult fpcGraph : fpcGraphs) {
				boolean existTPC = false;
				for (GraphResult tpcGraph : tpcGraphs) {
					if (tpcGraph.getGraphResult().getKey().equals(fpcGraph.getGraphResult().getKey())) {
						existTPC = true;						
						TPCFPC += "Pattern : "+ interpreter.interpret(fpcGraph.getGraphResult().getKey()) 
										+ " : (fpc: " + fpcGraph.getGraphResult().getValue().size() 
										+ " / " + (double)fpcGraph.getGraphResult().getValue().size()/fpcTotalSize*100 
										+ ") (tpc: " + tpcGraph.getGraphResult().getValue().size() 
										+ " / " + (double)tpcGraph.getGraphResult().getValue().size()/tpcTotalSize*100 + ")\n";
						tpcGraphs.remove(tpcGraph);
						break;
					}
				}
				if (!existTPC) 
					FPC += "Pattern : "+ interpreter.interpret(fpcGraph.getGraphResult().getKey()) 
								+ " : (fpc: " + fpcGraph.getGraphResult().getValue().size() 
								+ " / " + (double)fpcGraph.getGraphResult().getValue().size()/fpcTotalSize*100 + ")\n";
			}			
			for (GraphResult tpcGraph : tpcGraphs) {
				TPC += "Pattern : "+ interpreter.interpret(tpcGraph.getGraphResult().getKey()) 
							+ " : (tpc: " + tpcGraph.getGraphResult().getValue().size() 
							+ " / " + (double)tpcGraph.getGraphResult().getValue().size()/tpcTotalSize*100 + ")\n";
			}			
			csvPrinter.printRecord("Information", TPCFPC, TPC, FPC, None);
			writer.flush();
			writer.close();
		}catch(IOException e){
			e.printStackTrace();
		} 
	}
}
