package edu.handong.csee.isel.fcminer.saresultminer.sat.semgrep;

import edu.handong.csee.isel.fcminer.saresultminer.sat.SATRunner;
import edu.handong.csee.isel.fcminer.saresultminer.sat.pmd.Alarm;
import edu.handong.csee.isel.fcminer.util.OSValidator;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.ExecuteException;
import org.apache.commons.exec.ExecuteWatchdog;
import org.apache.commons.exec.environment.EnvironmentUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
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
        cmdLine.addArgument("--lang=java");
        cmdLine.addArgument("--exclude='*.js'");
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
            long startLineNum = -1;
            long endLineNum = -1;
            String warningPath = "";
            String rule = "";
            for(Object resultObj : resultArray){
                JSONObject result = (JSONObject) resultObj;
                warningPath = (String) result.get("path");
                rule = (String) result.get("check_id");
                startLineNum = (long) ((JSONObject) result.get("start")).get("line");
                endLineNum = (long) ((JSONObject) result.get("end")).get("line");
                Alarm temp = new Alarm(warningPath, startLineNum, endLineNum, rule);
                alarms.add(temp);
            }
            fReader.close();
        }
        catch (IOException | ParseException e) {
            e.printStackTrace();
        }

        return alarms;
    }

    //todo: result writer module implement

    public void initResult(String outputPath) {
        try(
                BufferedWriter writer = Files.newBufferedWriter(Paths.get(outputPath));
                CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT
                        .withHeader("Detection ID", "Rule", "Path", "Start Line Num", "End Line Num", "Code"));
        ) {
            writer.flush();
            writer.close();
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    public void writeResult(ArrayList<Alarm> alarms, String outputPath) {
        int detectionID = 0;
        try(
                BufferedWriter writer = Files.newBufferedWriter(Paths.get(outputPath), StandardOpenOption.APPEND, StandardOpenOption.CREATE);
                CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT);
        ) {
            String idx = "";
            String path = "";
            String startLineNum = "";
            String endLineNum = "";
            String code = "";
            String rule = "";

            for(Alarm alarm : alarms) {
                detectionID ++;
                idx = "" + detectionID;
                path = alarm.getDir();
                startLineNum = alarm.getStartLineNum();
                endLineNum = alarm.getEndNum();
                code = alarm.getCode();
                rule = alarm.getRule();
                csvPrinter.printRecord(idx, rule, path, startLineNum, endLineNum, code);
            }

            writer.flush();
            writer.close();
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    public String getReportPath() {
        return reportPath;
    }
}
