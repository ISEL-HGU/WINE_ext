package edu.handong.csee.isel.fcminer.saresultminer.sat.infer;

import edu.handong.csee.isel.fcminer.saresultminer.sat.SATRunner;
import edu.handong.csee.isel.fcminer.saresultminer.sat.pmd.Alarm;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class Infer implements SATRunner {
    enum Status {INIT, NUMBER, DETECTION, DESCRIPTION, CODE}
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
            while((str = fBufReader.readLine()) != null) {
                if(s == Status.INIT && str.matches("#[0-9]*")){
                    s = Status.NUMBER;
                } else if (s == Status.NUMBER && str.split(":").length == 4){
                    s = Status.DETECTION;
                    reportedPath = "TargetProjects/" + str.split(":")[0];
                    sNum = str.split(":")[1];
                    rule = str.split(":")[3];
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

    public void initResult(String resultPath) {

    }

    public void writeResult(ArrayList<Alarm> alarms, String outputPath) {

    }
}
