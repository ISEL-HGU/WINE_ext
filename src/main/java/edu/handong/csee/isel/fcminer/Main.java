package edu.handong.csee.isel.fcminer;

import edu.handong.csee.isel.fcminer.fpcollector.FPCollector;
import edu.handong.csee.isel.fcminer.saresultminer.SAResultMiner;
import edu.handong.csee.isel.fcminer.util.CliCommand;
import edu.handong.csee.isel.fcminer.util.CliOptions;

public class Main {
    public static void main(String[] args) {
    	SAResultMiner analyzer = new SAResultMiner();
    	FPCollector fpCollector = new FPCollector();
    	CliOptions cliReader = new CliOptions();
    	
    	CliCommand command = cliReader.parseOptions(args);
    	analyzer.run(command);        
    	fpCollector.run(command, analyzer.getGitRepo());
    }
}
