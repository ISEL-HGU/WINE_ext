package edu.handong.csee.isel.fcminer.gumtree.core.gen;

import edu.handong.csee.isel.fcminer.gumtree.core.tree.TreeContext;
import org.atteo.classindex.IndexSubclasses;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;

@IndexSubclasses
public abstract class TreeGenerator {

    protected abstract TreeContext generate(Reader r) throws IOException;

    public TreeContext generateFromReader(Reader r) throws IOException {
        TreeContext ctx = generate(r);
        ctx.validate();
        return ctx;
    }

    public TreeContext generateFromFile(String path) throws IOException {
        return generateFromReader(Files.newBufferedReader(Paths.get(path), Charset.forName("UTF-8")));
    }

    public TreeContext generateFromFile(File file) throws IOException {
        return generateFromReader(Files.newBufferedReader(file.toPath(), Charset.forName("UTF-8")));
    }

    public TreeContext generateFromStream(InputStream stream) throws IOException {
        return generateFromReader(new InputStreamReader(stream, "UTF-8"));
    }

    public TreeContext generateFromString(String content) throws IOException {
        return generateFromReader(new StringReader(content));
    }
}
