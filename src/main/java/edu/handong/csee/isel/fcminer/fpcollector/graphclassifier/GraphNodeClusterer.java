package edu.handong.csee.isel.fcminer.fpcollector.graphclassifier;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map.Entry;

import edu.handong.csee.isel.fcminer.fpcollector.graphanalyzer.GraphInfo;

public class GraphNodeClusterer extends GraphNodeNumClusterer{
	public int totalGraphSize = 0;
	
	public HashMap <String, ArrayList<GraphInfo>> clusterByTotalNode = new HashMap<>();
	public ArrayList<Entry<String, ArrayList<GraphInfo>>> clusterByTotalNodeRank = new ArrayList<>();
	
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
		for (Entry<String, ArrayList<GraphInfo>> g : clusterByTotalNode.entrySet())
			clusterByTotalNodeRank.add(g);
		
		clusterByTotalNumRank.sort(new Comparator<Entry<Integer, ArrayList<GraphInfo>>>() {
			@Override
			public int compare(Entry<Integer, ArrayList<GraphInfo>> o1, Entry<Integer, ArrayList<GraphInfo>> o2) {
				// TODO Auto-generated method stub
				int size1 = o1.getValue().size();
				int size2 = o2.getValue().size();
				
				if (size1 == size2)
					return 0;
				else if (size1 > size2)
					return -1;
				else
					return 1;
			}
		});
	}
}
