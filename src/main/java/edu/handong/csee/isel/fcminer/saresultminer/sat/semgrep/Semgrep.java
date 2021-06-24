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
import java.util.HashMap;
import java.util.Map;

public class Semgrep implements SATRunner {
    String reportPath = "";
    int detectionID = 0;
    ArrayList<String> rules = new ArrayList<>();

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
            String[] fullNameOfRule;
            for(Object resultObj : resultArray){
                JSONObject result = (JSONObject) resultObj;
                if(!((String)result.get("check_id")).contains("javascript")) {
                    warningPath = (String) result.get("path");
                    fullNameOfRule = ((String) result.get("check_id")).split("\\.");
                    rule = fullNameOfRule[fullNameOfRule.length - 2] + "_" + fullNameOfRule[fullNameOfRule.length - 1];
                    startLineNum = (long) ((JSONObject) result.get("start")).get("line");
                    endLineNum = (long) ((JSONObject) result.get("end")).get("line");

                    Alarm temp = new Alarm(warningPath, startLineNum, endLineNum, rule);
                    alarms.add(temp);
                }
            }
            fReader.close();
        }
        catch (IOException | ParseException e) {
            e.printStackTrace();
        }

        return alarms;
    }

    public void initResult(String outputPath) {
        try(
                BufferedWriter writer = Files.newBufferedWriter(Paths.get(outputPath));
                CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT
                        .withHeader("Detection ID", "Path", "Start Line Num", "End Line Num", "Code"));
        ) {
            writer.flush();
            writer.close();
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    public void writeResult(ArrayList<Alarm> alarms, String outputPath) {
        String pathName = "./SemgrepRules/";

        for(Alarm alarm : alarms){
            File newDir = new File(pathName);
            if(!newDir.exists()){
                newDir.mkdirs();
            }
            outputPath = pathName + alarm.getRule() + ".csv";
            if(!rules.contains(alarm.getRule())){
                rules.add(alarm.getRule());
                initResult(outputPath);
            }
            try(
                    BufferedWriter writer = Files.newBufferedWriter(Paths.get(outputPath), StandardOpenOption.APPEND, StandardOpenOption.CREATE);
                    CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT);
            ) {
                    detectionID ++;
                    csvPrinter.printRecord("" + detectionID, alarm.getDir(), alarm.getStartLineNum(), alarm.getEndNum(), alarm.getCode());
                writer.flush();
                writer.close();
            }catch(IOException e){
                e.printStackTrace();
            }
        }
    }

    public String getReportPath() {
        return reportPath;
    }
}
