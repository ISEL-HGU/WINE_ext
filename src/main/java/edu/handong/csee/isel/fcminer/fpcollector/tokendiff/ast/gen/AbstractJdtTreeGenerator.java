package edu.handong.csee.isel.fcminer.fpcollector.tokendiff.ast.gen;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.Map;

import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.CompilationUnit;

import edu.handong.csee.isel.fcminer.fpcollector.tokendiff.datapreproc.ProcessedData;
import edu.handong.csee.isel.fcminer.fpcollector.tokendiff.datapreproc.RawData;

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
    public ProcessedData generate(Reader r, RawData rawData) throws IOException {
        ASTParser parser = ASTParser.newParser(AST.JLS11);
        parser.setKind(ASTParser.K_COMPILATION_UNIT);
        Map pOptions = JavaCore.getOptions();
        pOptions.put(JavaCore.COMPILER_COMPLIANCE, JavaCore.VERSION_11);
        pOptions.put(JavaCore.COMPILER_CODEGEN_TARGET_PLATFORM, JavaCore.VERSION_11);
        pOptions.put(JavaCore.COMPILER_SOURCE, JavaCore.VERSION_11);
        pOptions.put(JavaCore.COMPILER_DOC_COMMENT_SUPPORT, JavaCore.ENABLED);
        parser.setCompilerOptions(pOptions);
        parser.setSource(readerToCharArray(r));
        AbstractJdtVisitor v = createVisitor(rawData);
        ASTNode node = parser.createAST(null);
        
        if(node.getNodeType() == 15)
        	v.setCUnit((CompilationUnit)node);
        
        if ((node.getFlags() & ASTNode.MALFORMED) != 0) // bitwise flag to check if the node has a syntax error
            throw new SyntaxException(this, r);
        node.accept(v);
        ProcessedData pData = v.getPreprocessedData();
        pData.setCtx(v.getTreeContext());        
        return pData;
    }

    protected abstract AbstractJdtVisitor createVisitor(RawData rawData);
}
