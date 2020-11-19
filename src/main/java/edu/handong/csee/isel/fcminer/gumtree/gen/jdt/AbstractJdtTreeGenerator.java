package edu.handong.csee.isel.fcminer.gumtree.gen.jdt;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.Map;

import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.CompilationUnit;

import edu.handong.csee.isel.fcminer.fpcollector.gumtree.Info;
import edu.handong.csee.isel.fcminer.gumtree.core.gen.SyntaxException;
import edu.handong.csee.isel.fcminer.gumtree.core.gen.TreeGenerator;
import edu.handong.csee.isel.fcminer.gumtree.core.tree.TreeContext;

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

    @SuppressWarnings({ "unchecked", "rawtypes" })
    public Info generate(Reader r, Info info) throws IOException {
        ASTParser parser = ASTParser.newParser(AST.JLS11);
        parser.setKind(ASTParser.K_COMPILATION_UNIT);
        Map pOptions = JavaCore.getOptions();
        pOptions.put(JavaCore.COMPILER_COMPLIANCE, JavaCore.VERSION_11);
        pOptions.put(JavaCore.COMPILER_CODEGEN_TARGET_PLATFORM, JavaCore.VERSION_11);
        pOptions.put(JavaCore.COMPILER_SOURCE, JavaCore.VERSION_11);
        pOptions.put(JavaCore.COMPILER_DOC_COMMENT_SUPPORT, JavaCore.ENABLED);
        parser.setCompilerOptions(pOptions);
        parser.setSource(readerToCharArray(r));
        AbstractJdtVisitor v = createVisitor(info);
        ASTNode node = parser.createAST(null);
        
        if(node.getNodeType() == 15)
        	v.setCUnit((CompilationUnit)node);
        
        if ((node.getFlags() & ASTNode.MALFORMED) != 0) // bitwise flag to check if the node has a syntax error
            throw new SyntaxException(this, r);
        node.accept(v);
        info = v.getInfo();
        info.setCtx(v.getTreeContext());
        return info;
//        return v.getTreeContext();
    }

    protected abstract AbstractJdtVisitor createVisitor(Info info);
}
