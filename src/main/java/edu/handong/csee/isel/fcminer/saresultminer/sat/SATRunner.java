package edu.handong.csee.isel.fcminer.saresultminer.sat;

public interface SATRunner {
    public void execute(String rule, String clonedPath, int cnt, String projectName);

    public String getReportPath();
}
