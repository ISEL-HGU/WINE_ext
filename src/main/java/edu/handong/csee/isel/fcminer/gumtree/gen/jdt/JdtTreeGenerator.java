package edu.handong.csee.isel.fcminer.gumtree.gen.jdt;

import edu.handong.csee.isel.fcminer.gumtree.core.gen.Register;
import edu.handong.csee.isel.fcminer.gumtree.core.gen.Registry;

@Register(id = "java-jdt", accept = "\\.java$", priority = Registry.Priority.MAXIMUM)
public class JdtTreeGenerator extends AbstractJdtTreeGenerator {

    @Override
    protected AbstractJdtVisitor createVisitor() {
        return new JdtVisitor();
    }

}