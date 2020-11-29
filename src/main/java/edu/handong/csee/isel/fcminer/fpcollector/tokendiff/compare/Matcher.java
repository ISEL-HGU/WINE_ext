package edu.handong.csee.isel.fcminer.fpcollector.tokendiff.compare;

import java.util.ArrayList;

import edu.handong.csee.isel.fcminer.fpcollector.tokendiff.ast.ITree;
import edu.handong.csee.isel.fcminer.fpcollector.tokendiff.datapreproc.Info;

public class Matcher {
	Info fixed;
	Info var;
	
	MappingStorage storage = new MappingStorage();
	
	int forIdx = -1;
	int backIdx = -1;
	int vIdx = -1;
	
	private enum Part{
		F, V, B
	}
	
	public Matcher(Info fixed, Info var) {
		this.fixed = fixed;
		this.var = var;
	}
	
	public void setForIdx(int forIdx) {
		this.forIdx = forIdx;
	}
	
	public void setBackIdx(int backIdx) {
		this.backIdx = backIdx;
	}
	
	public void setVIdx(int vIdx) {
		this.vIdx = vIdx;
	}
	
	public int getForIdx() {
		return forIdx;
	}
	
	public int getBackIdx() {
		return backIdx;
	}
	
	public int getVIdx() {
		return vIdx;
	}
	
	public void match() {
		findMatchIn(fixed.getForwardPart(), var.getForwardPart(), Part.F);
		findMatchIn(fixed.getVPart(), var.getVPart(), Part.V);
		findMatchIn(fixed.getBackwardPart(), var.getBackwardPart(), Part.B);
	}
	
	private void findMatchIn(ArrayList<ITree> fixed, ArrayList<ITree> var,Part part) {
		if(part == Part.F) {
			
		} 
		else if(part == Part.V) {
			// same type node collect
			ArrayList<ITree> fixedNodes = new ArrayList<>();
			ArrayList<ITree> varNodes = new ArrayList<>();
			
			boolean[] checkFixed = new boolean[fixed.size()];
			boolean[] checkVar = new boolean[var.size()];
			
			// compare maximum same property
			// get maximum same property as matched
		}
		else if(part == Part.B) {
			
		}
	}
	
	
	
	
}
