package edu.handong.csee.isel.fcminer.gumtree.gen.jdt.cd;

import edu.handong.csee.isel.fcminer.fpcollector.tokendiff.Info;
import edu.handong.csee.isel.fcminer.gumtree.core.gen.Register;
import edu.handong.csee.isel.fcminer.gumtree.core.gen.Registry;
import edu.handong.csee.isel.fcminer.gumtree.gen.jdt.AbstractJdtTreeGenerator;
import edu.handong.csee.isel.fcminer.gumtree.gen.jdt.AbstractJdtVisitor;

@Register(id = "java-cdjdt", accept = "\\.java$", priority = Registry.Priority.MINIMUM)
public class CdJdtTreeGenerator extends AbstractJdtTreeGenerator {
    @Override
    protected AbstractJdtVisitor createVisitor(Info info) {
        return new CdJdtVisitor(info);
    }
}
