package edu.handong.csee.isel.fcminer.fpcollector.graphclassifier;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map.Entry;

import edu.handong.csee.isel.fcminer.fpcollector.graphanalyzer.GraphInfo;
import edu.handong.csee.isel.fcminer.util.GraphResult;

public class GraphNodeClusterer extends GraphNodeNumClusterer {
	public int totalGraphSize = 0;
	
	public HashMap <String, ArrayList<GraphInfo>> clusterByTotalNode = new HashMap<>();
	public ArrayList<GraphResult> clusterByTotalNodeRank = new ArrayList<>();
	
	public GraphNodeClusterer(ArrayList<GraphInfo> graphInfos) {
		super(graphInfos);
		super.run(graphInfos);
	}
	
	public void run(ArrayList<GraphInfo> graphInfos) {
		for (GraphInfo g : graphInfos)
			clusterByTotalNode(g);
		
		clusterByTotalNodeRank();
	}
	
	public void clusterByTotalNode(GraphInfo g) {		
		if(g.graph2String.equals("")) {
			return;
		}
		else {
			String graphString2Int = g.graph2String;						
			if(clusterByTotalNode.containsKey(graphString2Int)) {
				clusterByTotalNode.get(graphString2Int).add(g);
				clusterByTotalNode.replace(graphString2Int, clusterByTotalNode.get(graphString2Int));
			} else {
				ArrayList<GraphInfo> temp = new ArrayList<>();
				temp.add(g);
				clusterByTotalNode.put(graphString2Int, temp);
			}
		}

	}
	
	private void clusterByTotalNodeRank() {
		for (Entry<String, ArrayList<GraphInfo>> g : clusterByTotalNode.entrySet()) {
			GraphResult tempG = new GraphResult(g);
			clusterByTotalNodeRank.add(tempG);
		}
		for(GraphResult g : clusterByTotalNodeRank) {
			System.out.println(g.getGraphResult().getKey() + " " + g.getGraphResult().getValue().size());
		}
		
		Collections.sort(clusterByTotalNodeRank);
		System.out.println("\n%%%%%%%%%%%%%%%%%%%%%%\n");
		for(GraphResult g : clusterByTotalNodeRank) {
			System.out.println(g.getGraphResult().getKey() + " " + g.getGraphResult().getValue().size());
		}
		System.out.println("\n%%%%%%%%%%%%%%%%%%%%%%\n");
	}
}
