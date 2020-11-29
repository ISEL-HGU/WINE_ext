package edu.handong.csee.isel.fcminer.fpcollector.tokendiff.ast.gen;

import java.io.Reader;

import edu.handong.csee.isel.fcminer.fpcollector.tokendiff.ast.gen.TreeGenerator;

public class SyntaxException extends RuntimeException {

    public SyntaxException(TreeGenerator g, Reader r) {
        super(String.format("Syntax error on source code %s using generator %s", r, g));
    }

}
