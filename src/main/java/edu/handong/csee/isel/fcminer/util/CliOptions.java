package edu.handong.csee.isel.fcminer.util;

import java.io.File;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

public class CliOptions {
	public enum RunState{
		SAResultMiner, FPCollector, All
	}
	
	public CliCommand parseOptions(String[] args){
		String os = OSValidator.getOS();
		CliCommand command = new CliCommand();
		CommandLineParser parser = new DefaultParser();
		Options options = new Options();	
		
		options.addOption("s", "saresult", false, "run only Static Analysis Result Miner.\n"
												+"TargetAddress.txt and Rule are needed as args.")
			   .addOption("f", "fpcollector", false, "run only False Positive candidate Collector.\n"
					   							+"SAResultMiner_Result.csv and TargetAddress.txt\n"
					   							+"are needed as an arg.")
			   .addOption("m", "fcminer", false, "run both SAResultMiner and FPCollector.\n"
					   							+"TargetAddress.txt and Rule are needed as args.")
			   .addOption("R", "rule", true, "need an argument, rule command of PMD")
			   .addOption("t", "target", true, "path of TargetAddress.txt")
			   .addOption("e", "result", true, "path of SAResult_Result.csv")
			   .addOption("p", "pmd", true, "path of PMD run file\n"
					   						+"ex) ./pmd-bin-6.25.0/bin/run.sh");
		
		try {
		    // parse the command line arguments
		    CommandLine line = parser.parse( options, args );
		 // automatically generate the help statement
		    HelpFormatter formatter = new HelpFormatter();
		    String tempPath = "";
		    if(line.hasOption("s") || line.hasOption("saresult")) {
		    	if(line.hasOption("R") && line.hasOption("t") && line.hasOption("p")) {		    		
		    		command.setRule(line.getOptionValue("R"));
		    		command.setAddressPath(line.getOptionValue("t"));
		    		String addressPath = command.getAddressPath();
		    		if(os.equals("linux") && addressPath.contains("\\")) {
		    			for(int i = 0; i < addressPath.length(); i++) {
		    				if(addressPath.charAt(i) == '\\') {
		    					tempPath += '/';
		    				} else {
		    					tempPath += addressPath.charAt(i);
		    				}
		    			}
		    			command.setAddressPath(tempPath);
		    			tempPath = "";
		    		} else if(os.equals("window") && addressPath.contains("/")) {
		    			for(int i = 0; i < addressPath.length(); i++) {
		    				if(addressPath.charAt(i) == '/') {
		    					tempPath += '\\';
		    				} else {
		    					tempPath += addressPath.charAt(i);
		    				}
		    			}
		    			command.setAddressPath(tempPath);
		    			tempPath = "";
		    		}
		    		
		    		if(isPMDFolderExists(line.getOptionValue("p")))
		    			command.setPMD(line.getOptionValue("p"));
		    		else {
		    			printHelp(formatter, options);
		    		}
		    		command.setState(RunState.SAResultMiner);
		    		return command;
		    	} else {
		    		printHelp(formatter, options);
		    	}
		    } 
		    else if(line.hasOption("f") || line.hasOption("fpcollector")) {
		    	if(line.hasOption("e") && line.hasOption("t")) {
		    		command.setResultPath(line.getOptionValue("e"));
		    		command.setAddressPath(line.getOptionValue("t"));
		    		command.setState(RunState.FPCollector);
		    		return command;
		    	} else {
		    		printHelp(formatter, options);
		    	}
		    }
		    else if(line.hasOption("m")) {
		    	if(line.hasOption("t") && line.hasOption("R")) {
		    		command.setRule(line.getOptionValue("R"));
		    		command.setAddressPath(line.getOptionValue("t"));
		    		if(isPMDFolderExists(line.getOptionValue("p")))
		    			command.setPMD(line.getOptionValue("p"));
		    		else {
		    			printHelp(formatter, options);
		    		}
		    		command.setState(RunState.All);
		    		return command;
		    	} else {
		    		printHelp(formatter, options);
		    	}
		    } 
		    else {
		    	printHelp(formatter, options);
		    }
		}
		catch( ParseException exp ) {
		    System.out.println( "Unexpected exception:" + exp.getMessage() );
		}
		return command;
	}
	
	private boolean isPMDFolderExists(String path) {
		File f = new File(path);
		return f.exists();
	}
	
	private void printHelp(HelpFormatter formatter, Options options) {
		formatter.printHelp("only SAResultMiner: ./FC-Miner -s -R <PMD Rule Context> -t <TargetAddress.txt Path> -p <pmd runfile path>\n"+
				"only FPCollector  : ./FC-Miner -f -e <SAResultMiner_Result.csv Path>\n" +
				"Both              : ./FC-Miner -m -R <PMD Rule Context> -t <TargetAddress.txt Path -p <pmd runfile path>\n\n", options);
		System.exit(-1);
	}
}
