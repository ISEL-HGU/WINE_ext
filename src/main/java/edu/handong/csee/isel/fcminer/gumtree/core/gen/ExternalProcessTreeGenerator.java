package edu.handong.csee.isel.fcminer.gumtree.core.gen;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.Files;

public abstract class ExternalProcessTreeGenerator extends TreeGenerator {

    public String readStandardOutput(Reader r) throws IOException {
        // TODO avoid recreating file if supplied reader is already a file
        File f = dumpReaderInTempFile(r);
        ProcessBuilder b = new ProcessBuilder(getCommandLine(f.getAbsolutePath()));
        b.directory(f.getParentFile());
        Process p = b.start();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream(), "UTF-8"));) {
            StringBuilder buf = new StringBuilder();
            String line = null;
            while ((line = br.readLine()) != null)
                buf.append(line + System.lineSeparator());
            p.waitFor();
            if (p.exitValue() != 0)
                throw new RuntimeException(buf.toString());
            r.close();
            p.destroy();
            return buf.toString();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            f.delete();
        }
    }

    private File dumpReaderInTempFile(Reader r) throws IOException {
        File f = File.createTempFile("gumtree", "");
        try (
                Writer w = Files.newBufferedWriter(f.toPath(), Charset.forName("UTF-8"));
        ) {
            char[] buf = new char[8192];
            while (true)
            {
                int length = r.read(buf);
                if (length < 0)
                    break;
                w.write(buf, 0, length);
            }
        }
        return f;
    }

    protected abstract String[] getCommandLine(String file);

}
