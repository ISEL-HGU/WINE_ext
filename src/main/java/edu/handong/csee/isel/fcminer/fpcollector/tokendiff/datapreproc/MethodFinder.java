package edu.handong.csee.isel.fcminer.fpcollector.tokendiff.datapreproc;

import java.io.IOException;
import java.util.ArrayList;

import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.MethodDeclaration;

import edu.handong.csee.isel.fcminer.fpcollector.tokendiff.ast.gen.JdtTreeGenerator;

public class MethodFinder {
	Info info;
	CompilationUnit cUnit;
	ArrayList<MethodDeclaration> lstMethodDeclaration = new ArrayList<>();
	
	public MethodFinder(Info info) {
		this.info = info;
	}
	
	public Info findMethod(){
		try {
			this.info = new JdtTreeGenerator().generateFromInfo(info);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		return info;
	}
	
	public int getLineNum(int startPosition){
		return cUnit.getLineNumber(startPosition);
	}
	
}
