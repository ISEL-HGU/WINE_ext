package edu.handong.csee.isel.fcminer.gumtree.core.gen;

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

import edu.handong.csee.isel.fcminer.fpcollector.gumtree.Info;
import edu.handong.csee.isel.fcminer.gumtree.core.tree.TreeContext;

@IndexSubclasses
public abstract class TreeGenerator {

    protected abstract Info generate(Reader r, Info info) throws IOException;
    
//    protected abstract TreeContext generate(Reader r, Info info) throws IOException;
//
//    public TreeContext generateFromReader(Reader r) throws IOException {
//        TreeContext ctx = generate(r);
//        ctx.validate();
//        return ctx;
//    }
    
    public Info generateFromReader(Reader r, Info info) throws IOException {
        Info updatedInfo = generate(r, info);
        updatedInfo.getCtx().validate();
        return updatedInfo;
    }

//    public TreeContext generateFromFile(String path) throws IOException {
//        return generateFromReader(Files.newBufferedReader(Paths.get(path), Charset.forName("UTF-8")));
//    }
//
//    public TreeContext generateFromFile(File file) throws IOException {
//        return generateFromReader(Files.newBufferedReader(file.toPath(), Charset.forName("UTF-8")));
//    }
//
//    public TreeContext generateFromStream(InputStream stream) throws IOException {
//        return generateFromReader(new InputStreamReader(stream, "UTF-8"));
//    }
//
//    public TreeContext generateFromString(String content) throws IOException {
//        return generateFromReader(new StringReader(content));
//    }
    
    public Info generateFromInfo(Info info) throws IOException {
    	info.sourceByLine.remove(0);
    	String src = String.join("\n", info.sourceByLine);
        return generateFromReader(new StringReader(src), info);
    }
}
