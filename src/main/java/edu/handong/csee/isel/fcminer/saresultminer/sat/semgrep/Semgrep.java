package edu.handong.csee.isel.fcminer.saresultminer.sat.semgrep;

import edu.handong.csee.isel.fcminer.saresultminer.sat.SATRunner;
import edu.handong.csee.isel.fcminer.saresultminer.sat.pmd.Alarm;
import edu.handong.csee.isel.fcminer.util.OSValidator;
import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.ExecuteException;
import org.apache.commons.exec.ExecuteWatchdog;
import org.apache.commons.exec.environment.EnvironmentUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

public class Semgrep implements SATRunner {
    String reportPath = "";

    public void execute(String rule, String clonedPath, int cnt, String projectName) {
        File newDir = new File("./SemgrepReports/");
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
            ExecuteWatchdog watchdog = new ExecuteWatchdog(600000);
            executor.setWatchdog(watchdog);

            Map<String, String> env = EnvironmentUtils.getProcEnvironment();
            env.replace("LANG", "en_US.UTF-8");
            int exitValue = executor.execute(cmdLine, env);
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(-1);
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
        cmdLine.addArgument("-q");

        return cmdLine;
    }

    public ArrayList<Alarm> readReportFile(String path){
        File f = new File(path);
        ArrayList<Alarm> alarms = new ArrayList<>();

        try {
            JSONParser jsonParser = new JSONParser();
            FileReader fReader =new FileReader(f);

            Object obj = jsonParser.parse(fReader);
            JSONObject report = (JSONObject) obj;
            Object results = report.get("results");
            JSONArray resultArray = (JSONArray) results;
            for(Object resultObj : resultArray){
                JSONObject result = (JSONObject) resultObj;
                String warningPath = (String) result.get("path");
                String startLineNum = "" + ((JSONObject) result.get("start")).get("line");
                String endLineNum = "" + ((JSONObject) result.get("end")).get("line");
            }
            Alarm temp = new Alarm();
            alarms.add(temp);

            fReader.close();
        }
        catch (IOException | ParseException e) {
            e.printStackTrace();
        }

        return alarms;
    }

    public String getReportPath() {
        return reportPath;
    }
}
