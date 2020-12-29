package edu.handong.csee.isel.fcminer.fpcollector.pattern;

import java.util.ArrayList;

import edu.handong.csee.isel.fcminer.fpcollector.tokendiff.compare.Pair;

public class Pattern implements Comparable<Pattern>{
	//pattern --> (Frequency, Pattern)
	Pair<Integer, String> pattern;
	
	//example Code
	String code = "";
	
	//hash
	
	int hash = -1;
	
	//Abstracted Pattern in Layer 2	
	ArrayList<Pattern> patternL2 = new ArrayList<>();
	
	//Abstracted Pattern in Layer 3
	ArrayList<Pattern> patternL3 = new ArrayList<>();
	
	public Pattern(Integer cnt, String pattern, String code) {
		this.pattern = new Pair<>(cnt, pattern);
		this.code = code;
	}
	
	public Pattern(Integer cnt, String pattern, int hash) {
		this.pattern = new Pair<>(cnt, pattern);		
		this.hash = hash;
	}
	
	public void abstractL2() {		
		String[] nodes = pattern.getSecond().split(",");		
		int nodeNum = nodes.length - 1;								

		ArrayList<String> abstractPattern = new ArrayList<>();
		ArrayList<int[]> patternCombination = new ArrayList<>();
		
		//combination for abstracting
		for(int i = 1 ; i < nodeNum; i ++) {
			//nCj
			patternCombination = combination(nodeNum, i);
			
			//generate abstract pattern
			for(int[] comb : patternCombination) {				
				String tempPattern = "";
				boolean[] check = new boolean[nodeNum];				
				
				for(int j = 0; j < i; j++) {					
					for(int k = 0 ; k < nodeNum; k++) {
						if(comb[j] == k) {
							check[k] = true;							
						}
						else if(check[k] != true && comb[j] != k){
							check[k] = false;							
						}
					}										
				}
				
				for(int j = 0 ; j < nodeNum; j ++) {
					if(check[j] == true) {
						tempPattern += nodes[j] + ", ";						
					} else {
						tempPattern += "<?>" + getParentProperty(nodes[j]) + ", ";
					}
				}				
				abstractPattern.add(tempPattern);
			}
		}
		
		for(int i = 0; i < abstractPattern.size(); i ++) {
			patternL2.add(new Pattern(pattern.getFirst(), abstractPattern.get(i), abstractPattern.get(i).hashCode()));
		}		
	}
	
	public void abstractL3() {
		String[] nodes = pattern.getSecond().split(",");
		int nodeNum = nodes.length-1;								

		ArrayList<String> abstractPattern = new ArrayList<>();
		ArrayList<int[]> patternCombination = new ArrayList<>();
		
		//combination for abstracting
		for(int i = 1 ; i < nodeNum; i ++) {
			//nCj
			patternCombination = combination(nodeNum, i);
			
			//generate abstract pattern
			for(int[] comb : patternCombination) {				
				String tempPattern = "";
				boolean[] check = new boolean[nodeNum];				
				
				for(int j = 0; j < i; j++) {					
					for(int k = 0 ; k < nodeNum; k++) {
						if(comb[j] == k) {
							check[k] = true;							
						}
						else if(check[k] != true && comb[j] != k){
							check[k] = false;							
						}
					}										
				}
				
				for(int j = 0 ; j < nodeNum; j ++) {
					if(check[j] == true) {
						tempPattern += nodes[j] + ", ";						
					} else {
						tempPattern += "<?>" + "(? - ?)" + ", ";
					}
				}				
				abstractPattern.add(tempPattern);
			}
		}
		
		for(int i = 0; i < abstractPattern.size(); i ++) {
			patternL3.add(new Pattern(pattern.getFirst(), abstractPattern.get(i), abstractPattern.get(i).hashCode()));
		}
	}
	
	private String getParentProperty(String pattern) {
		String parentProperty = "";
		boolean flag = false;
		for(int i = 0 ; i < pattern.length(); i ++) {
			if(flag == false && pattern.charAt(i) == '(') {
				parentProperty += '(';
				flag = true;
			} else if(flag == true && pattern.charAt(i) == ')') {
				parentProperty += ')';
				flag = false;
			} else if(flag == true) {
				parentProperty += pattern.charAt(i);
			}
		}
		
		return parentProperty;
	}
	
	public ArrayList<int[]> combination(int n, int r) {
	    ArrayList<int[]> combinations = new ArrayList<>();
	    calculate(combinations, new int[r], 0, n-1, 0);
	    return combinations;
	}
	
	private void calculate(ArrayList<int[]> combinations, int data[], int start, int end, int index) {
	    if (index == data.length) {
	        int[] combination = data.clone();
	        combinations.add(combination);
	    } else if (start <= end) {
	        data[index] = start;
	        calculate(combinations, data, start + 1, end, index + 1);
	        calculate(combinations, data, start + 1, end, index);
	    }
	}
	
