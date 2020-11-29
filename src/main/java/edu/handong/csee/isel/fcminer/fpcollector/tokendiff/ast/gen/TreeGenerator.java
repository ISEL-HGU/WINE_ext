package edu.handong.csee.isel.fcminer.fpcollector.tokendiff.ast.gen;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.atteo.classindex.IndexSubclasses;

import edu.handong.csee.isel.fcminer.fpcollector.tokendiff.ast.TreeContext;
import edu.handong.csee.isel.fcminer.fpcollector.tokendiff.datapreproc.Info;

@IndexSubclasses
public abstract class TreeGenerator {

    protected abstract Info generate(Reader r, Info info) throws IOException;
    
    public Info generateFromReader(Reader r, Info info) throws IOException {
        Info updatedInfo = generate(r, info);
        updatedInfo.getCtx().validate();
        return updatedInfo;
    }
    
    public Info generateFromInfo(Info info) throws IOException {
    	info.sourceByLine.remove(0);
    	String src = String.join("\n", info.sourceByLine);
        return generateFromReader(new StringReader(src), info);
    }
}
