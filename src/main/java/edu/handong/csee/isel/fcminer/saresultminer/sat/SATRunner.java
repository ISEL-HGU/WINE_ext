package edu.handong.csee.isel.fcminer.saresultminer.sat;

import edu.handong.csee.isel.fcminer.saresultminer.sat.pmd.Alarm;

import java.util.ArrayList;

public interface SATRunner {
    public void execute(String rule, String clonedPath, int cnt, String projectName);

    public String getReportPath();

    public ArrayList<Alarm> readReportFile(String path);

    public void initResult(String resultPath);

    public void writeResult(ArrayList<Alarm> alarms, String outputPath);
}
