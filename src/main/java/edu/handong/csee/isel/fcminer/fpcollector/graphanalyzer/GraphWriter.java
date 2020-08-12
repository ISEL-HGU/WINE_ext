package edu.handong.csee.isel.fcminer.fpcollector.graphanalyzer;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

import edu.handong.csee.isel.fcminer.fpcollector.graphbuilder.ControlNode;
import edu.handong.csee.isel.fcminer.fpcollector.graphclassifier.NodeInterpreter;
import edu.handong.csee.isel.fcminer.util.GraphResult;

public class GraphWriter {
	public String fileName;
	
	public void writeTotalNumRankGraph(ArrayList<Entry<Integer, ArrayList<GraphInfo>>> graphs, String type, int totalSize) {
//		String fileName = ;/* ./Result.csv */
		 fileName = "./" + type + "GraphRepresentation.csv";
		
		try(
			BufferedWriter writer = Files.newBufferedWriter(Paths.get(fileName));
			CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT
									.withHeader("Path", "Method", "Graph", "Graph Information"));
			) {
			String totalInfo = "";
			for(Entry<Integer, ArrayList<GraphInfo>> g : graphs) {
				totalInfo += "Number of Size "+ g.getKey() + " : " + g.getValue().size() + " (" +  (double)g.getValue().size()/totalSize*100 + ")\n";
			}
			csvPrinter.printRecord("Total", "Graph", "Information : ", totalInfo);
			for(Entry<Integer, ArrayList<GraphInfo>> g : graphs) {
//				Integer i = g.getKey();
//				if(totalNodeNum == 0 || totalNodeNum == 1) continue;
				for (GraphInfo tempGraph : g.getValue()) {
					String path = tempGraph.root.path;
					String method = tempGraph.root.node.toString();
					String graph = tempGraph.root.writeInfo();
					String graphInfo = tempGraph.getNumberInfo();
		
					csvPrinter.printRecord(path, method, graph, graphInfo);
				}
			}
			writer.flush();
			writer.close();
		}catch(IOException e){
			e.printStackTrace();
		} 
	}
	
	public void writeNodeRankGraph(ArrayList<GraphResult> graphs, String type, int totalSize) {
//		String fileName = ;/* ./Result.csv */
		 fileName = "./" + type + "GraphRepresentation.csv";
		
		try(
			BufferedWriter writer = Files.newBufferedWriter(Paths.get(fileName));
			CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT
									.withHeader("Path", "Method", "Graph", "Graph Information"));
			) {
			String totalInfo = "";
			for(GraphResult g : graphs) {
				totalInfo += "Number of Size "+ g.getGraphResult().getKey() + " : " + g.getGraphResult().getValue().size() + " (" +  (double)g.getGraphResult().getValue().size()/totalSize*100 + ")\n";
			}
			csvPrinter.printRecord("Total", "Graph", "Information : ", totalInfo);
			for(GraphResult g : graphs) {
//				Integer i = g.getKey();
//				if(totalNodeNum == 0 || totalNodeNum == 1) continue;
				for (GraphInfo tempGraph : g.getGraphResult().getValue()) {
					String path = tempGraph.root.path;
					String method = tempGraph.root.node.toString();
					String graph = tempGraph.root.writeInfo();
					String graphInfo = "Pattern : " + g.getGraphResult().getKey() + "\n" + tempGraph.getNumberInfo();
		
					csvPrinter.printRecord(path, method, graph, graphInfo);
				}
			}
			writer.flush();
			writer.close();
		}catch(IOException e){
			e.printStackTrace();
		} 
	}
	
	public void writeGraph (HashMap<Integer, ArrayList<GraphInfo>> g, String type) {
//		String fileName = ;/* ./Result.csv */
		 fileName = "./" + type + "GraphRepresentation.csv";
		
		try(
			BufferedWriter writer = Files.newBufferedWriter(Paths.get(fileName));
			CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT
									.withHeader("Path", "Method", "Graph", "Graph Information"));
			) {
			String totalInfo = "";
			for(Integer totalNodeNum : g.keySet()) {
				totalInfo += "Number of Size "+ totalNodeNum + " : " + g.get(totalNodeNum).size() + "\n";				
			}
			csvPrinter.printRecord("Total", "Graph", "Information : ", totalInfo);
			for(Integer totalNodeNum : g.keySet()) {
//				if(totalNodeNum == 0 || totalNodeNum == 1) continue;
				for(GraphInfo tempGraph : g.get(totalNodeNum)) {
					String path = tempGraph.root.path;
					String method = tempGraph.root.node.toString();
					String graph = tempGraph.root.writeInfo();
					String graphInfo = tempGraph.getNumberInfo();
		
					csvPrinter.printRecord(path, method, graph, graphInfo);
				}
			}
			writer.flush();
			writer.close();
		}catch(IOException e){
			e.printStackTrace();
		} 
	}
	
	public void writeGraphS (HashMap<String, ArrayList<GraphInfo>> g) {		
		try(
			BufferedWriter writer = Files.newBufferedWriter(Paths.get(fileName));
			CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT
									.withHeader("Path", "Method", "Graph", "Graph Information"));
			) {
			NodeInterpreter interpreter = new NodeInterpreter();
			String totalInfo = "";
			for(String totalNodeNum : g.keySet()) {
				totalInfo += "Pattern : "+ interpreter.interpret(totalNodeNum) + " : " + g.get(totalNodeNum).size() + "\n";				
			}
			csvPrinter.printRecord("Total", "Graph", "Information : ", totalInfo);
			for(String totalNodeNum : g.keySet()) {				
				for(GraphInfo tempGraph : g.get(totalNodeNum)) {
					String path = tempGraph.root.path;
					String method = tempGraph.root.node.toString();
					String graph = tempGraph.root.writeInfo();
					String graphInfo = totalNodeNum + ": " + interpreter.interpret(totalNodeNum) + "\n" + tempGraph.getNumberInfo();
		
					csvPrinter.printRecord(path, method, graph, graphInfo);
				}
			}
			writer.flush();
			writer.close();
		}catch(IOException e){
			e.printStackTrace();
		} 
	}
	
	public void writeGraph (ArrayList<ControlNode> g) {
//		String fileName = ;/* ./Result.csv */
		
		try(
			BufferedWriter writer = Files.newBufferedWriter(Paths.get(fileName));
			CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT
									.withHeader("Path", "Method", "Graph"));
			) {
			String totalInfo = "";			
			csvPrinter.printRecord("Total", "Graph", "Information : ", totalInfo);
			for(ControlNode tempGraph : g) {							
					String path = tempGraph.path;
					String method = tempGraph.node.toString();
					String graph = tempGraph.writeInfo();					
					
					csvPrinter.printRecord(path, method, graph);				
			}
			writer.flush();
			writer.close();
		}catch(IOException e){
			e.printStackTrace();
		} 
	}
	
}

