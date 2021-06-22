package edu.handong.csee.isel.fcminer.saresultminer.sat.semgrep;

import edu.handong.csee.isel.fcminer.saresultminer.sat.SATRunner;
import edu.handong.csee.isel.fcminer.util.OSValidator;
import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.ExecuteException;
import org.apache.commons.exec.ExecuteWatchdog;

import java.io.File;
import java.io.IOException;

public class Semgrep implements SATRunner {
    String reportPath = "";

    public void execute(String rule, String clonedPath, int cnt, String projectName) {
        File newDir = new File("./SemgerpReports/");
        reportPath = "./SemgrepReports/" + cnt + "_" + projectName + ".json";

        if(!newDir.exists()) {
            newDir.mkdirs();
        }

        System.out.println("INFO: Semgrep Start");
        long start = System.currentTimeMillis();

        try {
            CommandLine cmdLine = setArgs(clonedPath, rule, cnt, projectName);
            DefaultExecutor executor = new DefaultExecutor();
            int[] exitValues = {0, 1, 4};

            executor.setExitValues(exitValues);
            ExecuteWatchdog watchdog = new ExecuteWatchdog(60000);
            executor.setWatchdog(watchdog);

            int exitValue = executor.execute(cmdLine);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println(-1);
        }
        long end = System.currentTimeMillis();

        System.out.println("INFO: "+ reportPath + " is Generated"+ " ("+(end-start)/1000 + " sec.)");
    }

    private CommandLine setArgs(String dirPath, String rule, int cnt, String projectName) {
        CommandLine cmdLine = new CommandLine("semgrep");

        cmdLine.addArgument("--config=" + rule);
        cmdLine.addArgument("-l");
        cmdLine.addArgument("java");
        cmdLine.addArgument(dirPath);
        cmdLine.addArgument("--json");
        cmdLine.addArgument("-o");
        cmdLine.addArgument(reportPath);

        return cmdLine;
    }

    public String getReportPath() {
        return reportPath;
    }
}
