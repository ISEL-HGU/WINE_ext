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
	public void run(String sarMinerResultPath, ArrayList<Git> gits) {
		InfoCollector collector = new InfoCollector();	
		
		for(Git git : gits) {
			String[] fullProjectPath = git.getRepository().getIdentifier().split("/");
			String projectName = fullProjectPath[fullProjectPath.length-2]; 					
				
			try {
				collector.run(sarMinerResultPath, git, projectName);
			} catch (IOException e) {
				e.printStackTrace();
			}		
		}
		System.out.println("Step 3 CLEAR");
			
		//Step 4. Get Graph Information
		ArrayList<GraphInfo> fpcGraphInfos = new ArrayList<>();
		ArrayList<GraphInfo> tpcGraphInfos = new ArrayList<>();
		
		fpcGraphInfos = getGraphInfo(collector.getFPCGraphs());
		tpcGraphInfos = getGraphInfo(collector.getTPCGraphs());		
		
		System.out.println("Step 4 Clear");
			
		//Step 5. Graph Clustering
		clusterGraphByNodeNum(fpcGraphInfos, tpcGraphInfos);
		clusterGraphByNode(fpcGraphInfos, tpcGraphInfos);
		System.out.println("Step 5 Clear");		
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
	
	private void clusterGraphByNodeNum(ArrayList<GraphInfo> fpcGraphInfos, ArrayList<GraphInfo> tpcGraphInfos) {
		GraphNodeNumClusterer fpcGraphNodeNumClusterer = new GraphNodeNumClusterer(fpcGraphInfos);
		GraphNodeNumClusterer tpcGraphNodeNumClusterer = new GraphNodeNumClusterer(tpcGraphInfos);
		
		fpcGraphNodeNumClusterer.run(fpcGraphInfos);
		tpcGraphNodeNumClusterer.run(tpcGraphInfos);
		
		GraphWriter graphWriter = new GraphWriter();
		
		//cluster by total node num
		graphWriter.writeTotalNumRankGraph(fpcGraphNodeNumClusterer.clusterByTotalNumRank, "FPCTNNum", fpcGraphNodeNumClusterer.totalGraphSize);
		graphWriter.writeTotalNumRankGraph(tpcGraphNodeNumClusterer.clusterByTotalNumRank, "TPCTNNum", tpcGraphNodeNumClusterer.totalGraphSize);
		
		//rank graph
		GraphRankWriter graphRankWriter = new GraphRankWriter();
		graphRankWriter.writeRankGraphTotalNum(fpcGraphNodeNumClusterer, tpcGraphNodeNumClusterer);
	}
	
	private void clusterGraphByNode(ArrayList<GraphInfo> fpcGraphInfos, ArrayList<GraphInfo> tpcGraphInfos) {
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
		graphWriter.writeNodeRankGraph(fpcGraphNodeClusterer.clusterByTotalNodeRank, "FPCNode", fpcGraphNodeClusterer.totalGraphSize);
		graphWriter.writeNodeRankGraph(tpcGraphNodeClusterer.clusterByTotalNodeRank, "TPCNode", tpcGraphNodeClusterer.totalGraphSize);
		
		//rank graph
		GraphRankWriter graphRankWriter = new GraphRankWriter();
		graphRankWriter.writeRankGraph(fpcGraphNodeClusterer, tpcGraphNodeClusterer);
	}	
}
