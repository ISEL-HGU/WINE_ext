package edu.handong.csee.isel.fcminer.fpcollector.gumtree;

import java.util.ArrayList;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.ThisExpression;

public class Info {
	public ArrayList<String> sourceByLine;
	public String path;
	public int start;
	public int end;
	public int startPosition;
	private MethodDeclaration violatingMethod;
	private int changedLineNum;
	private String mockClass;
	private int mockStart;
	private int mockEnd;
	private int mockStartPosition;
	
	
	public void setMockClass(String mockClass) {
		this.mockClass = mockClass;
	}
	
	public String getMockClass() {
		return mockClass;
	}
	
	public MethodDeclaration getViolatingMethod() {
		return violatingMethod;
	}
	
	public void setViolatingMethod(MethodDeclaration method) {
		this.violatingMethod = method;
	}
	
	public void setChangedLineNum(int lineNum) {
		this.changedLineNum = lineNum;
	}
	
	public int getChangedLineNum() {
		return changedLineNum;
	}

	public int getMockStart() {
		return mockStart;
	}

	public void setMockStart(int mockStart) {
		this.mockStart = mockStart;
	}

	public int getMockEnd() {
		return mockEnd;
	}

	public void setMockEnd(int mockEnd) {
		this.mockEnd = mockEnd;
	}

	public int getMockStartPosition() {
		return mockStartPosition;
	}

	public void setMockStartPosition(int mockStartPosition) {
		this.mockStartPosition = mockStartPosition;
	}
	
	
}
