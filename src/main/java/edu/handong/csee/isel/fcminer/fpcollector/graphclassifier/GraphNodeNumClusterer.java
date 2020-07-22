package edu.handong.csee.isel.fcminer.fpcollector.graphclassifier;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map.Entry;

import edu.handong.csee.isel.fcminer.fpcollector.graphanalyzer.GraphInfo;

public class GraphNodeNumClusterer{
	public int totalGraphSize = 0;
	
	public HashMap <Integer, ArrayList<GraphInfo>> clusterByTotalNum = new HashMap<>();
	public ArrayList<Entry<Integer, ArrayList<GraphInfo>>> clusterByTotalNumRank = new ArrayList<>();
	
	public GraphNodeNumClusterer(ArrayList<GraphInfo> graphInfos) {
		totalGraphSize = graphInfos.size();
	}
	
	public void run(ArrayList<GraphInfo> graphInfos) {
		for (GraphInfo g : graphInfos)
			clusterByTotalNodeNum(g);
		
		clusterByTotalNodeNumRank();
	}
	
	private void clusterByTotalNodeNum(GraphInfo g) {
		Integer totalNodeNum = g.controlNodeNum + g.dataNodeNum;
		if(!clusterByTotalNum.containsKey(totalNodeNum)) {
			clusterByTotalNum.put(totalNodeNum, null);
			ArrayList<GraphInfo> tempGraphInfo = new ArrayList<>();
			tempGraphInfo.add(g);
			clusterByTotalNum.replace(totalNodeNum, tempGraphInfo);
		}
		else{
			clusterByTotalNum.get(totalNodeNum).add(g);
		}
	}
	
	private void clusterByTotalNodeNumRank() {
		for (Entry<Integer, ArrayList<GraphInfo>> g : clusterByTotalNum.entrySet())
			clusterByTotalNumRank.add(g);
		
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