	public boolean contain(String p2) {
		
		String p1 = this.pattern.getSecond();
		String[] p1Nodes = p1.split(", ");
		String[] p2Nodes = p2.split(", ");		
		int matchCnt = 0;
		int matchIdx = -1;
		
		if(p1Nodes.length > p2Nodes.length) {
			for(int i = 0; i < p2Nodes.length; i++) {								
				int flag = 0;
				for(int j = matchIdx+1; j < p1Nodes.length; j ++) {				
					if(p2Nodes[i].equals(p1Nodes[j])) {
						matchIdx = j;
						matchCnt++;
						flag = 1;
						break;
					}
				}
				if(flag != 1) {
					break;
				}
				if(matchCnt == p2Nodes.length) return true;				
			}								
		}
		
		return false;
	}
	
	@Override
	public int compareTo(Pattern p) {
		if(this.pattern.getFirst() > p.pattern.getFirst()) {
			return -1;
		} else if (this.pattern.getFirst() < p.pattern.getFirst()) {
			return 1;		
		} else {
			return 0;
		}		
	}
	
	public ArrayList<Pattern> getPatternL2(){
		return patternL2;
	}
	
	public ArrayList<Pattern> getPatternL3(){
		return patternL3;
	}
	
	public Pair<Integer, String> getPattern() {
		return pattern;
	}
	
	public static String type2String(int type) {
		switch(type) {
			case 1: return "Anoymous_Class_Dec.";
			case 2: return "Array_access";
			case 3: return "Array_Creation";
			case 4: return "Array_init.";
			case 5: return "Array_Type";
			case 6: return "Assert_ststement";
			case 7: return "Assignment";
			case 8: return "Block";
			case 9: return "Boolean_literal";
			case 10: return "Break_statement";
			case 11: return "Cast_Exp.";
			case 12: return "Catch_Clause";
			case 13: return "Character_literal";
			case 14: return "Class_instance_Creation";
			case 15: return "Compliation_unit";
			case 16: return "Conditional_Exp.";
			case 17: return "Constructor_invocation";
			case 18: return "Continue_statement";
			case 19: return "Do_statement";
			case 20: return "Empty_statement";
			case 21: return "Exp._statement";
			case 22: return "Field_access";
			case 23: return "Field_dec.";
			case 24: return "For_statement";
			case 25: return "If_statement";
			case 26: return "Import_dec.";
			case 27: return "Infix_Exp.";
			case 28: return "Initializer";
			case 29: return "Javadoc";
			case 30: return "Labeled_statement";
			case 31: return "Method_dec.";
			case 32: return "Method_invocation";
			case 33: return "null_literal";
			case 34: return "Number_literal";
			case 35: return "Package_dec.";
			case 36: return "Parenthesized_Exp.";
			case 37: return "Postfix_Exp.";
			case 38: return "Prefix_Exp.";
			case 39: return "Primitive_TYPE";
			case 40: return "Qualified_NAME";
			case 41: return "Return_statement";
			case 42: return "Simple_Name";
			case 43: return "Simple_Type";
			case 44: return "Single_Variable_dec.";
			case 45: return "String_literal";
			case 46: return "Super_Constructor_invocation";
			case 47: return "Super_Field_access";
			case 48: return "Super_Method_invocation";
			case 49: return "Switch_Case";
			case 50: return "Switch_statement";
			case 51: return "Synchronized_statement";
			case 52: return "This_Exp.";
			case 53: return "Throw_statement";
			case 54: return "Try_statement";
			case 55: return "Type_dec.";
			case 56: return "Type_dec._statement";
			case 57: return "Type_literal";
			case 58: return "Variable_dec._Exp.";
			case 59: return "Variable_dec._fragment";
			case 60: return "Variable_dec._statement";
			case 61: return "While_statement";
			case 62: return "InstanceOf_Exp.";
			case 63: return "Line_Comment";
			case 64: return "Block_Comment";
			case 65: return "Tag_element";
			case 66: return "Text_element";
			case 67: return "Member_ref.";
			case 68: return "Method_Ref.";
			case 69: return "Method_Ref._Parameter";
			case 70: return "Enhanced_For_statement";
			case 71: return "Enum_dec.";
			case 72: return "Enum_Constant_dec.";
			case 73: return "Type_Parameter";
			case 74: return "Parameterized_Type";
			case 75: return "Qualified_Type";
			case 76: return "Wildcard_Type";
			case 77: return "Normal_Annotation";
			case 78: return "Marker_Annotation";
			case 79: return "Single_Member_Annotation";
			case 80: return "Member_Value_Pair";
			case 81: return "Annotation_Type_dec.";
			case 82: return "Annotation_Type_Member_dec.";
			case 83: return "Modifier";
			case 84: return "Union_Type";
			case 85: return "Dimension";
			case 86: return "Lambda_Exp.";
			case 87: return "Intersection_Type";
			case 88: return "Name_Qualified_Type";
			case 89: return "Creation_ref.";
			case 90: return "Exp._Method_ref.";
			case 91: return "Super_Method_ref.";
			case 92: return "Type_Method_ref.";
			case 93: return "Module_dec.";
			case 94: return "Requires_Directive";
			case 95: return "Exports_Directive";
			case 96: return "Opens_Directive";
			case 97: return "Uses_Directive";
			case 98: return "Provides_Directive";
			case 99: return "Module_MODIFIER";
		}
		return "";
	}
}
