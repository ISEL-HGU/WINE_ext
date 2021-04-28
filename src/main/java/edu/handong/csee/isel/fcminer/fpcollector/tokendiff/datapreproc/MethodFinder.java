package edu.handong.csee.isel.fcminer.fpcollector.tokendiff.datapreproc;

import java.io.IOException;
import java.util.ArrayList;

import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.MethodDeclaration;

import edu.handong.csee.isel.fcminer.fpcollector.tokendiff.ast.gen.JdtTreeGenerator;

public class MethodFinder {
	CompilationUnit cUnit;
	
	public ProcessedData findMethod(RawData rawData){
		ProcessedData pData = null;
		try {
			pData = new JdtTreeGenerator().generateFromInfo(rawData);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		return pData;
	}
	
	public int getLineNum(int startPosition){
		return cUnit.getLineNumber(startPosition);
	}
	
}