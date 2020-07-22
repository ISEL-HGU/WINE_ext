package edu.handong.csee.isel.fcminer.fpcollector;

import java.io.IOException;
import java.util.ArrayList;

import org.eclipse.jgit.api.Git;

import edu.handong.csee.isel.fcminer.fpcollector.graphanalyzer.GraphInfo;
import edu.handong.csee.isel.fcminer.fpcollector.graphanalyzer.GraphInfoGetter;
import edu.handong.csee.isel.fcminer.fpcollector.graphanalyzer.GraphWriter;
import edu.handong.csee.isel.fcminer.fpcollector.graphbuilder.ControlNode;
import edu.handong.csee.isel.fcminer.fpcollector.graphbuilder.InfoCollector;
import edu.handong.csee.isel.fcminer.fpcollector.graphclassifier.GraphNodeClusterer;
import edu.handong.csee.isel.fcminer.fpcollector.graphclassifier.GraphNodeNumClusterer;
import edu.handong.csee.isel.fcminer.fpcollector.graphclassifier.GraphRankWriter;
import edu.handong.csee.isel.fcminer.fpcollector.graphclassifier.NodeResolver;

public class FPCollector {	
	public void run(String sarMinerResultPath, Git git) {
		String projectName = git.toString(); 			
		
		// 3. Get Pattern of the FPC		
		ArrayList<ControlNode> graphs = new ArrayList<>();		
		graphs = drawGraph(sarMinerResultPath, git);				
		
		//Step 1. Get Graph Information
		ArrayList<GraphInfo> fpcGraphInfos = new ArrayList<>();
		ArrayList<GraphInfo> tpcGraphInfos = new ArrayList<>();		
		fpcGraphInfos = getGraphInfo(fpcGraphs);
		tpcGraphInfos = getGraphInfo(tpcGraphs);
		fpcGraphs = null;
		tpcGraphs = null;		
			
		//Step 5. Graph Clustering
		clusterGraphByNodeNum(fpcGraphInfos, tpcGraphInfos, projectName);
		clusterGraphByNode(fpcGraphInfos, tpcGraphInfos, projectName);
		System.out.println("Step 5 Clear");
	}
	
	private ArrayList<ControlNode> drawGraph(String sarMinerResultpath, Git git) { 
		InfoCollector collector = new InfoCollector();		
		try {
			collector.run(sarMinerResultpath, git);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		System.out.println("graphSize: " + collector.graphs.size());
		
		return collector.graphs;
	}
	
	private ArrayList<GraphInfo> getGraphInfo(ArrayList<ControlNode> graphs) {
		ArrayList<GraphInfo> graphInfos = new ArrayList<>();
			int count = 0;
			for(ControlNode g : graphs) {
				count++;
				System.out.println("GraphInfo " + count);
				if(count == 488) 
					System.out.println("A");
				GraphInfo tempGraphInfo = new GraphInfo(g);
				GraphInfoGetter tempGetter = new GraphInfoGetter();
//				g.printInfo();
				tempGetter.getNodeNum(tempGraphInfo);
				graphInfos.add(tempGraphInfo);
			}
			
			return graphInfos;
	}
	
	private void clusterGraphByNodeNum(ArrayList<GraphInfo> fpcGraphInfos, ArrayList<GraphInfo> tpcGraphInfos, String projectName) {
		GraphNodeNumClusterer fpcGraphNodeNumClusterer = new GraphNodeNumClusterer(fpcGraphInfos);
		GraphNodeNumClusterer tpcGraphNodeNumClusterer = new GraphNodeNumClusterer(tpcGraphInfos);
		
		fpcGraphNodeNumClusterer.run(fpcGraphInfos);
		tpcGraphNodeNumClusterer.run(tpcGraphInfos);
		
		GraphWriter graphWriter = new GraphWriter();
		
		//cluster by total node num
		graphWriter.writeTotalNumRankGraph(fpcGraphNodeNumClusterer.clusterByTotalNumRank, projectName + "FPCTNNum", fpcGraphNodeNumClusterer.totalGraphSize);
		graphWriter.writeTotalNumRankGraph(tpcGraphNodeNumClusterer.clusterByTotalNumRank, projectName + "TPCTNNum", tpcGraphNodeNumClusterer.totalGraphSize);
		
		//rank graph
		GraphRankWriter graphRankWriter = new GraphRankWriter();
		graphRankWriter.writeRankGraphTotalNum(fpcGraphNodeNumClusterer, tpcGraphNodeNumClusterer, projectName);
	}
	
	private void clusterGraphByNode(ArrayList<GraphInfo> fpcGraphInfos, ArrayList<GraphInfo> tpcGraphInfos, String projectName) {
		GraphNodeClusterer fpcGraphNodeClusterer = new GraphNodeClusterer(fpcGraphInfos);
		GraphNodeClusterer tpcGraphNodeClusterer = new GraphNodeClusterer(tpcGraphInfos);
		
		//transform each graph to one string and clustering graphs with there properties
		NodeResolver nodeResolver = new NodeResolver();
		nodeResolver.transformNode(fpcGraphNodeClusterer.clusterByTotalNum);
		nodeResolver.transformNode(tpcGraphNodeClusterer.clusterByTotalNum);
		
		fpcGraphNodeClusterer.run(fpcGraphInfos);
		tpcGraphNodeClusterer.run(tpcGraphInfos);
		
		GraphWriter graphWriter = new GraphWriter();
		
		//cluster by violated node
		graphWriter.writeNodeRankGraph(fpcGraphNodeClusterer.clusterByTotalNodeRank, projectName + "FPCNode", fpcGraphNodeClusterer.totalGraphSize);
		graphWriter.writeNodeRankGraph(tpcGraphNodeClusterer.clusterByTotalNodeRank, projectName + "TPCNode", tpcGraphNodeClusterer.totalGraphSize);
		
		//rank graph
		GraphRankWriter graphRankWriter = new GraphRankWriter();
		graphRankWriter.writeRankGraph(fpcGraphNodeClusterer, tpcGraphNodeClusterer, projectName);
	}	
}
