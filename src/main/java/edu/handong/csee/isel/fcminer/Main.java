package edu.handong.csee.isel.fcminer;

import edu.handong.csee.isel.fcminer.fpcollector.SuperWarningCollector;
import edu.handong.csee.isel.fcminer.saresultminer.SAResultMiner;
import edu.handong.csee.isel.fcminer.util.CliCommand;
import edu.handong.csee.isel.fcminer.util.CliOptions;

public class Main {
    public static void main(String[] args) {
    	SAResultMiner analyzer = new SAResultMiner();
    	SuperWarningCollector superWarningCollector = new SuperWarningCollector();
    	CliOptions cliReader = new CliOptions();
    	
    	CliCommand command = cliReader.parseOptions(args);
    	System.out.println("----------------------SARMiner Start----------------------");
    	analyzer.run(command);
    	System.out.println("\n----------------------SAResult.csv is Generated----------------------");
    	System.out.println("                      # of Alarms: "+ analyzer.getNumOfAlarm());
    	
    	System.out.println("\n----------------------FPCollector Start----------------------");
		superWarningCollector.run(command, analyzer.getNumOfAlarm());
    }
}
