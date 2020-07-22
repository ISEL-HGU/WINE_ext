package edu.handong.csee.isel.fcminer;

import edu.handong.csee.isel.fcminer.fpcollector.FPCollector;
import edu.handong.csee.isel.fcminer.saresultminer.SAResultMiner;

public class Main {
    public static void main(String[] args) {
    	SAResultMiner analyzer = new SAResultMiner();
    	FPCollector fpCollector = new FPCollector();
        String resultPath = analyzer.run(args[0]);        
        fpCollector.run(resultPath, analyzer.getGitRepo());
        
    }
}
