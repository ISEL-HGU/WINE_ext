package edu.handong.csee.isel.fcminer;

import java.util.ArrayList;

import edu.handong.csee.isel.fcminer.fpcollector.FPCollector;
import edu.handong.csee.isel.fcminer.fpcollector.subset.SuperWarning;
import edu.handong.csee.isel.fcminer.saresultminer.SAResultMiner;
import edu.handong.csee.isel.fcminer.util.CliCommand;
import edu.handong.csee.isel.fcminer.util.CliOptions;

public class Main {
    public static void main(String[] args) {
    	SAResultMiner analyzer = new SAResultMiner();
    	FPCollector fpCollector = new FPCollector();
    	CliOptions cliReader = new CliOptions();
    	
    	CliCommand command = cliReader.parseOptions(args);
    	System.out.println("----------------------SARMiner Start----------------------");
    	analyzer.run(command);
    	System.out.println("\n----------------------SAResult.csv is Generated----------------------");
    	System.out.println("                      # of Alarms: "+ analyzer.getNumOfAlarm());
    	
    	System.out.println("\n----------------------FPCollector Start----------------------");
    	ArrayList<SuperWarning> superSet = fpCollector.run(command, analyzer.getNumOfAlarm());
    	System.out.println("\n----------------------FPC_Patterns are Generated----------------------");
    	
//    	System.out.println("\n----------------------ClusterGenerator Start----------------------");
//    	ClusterGenerator cg = new ClusterGenerator();
//    	if(superSet != null)
//    		cg.cluster(superSet);    	
//    	System.out.println("\n----------------------Clusters are generated----------------------");
    }
}
