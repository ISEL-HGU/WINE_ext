package edu.handong.csee.isel.fcminer.clustergen;

import java.util.ArrayList;

import edu.handong.csee.isel.fcminer.fpcollector.subset.Superset;
import edu.handong.csee.isel.fcminer.fpcollector.tokendiff.datapreproc.CompareData;

public class ClusterGenerator {
	public void cluster(ArrayList<Superset> superSet) {
		preprocess(superSet);
		findClusters();
		printClusters();
	}
	
	private ArrayList<String> preprocess(ArrayList<Superset> superSet) {		
		ArrayList<String> updatedNCLs = new ArrayList<>();
		//distinguish each node --> update NCL with only Leaf Nodes and specified Internal nodes
		/*input
		 *	superSet.compareDatas (startPos, leafs, label) --> add information to property not to compare data.
		 *	startPos: node's absolute position
		 *	leaf: isLeaf()
		 *	values of token: nodeStr
		 */
		
		//to-do: modifying assign property == when get property information, get pos and label information.
		StringBuilder updatedNCL = new StringBuilder();
		for(int i = 0; i < superSet.size(); i ++) {
			ArrayList<CompareData> warning = superSet.get(i).getLineNodes().getCompareDatas();
			for(int j = 0; j < warning.size(); j++) {
				CompareData tmpNode = warning.get(j);
				if(tmpNode.isLeaf());
					//if true (update properties and concat to updated NCL)
					//else (pass)							
			}
		}			
			
			/*output
			 *	updatedNCL: list of pair of updatedNC
			 * 	updatedNC: a pair of (updatedNode, updatedParentProperty)
			 * 	updatedNode: a three tuple of (Node type, label, position)
			 * 	updatedParentProperty: a three tuple of (parent type, property, position)
			 */
					
		//symbolize them
		
		//encoding them
		
		return updatedNCLs;
	}
	
	private void findClusters() {
		
	}
	
	private void printClusters() {
		
	}
}
