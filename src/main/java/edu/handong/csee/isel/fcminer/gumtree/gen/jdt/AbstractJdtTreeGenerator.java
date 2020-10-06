package edu.handong.csee.isel.fcminer.gumtree.gen.jdt;

import edu.handong.csee.isel.fcminer.gumtree.core.gen.SyntaxException;
import edu.handong.csee.isel.fcminer.gumtree.core.gen.TreeGenerator;
import edu.handong.csee.isel.fcminer.gumtree.core.tree.TreeContext;
import edu.handong.csee.isel.fcminer.gumtree.core.gen.TreeGenerator;
import edu.handong.csee.isel.fcminer.gumtree.core.tree.TreeContext;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.IllegalFormatException;
import java.util.Map;

public abstract class AbstractJdtTreeGenerator extends TreeGenerator {

    private static char[] readerToCharArray(Reader r) throws IOException {
        StringBuilder fileData = new StringBuilder();
        try (BufferedReader br = new BufferedReader(r)) {
            char[] buf = new char[10];
            int numRead = 0;
            while ((numRead = br.read(buf)) != -1) {
                String readData = String.valueOf(buf, 0, numRead);
                fileData.append(readData);
                buf = new char[1024];
            }
        }
        return  fileData.toString().toCharArray();
    }

    @Override
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public TreeContext generate(Reader r) throws IOException {
        ASTParser parser = ASTParser.newParser(AST.JLS9);
        parser.setKind(ASTParser.K_COMPILATION_UNIT);
        Map pOptions = JavaCore.getOptions();
        pOptions.put(JavaCore.COMPILER_COMPLIANCE, JavaCore.VERSION_11);
        pOptions.put(JavaCore.COMPILER_CODEGEN_TARGET_PLATFORM, JavaCore.VERSION_11);
        pOptions.put(JavaCore.COMPILER_SOURCE, JavaCore.VERSION_11);
        pOptions.put(JavaCore.COMPILER_DOC_COMMENT_SUPPORT, JavaCore.ENABLED);
        parser.setCompilerOptions(pOptions);
        parser.setSource(readerToCharArray(r));
        AbstractJdtVisitor v = createVisitor();
        ASTNode node = parser.createAST(null);
        if ((node.getFlags() & ASTNode.MALFORMED) != 0) // bitwise flag to check if the node has a syntax error
            throw new SyntaxException(this, r);
        node.accept(v);
        return v.getTreeContext();
    }

    protected abstract AbstractJdtVisitor createVisitor();
}
