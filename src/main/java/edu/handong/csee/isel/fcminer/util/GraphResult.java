package edu.handong.csee.isel.fcminer.util;

import java.util.ArrayList;
import java.util.Map.Entry;

import edu.handong.csee.isel.fcminer.fpcollector.graphanalyzer.GraphInfo;

public class GraphResult implements Comparable<GraphResult> {
	Entry<String, ArrayList<GraphInfo>> graph;
	
	public GraphResult(Entry<String,ArrayList<GraphInfo>> graph) {
		this.graph = graph;
	}
	
	public Entry<String, ArrayList<GraphInfo>> getGraphResult() {
		return graph;
	}
	
	@Override
	public int compareTo(GraphResult o) {		
		if (this.graph.getValue().size() < o.getGraphResult().getValue().size()) {
            return 1;
        } else if (this.graph.getValue().size() > o.getGraphResult().getValue().size()) {
            return -1;
        }
        return 0;
	}

}
