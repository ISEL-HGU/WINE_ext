package edu.handong.csee.isel.fcminer.fpcollector.tokendiff.ast.gen;


import edu.handong.csee.isel.fcminer.fpcollector.tokendiff.datapreproc.RawData;

@Register(id = "java-jdt", accept = "\\.java$", priority = Registry.Priority.MAXIMUM)
public class JdtTreeGenerator extends AbstractJdtTreeGenerator {

    @Override
    protected AbstractJdtVisitor createVisitor(RawData rawData) {
        return new JdtVisitor(rawData);
    }

}