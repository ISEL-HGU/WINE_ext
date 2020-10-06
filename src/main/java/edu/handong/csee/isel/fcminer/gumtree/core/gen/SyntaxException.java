package edu.handong.csee.isel.fcminer.gumtree.core.gen;

import java.io.Reader;

import edu.handong.csee.isel.fcminer.gumtree.core.gen.TreeGenerator;

public class SyntaxException extends RuntimeException {

    public SyntaxException(TreeGenerator g, Reader r) {
        super(String.format("Syntax error on source code %s using generator %s", r, g));
    }

}
