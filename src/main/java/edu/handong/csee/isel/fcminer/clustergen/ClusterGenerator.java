package edu.handong.csee.isel.fcminer.clustergen;

import java.util.ArrayList;

import edu.handong.csee.isel.fcminer.fpcollector.subset.Superset;
import edu.handong.csee.isel.fcminer.util.Reader;

public class ClusterGenerator {
	public void cluster(ArrayList<Superset> superSet) {
		readResult();
		
	}
	
	private ArrayList<ExtractedResult> readResult(){
		Reader r = new Reader();
		return r.readExtractedResult("path");
	}
	
}
