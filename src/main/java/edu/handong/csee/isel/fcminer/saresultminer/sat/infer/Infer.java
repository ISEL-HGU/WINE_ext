package edu.handong.csee.isel.fcminer.saresultminer.sat.infer;

import edu.handong.csee.isel.fcminer.saresultminer.sat.SATRunner;
import edu.handong.csee.isel.fcminer.saresultminer.sat.pmd.Alarm;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;

public class Infer implements SATRunner {
    enum Status {INIT, NUMBER, DETECTION, DESCRIPTION, CODE}
    ArrayList<String> rules = new ArrayList<>();
    int detectionID = 0;

    public void execute(String rule, String clonedPath, int cnt, String projectName) {
        System.out.println("Wrong trial: Cannot execute infer in WINE");
        System.exit(-1);
    }

    public String getReportPath() {
        System.out.println("Wrong trial: Cannot execute infer in WINE");
        System.exit(-1);
        return null;
    }

    public ArrayList<Alarm> readReportFile(String path) {
        ArrayList<Alarm> alarms = new ArrayList<>();
        Status s = Status.INIT;
        File f = new File("./" + path);
        try {
            FileReader fReader =new FileReader(f);
            BufferedReader fBufReader = new BufferedReader(fReader);
            String str = "";
            String reportedPath = "";
            String sNum = "";
            String rule = "";
            String code = "";
            int idx = path.split("/").length;
            String projectName = path.split("/")[idx - 1].split("\\.")[0];
            while((str = fBufReader.readLine()) != null) {
                if(s == Status.INIT && str.matches("#[0-9]*")){
                    s = Status.NUMBER;
                } else if (s == Status.NUMBER && str.split(":").length == 4){
                    s = Status.DETECTION;
                    reportedPath = "./TargetProjects/" + projectName + "/" + str.split(":")[0];
                    sNum = str.split(":")[1];
                    rule = str.split(":")[3];
                    rule = rule.trim().replaceAll(" ", "_");
                } else if (s == Status.DETECTION){
                    s = Status.DESCRIPTION;
                } else if (s == Status.DESCRIPTION){
                    if(str.trim().startsWith(sNum)){
                        code = str.split(">", 2)[1];
                        Alarm tmp = new Alarm (reportedPath, sNum, sNum, rule, code);
                        alarms.add(tmp);
                        s = Status.CODE;
                    }
                } else if (s == Status.CODE && str.length() == 0){
                    s = Status.INIT;
                    reportedPath = "";
                    sNum = "";
                    rule = "";
                    code = "";
                }
            }
            fBufReader.close();
        }
        catch (IOException e) {
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
        String pathName = "./InferRules/";

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
}
