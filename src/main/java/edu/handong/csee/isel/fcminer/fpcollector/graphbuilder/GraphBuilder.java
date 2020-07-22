package edu.handong.csee.isel.fcminer.fpcollector.graphbuilder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.AbstractTypeDeclaration;
import org.eclipse.jdt.core.dom.AnnotationTypeDeclaration;
import org.eclipse.jdt.core.dom.AnnotationTypeMemberDeclaration;
import org.eclipse.jdt.core.dom.AnonymousClassDeclaration;
import org.eclipse.jdt.core.dom.ArrayAccess;
import org.eclipse.jdt.core.dom.ArrayCreation;
import org.eclipse.jdt.core.dom.ArrayInitializer;
import org.eclipse.jdt.core.dom.ArrayType;
import org.eclipse.jdt.core.dom.AssertStatement;
import org.eclipse.jdt.core.dom.Assignment;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.BlockComment;
import org.eclipse.jdt.core.dom.BooleanLiteral;
import org.eclipse.jdt.core.dom.CastExpression;
import org.eclipse.jdt.core.dom.CatchClause;
import org.eclipse.jdt.core.dom.CharacterLiteral;
import org.eclipse.jdt.core.dom.ClassInstanceCreation;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.ConditionalExpression;
import org.eclipse.jdt.core.dom.ConstructorInvocation;
import org.eclipse.jdt.core.dom.ContinueStatement;
import org.eclipse.jdt.core.dom.CreationReference;
import org.eclipse.jdt.core.dom.Dimension;
import org.eclipse.jdt.core.dom.DoStatement;
import org.eclipse.jdt.core.dom.EmptyStatement;
import org.eclipse.jdt.core.dom.EnhancedForStatement;
import org.eclipse.jdt.core.dom.EnumConstantDeclaration;
import org.eclipse.jdt.core.dom.EnumDeclaration;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.ExpressionStatement;
import org.eclipse.jdt.core.dom.FieldAccess;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.ForStatement;
import org.eclipse.jdt.core.dom.IfStatement;
import org.eclipse.jdt.core.dom.ImportDeclaration;
import org.eclipse.jdt.core.dom.InfixExpression;
import org.eclipse.jdt.core.dom.Initializer;
import org.eclipse.jdt.core.dom.InstanceofExpression;
import org.eclipse.jdt.core.dom.IntersectionType;
import org.eclipse.jdt.core.dom.Javadoc;
import org.eclipse.jdt.core.dom.LabeledStatement;
import org.eclipse.jdt.core.dom.LambdaExpression;
import org.eclipse.jdt.core.dom.LineComment;
import org.eclipse.jdt.core.dom.MarkerAnnotation;
import org.eclipse.jdt.core.dom.MemberRef;
import org.eclipse.jdt.core.dom.MemberValuePair;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.MethodRef;
import org.eclipse.jdt.core.dom.MethodRefParameter;
import org.eclipse.jdt.core.dom.Modifier;
import org.eclipse.jdt.core.dom.NameQualifiedType;
import org.eclipse.jdt.core.dom.NormalAnnotation;
import org.eclipse.jdt.core.dom.NullLiteral;
import org.eclipse.jdt.core.dom.NumberLiteral;
import org.eclipse.jdt.core.dom.PackageDeclaration;
import org.eclipse.jdt.core.dom.ParameterizedType;
import org.eclipse.jdt.core.dom.ParenthesizedExpression;
import org.eclipse.jdt.core.dom.PostfixExpression;
import org.eclipse.jdt.core.dom.PrefixExpression;
import org.eclipse.jdt.core.dom.PrimitiveType;
import org.eclipse.jdt.core.dom.QualifiedName;
import org.eclipse.jdt.core.dom.QualifiedType;
import org.eclipse.jdt.core.dom.ReturnStatement;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.SimpleType;
import org.eclipse.jdt.core.dom.SingleMemberAnnotation;
import org.eclipse.jdt.core.dom.SingleVariableDeclaration;
import org.eclipse.jdt.core.dom.StringLiteral;
import org.eclipse.jdt.core.dom.SuperConstructorInvocation;
import org.eclipse.jdt.core.dom.SuperFieldAccess;
import org.eclipse.jdt.core.dom.SuperMethodInvocation;
import org.eclipse.jdt.core.dom.SuperMethodReference;
import org.eclipse.jdt.core.dom.SwitchCase;
import org.eclipse.jdt.core.dom.SwitchExpression;
import org.eclipse.jdt.core.dom.SwitchStatement;
import org.eclipse.jdt.core.dom.SynchronizedStatement;
import org.eclipse.jdt.core.dom.TagElement;
import org.eclipse.jdt.core.dom.TextElement;
import org.eclipse.jdt.core.dom.ThisExpression;
import org.eclipse.jdt.core.dom.ThrowStatement;
import org.eclipse.jdt.core.dom.TryStatement;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.core.dom.TypeDeclarationStatement;
import org.eclipse.jdt.core.dom.TypeLiteral;
import org.eclipse.jdt.core.dom.TypeMethodReference;
import org.eclipse.jdt.core.dom.TypeParameter;
import org.eclipse.jdt.core.dom.UnionType;
import org.eclipse.jdt.core.dom.VariableDeclaration;
import org.eclipse.jdt.core.dom.VariableDeclarationExpression;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;
import org.eclipse.jdt.core.dom.VariableDeclarationStatement;
import org.eclipse.jdt.core.dom.WhileStatement;
import org.eclipse.jdt.core.dom.WildcardType;

public class GraphBuilder {
	CompilationUnit cUnit;
	Info info;
	public ControlNode root;
	int methodStart = 0;
	int methodEnd = 0;
	int blockStart = 0;
	int blockEnd = 0;			
	int level = 0;
	
	ArrayList<ASTNode> lstViolatedVariables = new ArrayList<>();
	ArrayList<String> lstVariableDeclaration = new ArrayList<>();
	ArrayList<String> lstFieldMemberDeclaration = new ArrayList<>();
	ArrayList<ASTNode> lstViolatedField = new ArrayList<>();
	ArrayList<ASTNode> lstLevelNode = new ArrayList<ASTNode>();
	ArrayList<Boolean> lstUseVar = new ArrayList<Boolean>();
	ArrayList<ASTNode> lstViolatedStringNode = new ArrayList<>();
	
	PackageDeclaration pkgDeclaration;

	public GraphBuilder(Info info){
			this.info = info;
	}
	
	public void run(){
		ASTParser parser = ASTParser.newParser(AST.JLS12);

		parser.setKind(ASTParser.K_COMPILATION_UNIT);
		char[] content = String.join("\n", info.sourceByLine).toCharArray();
		parser.setSource(content);
		//parser.setUnitName("temp.java");
		Map<String, String> options = JavaCore.getOptions();
		options.put(JavaCore.COMPILER_COMPLIANCE, JavaCore.VERSION_1_8);
		options.put(JavaCore.COMPILER_CODEGEN_TARGET_PLATFORM,
				JavaCore.VERSION_1_7);
		options.put(JavaCore.COMPILER_SOURCE, JavaCore.VERSION_1_8);
		String[] sources = {};
		String[] classPaths = {};
		parser.setEnvironment(classPaths, sources, null, true);
		parser.setResolveBindings(false);
		parser.setCompilerOptions(options);
		parser.setStatementsRecovery(true);

		try {
			final CompilationUnit unit = (CompilationUnit) parser.createAST(null);
			cUnit = unit;

			// Process the main body
			try {
				unit.accept(new ASTVisitor() {
					
					public boolean visit(AnnotationTypeDeclaration node) {
						if(methodStart == 0 && methodEnd == 0) {
							ControlNode n = new ControlNode(node, ControlState.S, level, info);
							root = n;				
							root.parent = null;
						}
						return super.visit(node);
					}
					
					public boolean visit(TypeDeclaration node) {
						if(methodStart == 0 && methodEnd == 0) {
							ControlNode n = new ControlNode(node, ControlState.S, level, info);
							root = n;				
							root.parent = null;
						}
						return super.visit(node);
					}
					
					public boolean visit(EnumDeclaration node) {
						if(methodStart == 0 && methodEnd == 0) {
							ControlNode n = new ControlNode(node, ControlState.S, level, info);
							root = n;				
							root.parent = null;
						}
						return super.visit(node);
					}
					
					public boolean visit(MethodDeclaration node) {											
//						System.out.println("level : " + level);					
//						checkScope(node);
						if(methodStart == 0 && methodEnd == 0) {
							if(info.varNodes.size() > 0) {
								if(violatedNodeisIn(node, info.varNodes.get(0))){
									methodStart = node.getStartPosition();
									methodEnd = node.getStartPosition() + node.getLength();
									ControlNode n = new ControlNode(node, ControlState.S, level, info);
									n.parent = root;
									root.nexts.add(n);
									root = n;
								}
							} else if(info.fieldNodes.size() > 0) {
								if(violatedNodeisIn(node, info.fieldNodes.get(0))){
									methodStart = node.getStartPosition();
									methodEnd = node.getStartPosition() + node.getLength();
									ControlNode n = new ControlNode(node, ControlState.S, level, info);
									n.parent = root;
									root.nexts.add(n);
									root = n;
								}
							} else if (info.fieldNodes.size() > 0 && info.varNodes.size() > 0) {
								if(violatedNodeisIn(node, info.fieldNodes.get(0)) && violatedNodeisIn(node, info.varNodes.get(0))) {
									methodStart = node.getStartPosition();
									methodEnd = node.getStartPosition() + node.getLength();
									ControlNode n = new ControlNode(node, ControlState.S, level, info);
									n.parent = root;
									root.nexts.add(n);
									root = n;
								}
							}																						
						}
						return super.visit(node);												
					}										
					
					public boolean visit(Block node) {
						if(node.getParent() instanceof Initializer) {
							if(blockStart == 0 && blockEnd == 0) {
								if(info.varNodes.size() > 0) {
									if(violatedNodeisIn(node, info.varNodes.get(0))){
										blockStart = node.getStartPosition();
										blockEnd = node.getStartPosition() + node.getLength();
										ControlNode n = new ControlNode(node, ControlState.S, level, info);
										n.parent = root;
										root.nexts.add(n);
										root = n;
									}
								} else if(info.fieldNodes.size() > 0) {
									if(violatedNodeisIn(node, info.fieldNodes.get(0))){
										methodStart = node.getStartPosition();
										methodEnd = node.getStartPosition() + node.getLength();
										ControlNode n = new ControlNode(node, ControlState.S, level, info);
										n.parent = root;
										root.nexts.add(n);
										root = n;
									}
								} else if (info.fieldNodes.size() > 0 && info.varNodes.size() > 0) {
									if(violatedNodeisIn(node, info.fieldNodes.get(0)) && violatedNodeisIn(node, info.varNodes.get(0))) {
										methodStart = node.getStartPosition();
										methodEnd = node.getStartPosition() + node.getLength();
										ControlNode n = new ControlNode(node, ControlState.S, level, info);
										n.parent = root;
										root.nexts.add(n);
										root = n;
									}
								}							
																
							}
						}							
						
						return super.visit(node);
					}
					
					public boolean visit(SimpleName node) {
						//System.out.println("level : " + level + ", node : " + node.getClass().getSimpleName() + ", isDefine : " + isDefine  + ", isScope : " + isScope);
						
						if (isNodeInMethodOrBlock(node)) {
							if (info.varNames.contains(node.getIdentifier())){
								DataNode n = new DataNode(node, level, info);
								
								if(isD(node) == VarState.D)
									n.setState(VarState.D);
								else if(isD(node) == VarState.DI)
									n.setState(VarState.DI);
								else if(isD(node) == VarState.Ref)
									n.setState(VarState.Ref);
								else if(isD(node) == VarState.DIN)
									n.setState(VarState.DIN);
								else if(isD(node) == VarState.Ass)
									n.setState(VarState.Ass);
								else if(isD(node) == VarState.NAss)
									n.setState(VarState.NAss);
								
								if(checkType(node) == VarState.ArrIdxC)
									n.setType(VarState.ArrIdxC);
								else if(checkType(node) == VarState.ArrIdxF)
									n.setType(VarState.ArrIdxF);
								else if(checkType(node) == VarState.NArr)
									n.setType(VarState.NArr);
								
								if(isInCondition(node)) {
									n.setInCondition(VarState.I);
								} else {
									n.setInCondition(VarState.O);
								}
								
								if(isInAnnotation(node)) {
									n.setInAnnotation(VarState.IA);
								} else {
									n.setInAnnotation(VarState.NA);
								}
								
								n.setFrom(getFrom(node));
								
								root.nexts.add(n);															
							}
							else if (info.fieldNames.contains(node.getIdentifier())/*&& getLineNum(node.getStartPosition()) >= Integer.parseInt(info.start)*/){
								DataNode n = new DataNode(node, level, info);
								
								if(isD(node) == VarState.D)
									n.setState(VarState.FD);
								else if(isD(node) == VarState.DI)
									n.setState(VarState.FDI);
								else if(isD(node) == VarState.Ref)
									n.setState(VarState.FRef);
								else if(isD(node) == VarState.DIN)
									n.setState(VarState.FDIN);
								else if(isD(node) == VarState.Ass)
									n.setState(VarState.FAss);
								else if(isD(node) == VarState.NAss)
									n.setState(VarState.FNAss);
								
								if(checkType(node) == VarState.ArrIdxC)
									n.setType(VarState.ArrIdxC);
								else if(checkType(node) == VarState.ArrIdxF)
									n.setType(VarState.ArrIdxF);
								else if(checkType(node) == VarState.NArr)
									n.setType(VarState.NArr);
								
								if(isInCondition(node)) {
									n.setInCondition(VarState.I);
								} else {
									n.setInCondition(VarState.O);
								}				
								
								if(isInAnnotation(node)) {
									n.setInAnnotation(VarState.IA);
								} else {
									n.setInAnnotation(VarState.NA);
								}
								
								n.setFrom(getFrom(node));
								
								root.nexts.add(n);															
							}
						}

						return super.visit(node);
					}
					
					public boolean visit(ThisExpression node) {
						//System.out.println("level : " + level + ", node : " + node.getClass().getSimpleName() + ", isDefine : " + isDefine  + ", isScope : " + isScope);
						if(node.getQualifier() != null) {
							return super.visit(node);
						}
						if (isNodeInMethodOrBlock(node)) {																					
							if (info.fieldNames.contains("this")&& getLineNum(node.getStartPosition()) >= info.start 
									&& getLineNum(node.getStartPosition() + node.getLength()) <= info.end){
								DataNode n = new DataNode(node, level, info);
								
								if(isD(node) == VarState.D)
									n.setState(VarState.FD);
								else if(isD(node) == VarState.DI)
									n.setState(VarState.FDI);
								else if(isD(node) == VarState.Ref)
									n.setState(VarState.FRef);
								else if(isD(node) == VarState.DIN)
									n.setState(VarState.FDIN);
								else if(isD(node) == VarState.Ass)
									n.setState(VarState.FAss);
								else if(isD(node) == VarState.NAss)
									n.setState(VarState.FNAss);
								
								if(checkType(node) == VarState.ArrIdxC)
									n.setType(VarState.ArrIdxC);
								else if(checkType(node) == VarState.ArrIdxF)
									n.setType(VarState.ArrIdxF);
								else if(checkType(node) == VarState.NArr)
									n.setType(VarState.NArr);
								
								if(isInCondition(node)) {
									n.setInCondition(VarState.I);
								} else {
									n.setInCondition(VarState.O);
								}
								
								if(isInAnnotation(node)) {
									n.setInAnnotation(VarState.IA);
								} else {
									n.setInAnnotation(VarState.NA);
								}
								
								n.setFrom(getFrom(node));
								
								root.nexts.add(n);																
							}
						}

						return super.visit(node);
					}
					
					public boolean visit(StringLiteral node) {
						//System.out.println("level : " + level + ", node : " + node.getClass().getSimpleName() + ", isDefine : " + isDefine  + ", isScope : " + isScope);

						if (isNodeInMethodOrBlock(node)) {																					
							if (info.varNames.contains(node.getLiteralValue())){
								DataNode n = new DataNode(node, level, info);																																

								n.setType(VarState.Str);								
								
								if(isD(node) == VarState.D)
									n.setState(VarState.D);
								else if(isD(node) == VarState.DI)
									n.setState(VarState.DI);
								else if(isD(node) == VarState.Ref)
									n.setState(VarState.Ref);
								else if(isD(node) == VarState.DIN)
									n.setState(VarState.DIN);
								else if(isD(node) == VarState.Ass)
									n.setState(VarState.Ass);
								else if(isD(node) == VarState.NAss)
									n.setState(VarState.NAss);
								
								if(checkType(node) == VarState.ArrIdxC)
									n.setType(VarState.ArrIdxC);
								else if(checkType(node) == VarState.ArrIdxF)
									n.setType(VarState.ArrIdxF);
								else if(checkType(node) == VarState.NArr)
									n.setType(VarState.NArr);
								
								if(isInCondition(node)) {
									n.setInCondition(VarState.I);
								} else {
									n.setInCondition(VarState.O);
								}
								if(isInAnnotation(node)) {
									n.setInAnnotation(VarState.IA);
								} else {
									n.setInAnnotation(VarState.NA);
								}
								
								n.setFrom(getFrom(node));

								root.nexts.add(n);																
							}
						}

						return super.visit(node);
					}
					
					public boolean visit(DoStatement node) {
						if (isNodeInMethodOrBlock(node)) {
							level ++;
							//System.out.println("level : " + level + ", node : " + node.getClass().getSimpleName() + ", isDefine : " + isDefine  + ", isScope : " + isScope);
							ControlNode n = new ControlNode(node, ControlState.M, level, info);
							n.setProperty(ControlState.L);
							n.parent = root;
							root.nexts.add(n);
							root = n;							
						}
						
						return super.visit(node);
					}
					
					public void endVisit(DoStatement node) {
						if (isNodeInMethodOrBlock(node)) {
							//System.out.println("level : " + level + ", Endnode : " + node.getClass().getSimpleName() + ", isDefine : " + isDefine  + ", isScope : " + isScope);
							level --;							
							root = root.parent;														
						}
						//System.out.println("level : " + level + ", Endnode : " + node.getClass().getSimpleName() + ", isDefine : " + isDefine  + ", isScope : " + isScope);
					}

					public boolean visit(IfStatement node) {
						if (isNodeInMethodOrBlock(node)) {
							level ++;
							ControlNode n = new ControlNode(node, ControlState.M, level, info);							
							//System.out.println("level : " + level + ", node : " + node.getClass().getSimpleName() + ", isDefine : " + isDefine  + ", isScope : " + isScope);
							
							n.setProperty(ControlState.C);
							n.parent = root;
							root.nexts.add(n);
							root = n;	
						}
						
//						if (isDefine) {
//							ControlNode n = new ControlNode(node, ControlState.M, level);
//							level ++;
//							System.out.println("level : " + level + ", node : " + node.getClass().getSimpleName() + ", isDefine : " + isDefine  + ", isScope : " + isScope);
//							
//							n.setProperty(ControlState.C);
//							n.parent = root;
//							root = n;
//							if (lstUseVar.size() <= level)
//								lstUseVar.add(false);
//							else
//								lstUseVar.set(level, false);
//						}
						
						return super.visit(node);
					}
					
					public void endVisit(IfStatement node) {
						if (isNodeInMethodOrBlock(node)) {
							//System.out.println("level : " + level + ", Endnode : " + node.getClass().getSimpleName() + ", isDefine : " + isDefine  + ", isScope : " + isScope);
							level --;
							root = root.parent;
						} 
						//else System.out.println("level : " + level + ", Endnode : " + node.getClass().getSimpleName() + ", isDefine : " + isDefine  + ", isScope : " + isScope);
					}
					
					public boolean visit(ConditionalExpression node) {
						if (isNodeInMethodOrBlock(node)) {
							level ++;
							//System.out.println("level : " + level + ", node : " + node.getClass().getSimpleName() + ", isDefine : " + isDefine  + ", isScope : " + isScope);
							ControlNode n = new ControlNode(node, ControlState.M, level, info);
							n.setProperty(ControlState.C);
							n.parent = root;
							root.nexts.add(n);
							root = n;							
						}
						
						return super.visit(node);
					}
					
					public void endVisit(ConditionalExpression node) {
						if (isNodeInMethodOrBlock(node)) {
							//System.out.println("level : " + level + ", Endnode : " + node.getClass().getSimpleName() + ", isDefine : " + isDefine  + ", isScope : " + isScope);
							level --;
							root = root.parent;
						} 
						//else System.out.println("level : " + level + ", Endnode : " + node.getClass().getSimpleName() + ", isDefine : " + isDefine  + ", isScope : " + isScope);
					}																																																																	
					
					public boolean visit(ForStatement node) {
						if (isNodeInMethodOrBlock(node)) {
							level ++;
							//System.out.println("level : " + level + ", node : " + node.getClass().getSimpleName() + ", isDefine : " + isDefine  + ", isScope : " + isScope);
							ControlNode n = new ControlNode(node, ControlState.M, level, info);
							n.setProperty(ControlState.L);
							n.parent = root;
							root.nexts.add(n);
							root = n;							
						}
						
						return super.visit(node);
					}
					
					public void endVisit(ForStatement node) {
						if (isNodeInMethodOrBlock(node)) {
							//System.out.println("level : " + level + ", Endnode : " + node.getClass().getSimpleName() + ", isDefine : " + isDefine  + ", isScope : " + isScope);
							level --;
							root = root.parent;
						} 
							//else System.out.println("level : " + level + ", Endnode : " + node.getClass().getSimpleName() + ", isDefine : " + isDefine  + ", isScope : " + isScope);
					}
					
					public boolean visit(WhileStatement node) {
						if (isNodeInMethodOrBlock(node)) {
							level ++;
							//System.out.println("level : " + level + ", node : " + node.getClass().getSimpleName() + ", isDefine : " + isDefine  + ", isScope : " + isScope);
							ControlNode n = new ControlNode(node, ControlState.M, level, info);
							n.setProperty(ControlState.L);
							n.parent = root;
							root.nexts.add(n);
							root = n;							
						}
						
						return super.visit(node);
					}
					
					public void endVisit(WhileStatement node) {
						if (isNodeInMethodOrBlock(node)) {
							//System.out.println("level : " + level + ", Endnode : " + node.getClass().getSimpleName() + ", isDefine : " + isDefine  + ", isScope : " + isScope);
							level --;
							root = root.parent;
						} 						
							//else System.out.println("level : " + level + ", Endnode : " + node.getClass().getSimpleName() + ", isDefine : " + isDefine  + ", isScope : " + isScope);
					}
					
					public boolean visit(EnhancedForStatement node) {
						if (isNodeInMethodOrBlock(node)) {
							level ++;
							//System.out.println("level : " + level + ", node : " + node.getClass().getSimpleName() + ", isDefine : " + isDefine  + ", isScope : " + isScope);
							ControlNode n = new ControlNode(node, ControlState.M, level, info);
							n.setProperty(ControlState.L);
							n.parent = root;
							root.nexts.add(n);
							root = n;							
						}
						
						return super.visit(node);
					}
					
					public void endVisit(EnhancedForStatement node) {
						if (isNodeInMethodOrBlock(node)) {
							//System.out.println("level : " + level + ", Endnode : " + node.getClass().getSimpleName() + ", isDefine : " + isDefine  + ", isScope : " + isScope);
							level --;
							root = root.parent;
						} 
						
							//else System.out.println("level : " + level + ", Endnode : " + node.getClass().getSimpleName() + ", isDefine : " + isDefine  + ", isScope : " + isScope);
					}
					
					public boolean visit(TryStatement node) {
						if (isNodeInMethodOrBlock(node)) {
							level ++;
							//System.out.println("level : " + level + ", node : " + node.getClass().getSimpleName() + ", isDefine : " + isDefine  + ", isScope : " + isScope);
							ControlNode n = new ControlNode(node, ControlState.M, level, info);
							n.setProperty(ControlState.C);
							n.parent = root;
							root.nexts.add(n);
							root = n;							
						}
						
						return super.visit(node);
					}
					
					public void endVisit(TryStatement node) {
						if (isNodeInMethodOrBlock(node)) {
							//System.out.println("level : " + level + ", Endnode : " + node.getClass().getSimpleName() + ", isDefine : " + isDefine  + ", isScope : " + isScope);
							level --;
							root = root.parent;
						} 
							//else System.out.println("level : " + level + ", Endnode : " + node.getClass().getSimpleName() + ", isDefine : " + isDefine  + ", isScope : " + isScope);
					}
					
					public boolean visit(CatchClause node) {
						if (isNodeInMethodOrBlock(node)) {
							level ++;
							//System.out.println("level : " + level + ", node : " + node.getClass().getSimpleName() + ", isDefine : " + isDefine  + ", isScope : " + isScope);
							ControlNode n = new ControlNode(node, ControlState.M, level, info);
							n.setProperty(ControlState.C);
							n.parent = root;
							root.nexts.add(n);
							root = n;							
						}
						
						return super.visit(node);
					}
					
					public void endVisit(CatchClause node) {
						if (isNodeInMethodOrBlock(node)) {
							//System.out.println("level : " + level + ", Endnode : " + node.getClass().getSimpleName() + ", isDefine : " + isDefine  + ", isScope : " + isScope);
							level --;
							root = root.parent;
						} 
							////else System.out.println("level : " + level + ", Endnode : " + node.getClass().getSimpleName() + ", isDefine : " + isDefine  + ", isScope : " + isScope);
					}
					
					public boolean visit(SwitchStatement node) {
						if (isNodeInMethodOrBlock(node)) {
							level ++;
							//System.out.println("level : " + level + ", node : " + node.getClass().getSimpleName() + ", isDefine : " + isDefine  + ", isScope : " + isScope);
							ControlNode n = new ControlNode(node, ControlState.M, level, info);
							n.setProperty(ControlState.C);
							n.parent = root;
							root.nexts.add(n);
							root = n;							
						}
						
						return super.visit(node);
					}
					
					public void endVisit(SwitchStatement node) {
						if (isNodeInMethodOrBlock(node)) {
							//System.out.println("level : " + level + ", Endnode : " + node.getClass().getSimpleName() + ", isDefine : " + isDefine  + ", isScope : " + isScope);
							level --;
							root = root.parent;
						} 
							//else System.out.println("level : " + level + ", Endnode : " + node.getClass().getSimpleName() + ", isDefine : " + isDefine  + ", isScope : " + isScope);
					}
					
					public boolean visit(ReturnStatement node) {						
						if (isNodeInMethodOrBlock(node)) {
							level ++;
							//System.out.println("level : " + level + ", node : " + node.getClass().getSimpleName() + ", isDefine : " + isDefine  + ", isScope : " + isScope);
							ControlNode n = new ControlNode(node, ControlState.M, level, info);
							n.setProperty(ControlState.T);
							n.parent = root;
							root.nexts.add(n);
							root = n;							
						}
						
						return super.visit(node);
					}
					
					public void endVisit(ReturnStatement node) {
						
						if (isNodeInMethodOrBlock(node)) {
							//System.out.println("level : " + level + ", Endnode : " + node.getClass().getSimpleName() + ", isDefine : " + isDefine  + ", isScope : " + isScope);
							level --;
							root = root.parent;
						} 
							//else System.out.println("level : " + level + ", Endnode : " + node.getClass().getSimpleName() + ", isDefine : " + isDefine  + ", isScope : " + isScope);
					}
					
					public boolean visit(ThrowStatement node) {
						if (isNodeInMethodOrBlock(node)) {
							level ++;
							//System.out.println("level : " + level + ", node : " + node.getClass().getSimpleName() + ", isDefine : " + isDefine  + ", isScope : " + isScope);
							ControlNode n = new ControlNode(node, ControlState.M, level, info);
							n.setProperty(ControlState.T);
							n.parent = root;
							root.nexts.add(n);
							root = n;							
						}
						
						return super.visit(node);
					}
					
					public void endVisit(ThrowStatement node) {
						
						if (isNodeInMethodOrBlock(node)) {
							//System.out.println("level : " + level + ", Endnode : " + node.getClass().getSimpleName() + ", isDefine : " + isDefine  + ", isScope : " + isScope);
							level --;
							root = root.parent;
						} 
							//else System.out.println("level : " + level + ", Endnode : " + node.getClass().getSimpleName() + ", isDefine : " + isDefine  + ", isScope : " + isScope);
					}
					
					public boolean visit(final Assignment node) {
						return super.visit(node);
					}
					
					public boolean visit(final VariableDeclarationFragment node) {
						return super.visit(node);
					}
					
					public boolean visit(MethodInvocation node) {
						return super.visit(node);
					}									

					public boolean visit(final FieldDeclaration node) {
						return super.visit(node);
					}
					
					public boolean visit(final SingleVariableDeclaration node) {
						return super.visit(node);
					}
					
					public boolean visit(final ClassInstanceCreation node) {
						return super.visit(node);
					}
					
					public boolean visit(final FieldAccess node) {
						return super.visit(node);
					}

					public boolean visit(InfixExpression node) {
						return super.visit(node);
					}
					
					public boolean visit(final ImportDeclaration node) {
						return super.visit(node);
					}
					
					public boolean visit(final PackageDeclaration node) {
						pkgDeclaration = node;
						return super.visit(node);
					}
					
					public boolean visit(final AnonymousClassDeclaration node) {
						//Log.info("AnonymousClassDeclaration");
						//Log.info(node);
						return super.visit(node);
					}
					
					//Expression ? Expression : Expression
//					public boolean visit(final ConditionalExpression node) {
//						lstConditionalExpression.add(node);
//						return super.visit(node);
//					}

					public boolean visit(EnumConstantDeclaration node) {
//						list.add(node.getName().toString());
						return super.visit(node);
					}

					public boolean visit(AssertStatement node) {
//						list.add("AssertStatement");
						return super.visit(node);
					} 
					public boolean visit(ContinueStatement node) {
//						list.add("ContinueStatement");
						return super.visit(node);
					}

					public boolean visit(SwitchCase node) {
//						list.add("SwitchCase");
						return super.visit(node);
					}
					public boolean visit(SynchronizedStatement node) {
//						list.add("SynchronizedStatement");
						return super.visit(node);
					}

					public boolean visit(final ExpressionStatement node) {

						return super.visit(node);
					}

					public boolean visit(final AnnotationTypeMemberDeclaration node) {
						//Log.info("AnnotationTypeMemberDeclaration");
						//Log.info(node);
						return super.visit(node);
					}


					public boolean visit(final ArrayAccess node) {
						//Log.info("ArrayAccess");
						//Log.info(node);
						return super.visit(node);
					}

					public boolean visit(final ArrayCreation node) {
						//Log.info("ArrayCreation");
						//Log.info(node);
						return super.visit(node);
					}

					public boolean visit(final ArrayInitializer node) {
						//Log.info("ArrayInitializer");
						//Log.info(node);
						return super.visit(node);
					}

					public boolean visit(final ArrayType node) {
						//Log.info("ArrayType");
						//Log.info(node);
						return super.visit(node);
					}


					public boolean visit(final BlockComment node) {
						//Log.info("BlockComment");
						//Log.info(node);
						return super.visit(node);
					}

					public boolean visit(final BooleanLiteral node) {
						//Log.info("BooleanLiteral");
						//Log.info(node);
						return super.visit(node);
					}

					public boolean visit(final CastExpression node) {
						//Log.info("CastExpression");
						//Log.info(node);
						return super.visit(node);
					}

					public boolean visit(final CharacterLiteral node) {
						//Log.info("CharacterLiteral");
						//Log.info(node);
						return super.visit(node);
					}



					public boolean visit(final CompilationUnit node) {
						//Log.info("CompilationUnit");
						//Log.info(node);
						return super.visit(node);
					}

					

					public boolean visit(final ConstructorInvocation node) {
						//Log.info("ConstructorInvocation");
						//Log.info(node);
						return super.visit(node);
					}

					public boolean visit(final CreationReference node) {
						//Log.info("CreationReference");
						//Log.info(node);
						return super.visit(node);
					}

					public boolean visit(final Dimension node) {
						//Log.info("Dimension");
						//Log.info(node);
						return super.visit(node);
					}

					public boolean visit(final EmptyStatement node) {
						//Log.info("EmptyStatement");
						//Log.info(node);
						return super.visit(node);
					}

				public boolean visit(final Initializer node) {
					//Log.info("Initializer");
					//Log.info(node);
					return super.visit(node);
				}

				public boolean visit(final InstanceofExpression node) {
					//Log.info("InstanceofExpression");
					//Log.info(node);
					return super.visit(node);
				}
				public boolean visit(final IntersectionType node) {
					//Log.info("IntersectionType");
					//Log.info(node);
					return super.visit(node);
				}
				public boolean visit(final Javadoc node) {
					//Log.info("Javadoc");
					//Log.info(node);
					return super.visit(node);
				}

				public boolean visit(final LabeledStatement node) {
					//Log.info("LabeledStatement");
					//Log.info(node);
					return super.visit(node);
				}

				public boolean visit(final LambdaExpression node) {
					//Log.info("LambdaExpression");
					//Log.info(node);
					return super.visit(node);
				}

				public boolean visit(final LineComment node) {
					//Log.info("LineComment");
					//Log.info(node);
					return super.visit(node);
				}
				public boolean visit(final MarkerAnnotation node) {
					//Log.info("MarkerAnnotation");
					//Log.info(node);
					return super.visit(node);
				}
				public boolean visit(final MemberRef node) {
					//Log.info("MemberRef");
					//Log.info(node);
					return super.visit(node);
				}

				public boolean visit(final MemberValuePair node) {
					//Log.info("MemberValuePair");
					//Log.info(node);
					return super.visit(node);
				}
				public boolean visit(final MethodRef node) {
					//Log.info("MethodRef");
					//Log.info(node);
					return super.visit(node);
				}
				public boolean visit(final MethodRefParameter node) {
					//Log.info("MethodRefParameter");
					//Log.info(node);
					return super.visit(node);
				}
				public boolean visit(final Modifier node) {
					//Log.info("Modifier");
					//Log.info(node);
					return super.visit(node);
				}
				public boolean visit(final NameQualifiedType node) {
					//Log.info("NameQualifiedType");
					//Log.info(node);
					return super.visit(node);
				}

				public boolean visit(final NormalAnnotation node) {
					//Log.info("NormalAnnotation");
					//Log.info(node);
					return super.visit(node);
				}
				public boolean visit(final NullLiteral node) {
					//Log.info("NullLiteral");
					//Log.info(node);
					return super.visit(node);
				}
				public boolean visit(final NumberLiteral node) {
					//Log.info("NumberLiteral");
					//Log.info(node);
					return super.visit(node);
				}

				public boolean visit(final ParameterizedType node) {
					//Log.info("ParameterizedType");
					//Log.info(node);
					return super.visit(node);
				}
				public boolean visit(final ParenthesizedExpression node) {
					//Log.info("ParenthesizedExpression");
					//Log.info(node);
					return super.visit(node);
				}
				public boolean visit(final PostfixExpression node) {
					//Log.info("PostfixExpression");
					//Log.info(node);
					return super.visit(node);
				}
				public boolean visit(final PrefixExpression node) {
					//Log.info("PrefixExpression");
					//Log.info(node);
					return super.visit(node);
				}
				public boolean visit(final PrimitiveType node) {
					//Log.info("PrimitiveType");
					//Log.info(node);
					return super.visit(node);
				}
				public boolean visit(final QualifiedName node) {
					//Log.info("QualifiedName");
					//Log.info(node);
					return super.visit(node);
				}

				public boolean visit(final QualifiedType node) {
					//Log.info("QualifiedType");
					//Log.info(node);
					return super.visit(node);
				}

				public boolean visit(final SimpleType node) {
					//Log.info("SimpleType");
					//Log.info(node);
					return super.visit(node);
				}
				public boolean visit(final SingleMemberAnnotation node) {
					//Log.info("SingleMemberAnnotation");
					//Log.info(node);
					return super.visit(node);
				}
				
				public boolean visit(final SuperConstructorInvocation node) {
					//Log.info("SuperConstructorInvocation");
					//Log.info(node);
					return super.visit(node);
				}
				public boolean visit(final SuperFieldAccess node) {
					//Log.info("SuperFieldAccess");
					//Log.info(node);
					return super.visit(node);
				}
				public boolean visit(final SuperMethodInvocation node) {
					//Log.info("SuperMethodInvocation");
					//Log.info(node);
					return super.visit(node);
				}
				public boolean visit(final SuperMethodReference node) {
					//Log.info("SuperMethodReference");
					//Log.info(node);
					return super.visit(node);
				}
				public boolean visit(final TagElement node) {
					//Log.info("TagElement");
					//Log.info(node);
					return super.visit(node);
				}
				public boolean visit(final TextElement node) {
					//Log.info("TextElement");
					//Log.info(node);
					return super.visit(node);
				}

				public boolean visit(final TypeDeclarationStatement node) {
					//Log.info("TypeDeclarationStatement");
					//Log.info(node);
					return super.visit(node);
				}
				public boolean visit(final TypeLiteral node) {
					//Log.info("TypeLiteral");
					//Log.info(node);
					return super.visit(node);
				}
				public boolean visit(final TypeMethodReference node) {
					//Log.info("TypeMethodReference");
					//Log.info(node);
					return super.visit(node);
				}
				public boolean visit(final TypeParameter node) {
					//Log.info("UnionType");
					//Log.info(node);
					return super.visit(node);
				}
				public boolean visit(final UnionType node) {
					//Log.info("UnionType");
					//Log.info(node);
					return super.visit(node);
				}
				public boolean visit(final VariableDeclarationExpression node) {
					//Log.info("VariableDeclarationExpression");
					//Log.info(node);
					return super.visit(node);
				}
				public boolean visit(final VariableDeclarationStatement node) {
					//Log.info("VariableDeclarationStatement");
					//Log.info(node);
					return super.visit(node);
				}
				public boolean visit(final WildcardType node) {
					//Log.info("WildcardType");
					//Log.info(node);
					return super.visit(node);
				}																	
				
				
				
				});
			} catch (Exception e) {
				System.out.println("Problem : " + e.toString());
				e.printStackTrace();
				System.exit(0);
			}

		} catch (Exception e) {
			System.out.println("\nError while executing compilation unit : " + e.toString());
		}

	}
	
	public ArrayList<ASTNode> getViolatedVariableList(String source, int type) {
		ASTParser parser = ASTParser.newParser(AST.JLS12);
		parser.setKind(ASTParser.K_COMPILATION_UNIT);
		char[] content = source.toCharArray();
		parser.setSource(content);
		//parser.setUnitName("temp.java");
		Map<String, String> options = JavaCore.getOptions();
		options.put(JavaCore.COMPILER_COMPLIANCE, JavaCore.VERSION_1_8);
		options.put(JavaCore.COMPILER_CODEGEN_TARGET_PLATFORM,
				JavaCore.VERSION_1_7);
		options.put(JavaCore.COMPILER_SOURCE, JavaCore.VERSION_1_8);
		String[] sources = {};
		String[] classPaths = {};
		parser.setEnvironment(classPaths, sources, null, true);
		parser.setResolveBindings(false);
		parser.setCompilerOptions(options);
		parser.setStatementsRecovery(true);
		
		try {
			final CompilationUnit unit = (CompilationUnit) parser.createAST(null);
			cUnit = unit;

			// Process the main body
			try {
				unit.accept(new ASTVisitor() {						
					public boolean visit(SimpleName node) {
						ASTNode parent = node.getParent();
						Integer lineNum = getLineNum(node.getStartPosition());
						ASTNode tempNode = parent;		
//						if(lineNum == 124) {
//							System.out.println("a");
//						}
						while(!(tempNode instanceof TypeDeclaration)) {
							tempNode = tempNode.getParent();
							if(tempNode instanceof CompilationUnit) {
								return super.visit(node);
							}
						}
												
						TypeDeclaration belongClass = (TypeDeclaration) tempNode;						
						Integer classStart = getLineNum(belongClass.getStartPosition());
						Integer classEnd = getLineNum(belongClass.getStartPosition() + belongClass.getLength() - 1);						
						
						if((parent instanceof VariableDeclarationFragment || parent instanceof SingleVariableDeclaration) 
								&& !(parent.getParent() instanceof FieldDeclaration)) {
							if(classStart <= info.start && classEnd >= info.end && !(parent.getParent() instanceof MethodDeclaration))
								lstVariableDeclaration.add(node.getIdentifier());
							
//							for(String temp : lstFieldMemberDeclaration)
//								lstVariableDeclaration.remove(temp);
						}
						
						if(lineNum >= info.start && lineNum <= info.end && lstVariableDeclaration.contains(node.getIdentifier())) {
							if(info.varNames.size() != 0) {
								if(info.varNames.contains(node.toString()))
										if(lstViolatedVariables.size() == 0)
											lstViolatedVariables.add(node);
							}
							else lstViolatedVariables.add(node);
						}					
						else if (lineNum >= info.start && lineNum <= info.end && lstFieldMemberDeclaration.contains(node.getIdentifier())) {
							lstViolatedField.add(node);
						}
						return super.visit(node);
					}
					
					public boolean visit(FieldDeclaration node) {						
						lstFieldMemberDeclaration.add(((VariableDeclarationFragment) node.fragments().get(0)).getName().getIdentifier());											
						return super.visit(node);
					}
					
					public boolean visit(ThisExpression node) {
						Integer lineNum = getLineNum(node.getStartPosition());						
						if(lineNum >= info.start && lineNum <=info.end) {
							lstViolatedField.add(node);
						}					
						return super.visit(node);
					}
					
					public boolean visit(StringLiteral node) {
						if (type != 0 && type != 1) {
							Integer lineNum = getLineNum(node.getStartPosition());
							String nodeValue = node.toString();
							nodeValue = nodeValue.substring(1, nodeValue.length()-1);

							if(nodeValue.equals(info.varNames.get(0)) && lineNum >= info.start && lineNum <= info.end) {
								lstViolatedStringNode.add(node);
							}
							
							else if(lstViolatedStringNode.size() == 0 && lineNum >= info.start -1 && lineNum <= info.end +1) {
								lstViolatedStringNode.add(node);
							}
						}
						return super.visit(node);
					}
					
					public boolean visit(Assignment node) {
						ASTNode rhs = node.getRightHandSide();
						if(rhs instanceof NullLiteral) {
							if(!lstViolatedField.contains(node.getRightHandSide()))
								lstViolatedField.add(node.getLeftHandSide());
						}
						
						return super.visit(node);
					}
					
				});
			} catch (Exception e) {
				System.out.println("Problem : " + e.toString());
				e.printStackTrace();
				System.exit(0);
			}

		} catch (Exception e) {
			System.out.println("\nError while executing compilation unit : " + e.toString());
		}
		
		lstViolatedVariables.removeAll(lstViolatedField);	
		
		//type 1 for field
		if(type == 0)
			return lstViolatedVariables;
		else if(type == 1)
			return lstViolatedField;		
		else
			return lstViolatedStringNode;
	}

	private boolean violatedNodeisIn(ASTNode node, ASTNode targetNode) {
		int nodeStartPosition = node.getStartPosition();
		int nodeEndPosition = nodeStartPosition + node.getLength();
		int targetStartPosition = targetNode.getStartPosition();
		int targetEndPosition = targetStartPosition + targetNode.getLength();
		
		if(targetStartPosition >= nodeStartPosition && targetEndPosition <= nodeEndPosition) {
			return true;
		} else return false;
	}
	
	private boolean isNodeInMethodOrBlock(ASTNode targetNode) {		
		int targetStartPosition = targetNode.getStartPosition();
		int targetEndPosition = targetStartPosition + targetNode.getLength();
		
		if(targetStartPosition >= methodStart && targetEndPosition <= methodEnd) {
			return true;
		} else if(targetStartPosition >= blockStart && targetEndPosition <= blockEnd) {
			return true;
		}
		return false;
	}
	
	private ASTNode getFrom(ASTNode node) {
		ASTNode tempParent = node.getParent();
		while(true) {
			if(tempParent instanceof Block || tempParent instanceof MethodDeclaration || tempParent instanceof TypeDeclaration) {
				break;
			}
			else if(tempParent instanceof EnhancedForStatement) {
				System.out.println(((EnhancedForStatement) tempParent).getExpression());
				return (ASTNode) ((EnhancedForStatement) tempParent).getExpression();
			}
			tempParent = tempParent.getParent();			
		}
		return null;
	}
	
	private ASTNode getFrom(ThisExpression node) {
		ASTNode tempParent = node.getParent();
		while(true) {
			if(tempParent instanceof Block || tempParent instanceof MethodDeclaration || tempParent instanceof TypeDeclaration) {
				break;
			}
			else if(tempParent instanceof EnhancedForStatement) {
				System.out.println(((EnhancedForStatement) tempParent).getExpression());
				return (ASTNode) ((EnhancedForStatement) tempParent).getExpression();
			}
			tempParent = tempParent.getParent();			
		}
		return null;
	}
	
	private VarState isD(ASTNode node) {
		if(node.toString().equals("indexOfOpenBracket")) {
			System.out.println("a");
		}
		ASTNode tempParent = node.getParent();
//		System.out.println(tempParent.getClass().getSimpleName());
	
		if(tempParent instanceof SingleVariableDeclaration) {
			return VarState.D;
		} else if(tempParent instanceof VariableDeclarationFragment) {
			if(((VariableDeclarationFragment) tempParent).getInitializer() == null) {
				return VarState.D;
			} else if(((VariableDeclarationFragment) tempParent).getInitializer().getClass().getSimpleName().equals("NullLiteral")) {
				return VarState.DIN;
			}
			else
			return VarState.DI;
		}
		
		while(true) {
			if(tempParent instanceof MethodDeclaration || tempParent instanceof Block) {
				break;
			} else if (tempParent instanceof Assignment) {
				ASTNode lHS = ((Assignment) tempParent).getLeftHandSide();				
				if(lHS.toString().contains(node.toString())){
					if (((Assignment) tempParent).getRightHandSide() instanceof NullLiteral){
						return VarState.NAss;
					}
					else
					return VarState.Ass;
				} else
					return VarState.Ref;
			}
			if(tempParent.getParent() != null) {
				tempParent = tempParent.getParent();
			} else break;
		}
		return VarState.Ref;
	}
	
	private VarState isD(ThisExpression node) {
		
		ASTNode tempParent = node.getParent();
//		System.out.println(tempParent.getClass().getSimpleName());
	
		if(tempParent instanceof SingleVariableDeclaration) {
			return VarState.D;
		} else if(tempParent instanceof VariableDeclarationFragment) {
			if(((VariableDeclarationFragment) tempParent).getInitializer() == null) {
				return VarState.D;
			} else if(((VariableDeclarationFragment) tempParent).getInitializer().getClass().getSimpleName().equals("NullLiteral")) {
				return VarState.DIN;
			}
			else
			return VarState.DI;
		}
		
		while(true) {
			if(tempParent instanceof MethodDeclaration || tempParent instanceof Block) {
				break;
			} else if (tempParent instanceof Assignment) {
				if(((Assignment) tempParent).getLeftHandSide().equals(node)) {
					return VarState.Ass;
				} else
					return VarState.Ref;
			}
			if(tempParent.getParent() != null) {
				tempParent = tempParent.getParent();
			} else break;
		}
		return VarState.Ref;
	}
	
	private VarState checkType(ASTNode node) {
		ASTNode tempParent = node.getParent();
		
		if(tempParent instanceof ArrayAccess) {
			if(((ArrayAccess) tempParent).getIndex() instanceof NumberLiteral) {
				return VarState.ArrIdxF;
			} else
				return VarState.ArrIdxC;
		}
		
		return VarState.NArr;		
	}
	
	private VarState checkType(ThisExpression node) {
		ASTNode tempParent = node.getParent();
		
		if(tempParent instanceof ArrayAccess) {
			if(((ArrayAccess) tempParent).getIndex() instanceof NumberLiteral) {
				return VarState.ArrIdxF;
			} else
				return VarState.ArrIdxC;
		}
		
		return VarState.NArr;		
	}
	
	private boolean isInCondition(SimpleName node) {
		ASTNode tempParent = node.getParent();
		while(true) {
			if(tempParent instanceof MethodDeclaration || tempParent instanceof Block) {
				break;
			}
			else if(tempParent instanceof ForStatement ||
					tempParent instanceof IfStatement||
					tempParent instanceof EnhancedForStatement||
					tempParent instanceof WhileStatement ||
					tempParent instanceof SwitchCase ||
					tempParent instanceof SwitchExpression) {
				return true;
			} 
			if(tempParent.getParent() != null) {
				tempParent = tempParent.getParent();
			} else break;
		}
		return false;
	}
	
	private boolean isInAnnotation(StringLiteral node) {
		ASTNode tempParent = node.getParent();
		while(true) {
			if(tempParent instanceof MethodDeclaration || tempParent instanceof Block) {
				break;
			}
			else if(tempParent instanceof NormalAnnotation ||
					tempParent instanceof MarkerAnnotation||
					tempParent instanceof SingleMemberAnnotation||
					tempParent instanceof AnnotationTypeDeclaration ||
					tempParent instanceof AnnotationTypeMemberDeclaration
					) {
				return true;
			} 
			if(tempParent.getParent() != null) {
				tempParent = tempParent.getParent();
			} else break;
		}
		return false;
	}
	
	private boolean isInAnnotation(ThisExpression node) {
		ASTNode tempParent = node.getParent();
		while(true) {
			if(tempParent instanceof MethodDeclaration || tempParent instanceof Block) {
				break;
			}
			else if(tempParent instanceof NormalAnnotation ||
					tempParent instanceof MarkerAnnotation||
					tempParent instanceof SingleMemberAnnotation||
					tempParent instanceof AnnotationTypeDeclaration ||
					tempParent instanceof AnnotationTypeMemberDeclaration
					) {
				return true;
			} 
			if(tempParent.getParent() != null) {
				tempParent = tempParent.getParent();
			} else break;
		}
		return false;
	}
	
	private boolean isInAnnotation(SimpleName node) {
		ASTNode tempParent = node.getParent();
		while(true) {
			if(tempParent instanceof MethodDeclaration || tempParent instanceof Block) {
				break;
			}
			else if(tempParent instanceof NormalAnnotation ||
					tempParent instanceof MarkerAnnotation||
					tempParent instanceof SingleMemberAnnotation||
					tempParent instanceof AnnotationTypeDeclaration ||
					tempParent instanceof AnnotationTypeMemberDeclaration
					) {
				return true;
			} 
			if(tempParent.getParent() != null) {
				tempParent = tempParent.getParent();
			} else break;
		}
		return false;
	}
	
	private boolean isInCondition(StringLiteral node) {
		ASTNode tempParent = node.getParent();
		while(true) {
			if(tempParent instanceof MethodDeclaration || tempParent instanceof Block) {
				break;
			}
			else if(tempParent instanceof ForStatement ||
					tempParent instanceof IfStatement||
					tempParent instanceof EnhancedForStatement||
					tempParent instanceof WhileStatement ||
					tempParent instanceof SwitchCase ||
					tempParent instanceof SwitchExpression) {
				return true;
			} 
			if(tempParent.getParent() != null) {
				tempParent = tempParent.getParent();
			} else break;
		}
		return false;
	}
	
	private boolean isInCondition(ThisExpression node) {
		ASTNode tempParent = node.getParent();
		while(true) {
			if(tempParent instanceof MethodDeclaration || tempParent instanceof Block) {
				break;
			}
			else if(tempParent instanceof ForStatement ||
					tempParent instanceof IfStatement||
					tempParent instanceof EnhancedForStatement||
					tempParent instanceof WhileStatement ||
					tempParent instanceof SwitchCase ||
					tempParent instanceof SwitchExpression) {
				return true;
			} 
			if(tempParent.getParent() != null) {
				tempParent = tempParent.getParent();
			} else break;
		}
		return false;
	}
	
	public int getLineNum(int startPosition){
		return cUnit.getLineNumber(startPosition);
	}

	public String getStringCode(){
		return String.join("\n", info.sourceByLine);
	}

	public CompilationUnit getCompilationUnit(){
		return cUnit;
	}
	
	public PackageDeclaration getPackageDeclaration(){
		return pkgDeclaration;
	}	
	
	/**
	 * Get only name in case of ArrayAccess
	 * @param leftOperand
	 * @return String
	 */
	@SuppressWarnings("unused")
	private String getOnlyNameFromArrayAccess(Expression operand) {
		
		if(operand instanceof ArrayAccess){
			return operand.toString().split("\\[")[0];
		}
		
		return operand.toString();
	}
	
	public String getTypeOfSimpleName(ASTNode astNode,String name) {
		
		// TODO need to find a target name in a hierarchy but not globally in a file
		final ArrayList<SingleVariableDeclaration> lstSingleVarDecs = new ArrayList<SingleVariableDeclaration>();
		final ArrayList<VariableDeclarationStatement> lstVarDecStmts = new ArrayList<VariableDeclarationStatement>();
		final ArrayList<FieldDeclaration> lstFieldDecs = new ArrayList<FieldDeclaration>();
		final ArrayList<VariableDeclarationExpression> lstVarDecExps = new ArrayList<VariableDeclarationExpression>();
		
		astNode.accept(new ASTVisitor() {
			public boolean visit(SingleVariableDeclaration node) {
				lstSingleVarDecs.add(node);
				return super.visit(node);
			}
			public boolean visit(VariableDeclarationStatement node) {
				lstVarDecStmts.add(node);
				return super.visit(node);
			}
			public boolean visit(VariableDeclarationExpression node) {
				lstVarDecExps.add(node);
				return super.visit(node);
			}
			public boolean visit(FieldDeclaration node) {
				lstFieldDecs.add(node);
				return super.visit(node);
			}
		}
		);
		
		for(SingleVariableDeclaration dec:lstSingleVarDecs){
			if (dec.getName().toString().equals(name))
				return dec.getType().toString();
		}
		for(VariableDeclarationStatement dec:lstVarDecStmts){
			for(Object node:dec.fragments()){
				if(node instanceof VariableDeclarationFragment){
					if (((VariableDeclarationFragment)node).getName().toString().equals(name))
						return dec.getType().toString();
				}
			}
		}
		for(VariableDeclarationExpression dec:lstVarDecExps){
			for(Object node:dec.fragments()){
				if(node instanceof VariableDeclarationFragment){
					if (((VariableDeclarationFragment)node).getName().toString().equals(name))
						return dec.getType().toString();
				}
			}
		}
		
		for(FieldDeclaration dec:lstFieldDecs){
			for(Object node:dec.fragments()){
				if(node instanceof VariableDeclarationFragment){
					if (((VariableDeclarationFragment)node).getName().toString().equals(name))
						return dec.getType().toString();
				}
			}
		}
		
		if(astNode.getParent() == null)
			return "";
		
		return getTypeOfSimpleName(astNode.getParent(),name);
	}

	public ArrayList<SimpleName> getSimpleNames(ASTNode node) {
		
		final ArrayList<SimpleName> simpleNames = new ArrayList<SimpleName>();
		
		node.accept(new ASTVisitor() {
			
			public boolean visit(SimpleName node) {
				simpleNames.add(node);
				return super.visit(node);
			}
			
		});
		
		return simpleNames;
	}

	public ArrayList<QualifiedName> getQualifiedNames(ASTNode exp) {
		final ArrayList<QualifiedName> qualifiedNames = new ArrayList<QualifiedName>();
		
		exp.accept(new ASTVisitor() {
			
			public boolean visit(QualifiedName node) {
				qualifiedNames.add(node);
				return super.visit(node);
			}
			
		});
		
		return qualifiedNames;
	}

	public ArrayList<MethodInvocation> getMethodInvocations(ASTNode node) {
		final ArrayList<MethodInvocation> methodInvocations = new ArrayList<MethodInvocation>();
		
		node.accept(new ASTVisitor() {
			
			public boolean visit(MethodInvocation node) {
				methodInvocations.add(node);
				return super.visit(node);
			}
			
		});
		
		return methodInvocations;
	}

	public ArrayList<InfixExpression> getInfixExpressions(Expression exp) {
		final ArrayList<InfixExpression> infixExps = new ArrayList<InfixExpression>();
		
		exp.accept(new ASTVisitor() {
			
			public boolean visit(InfixExpression node) {
				infixExps.add(node);
				return super.visit(node);
			}
			
		});
		
		return infixExps;
	}

	public ArrayList<ArrayAccess> getArrayAccesses(ASTNode node) {
		final ArrayList<ArrayAccess> arrayAccesses = new ArrayList<ArrayAccess>();
		
		node.accept(new ASTVisitor() {
			
			public boolean visit(ArrayAccess node) {
				arrayAccesses.add(node);
				return super.visit(node);
			}
			
		});
		
		return arrayAccesses;
	}

	public ArrayList<ExpressionStatement> getExpressionStatements(ASTNode node) {
		final ArrayList<ExpressionStatement> expStmts = new ArrayList<ExpressionStatement>();
		
		node.accept(new ASTVisitor() {
			
			public boolean visit(ExpressionStatement node) {
				expStmts.add(node);
				return super.visit(node);
			}
			
		});
		
		return expStmts;
	}

	public ArrayList<VariableDeclarationFragment> getVariableDeclarationFragments(ASTNode node) {
		final ArrayList<VariableDeclarationFragment> varDecFrags = new ArrayList<VariableDeclarationFragment>();
		
		node.accept(new ASTVisitor() {
			
			public boolean visit(VariableDeclarationFragment node) {
				varDecFrags.add(node);
				return super.visit(node);
			}
			
		});
		
		return varDecFrags;
	}

	public ArrayList<VariableDeclaration> getVariableDeclaration(ASTNode node) {
		final ArrayList<VariableDeclaration> varDecs = new ArrayList<VariableDeclaration>();
		
		node.accept(new ASTVisitor() {
			
			public boolean visit(VariableDeclarationFragment node) {
				varDecs.add(node);
				return super.visit(node);
			}
			
			public boolean visit(SingleVariableDeclaration node) {
				varDecs.add(node);
				return super.visit(node);
			}
			
		});
		
		return varDecs;
	}
	
	public HashMap<String,VariableDeclaration> getMapForVariableDeclaration(ASTNode node) {
		final HashMap<String, VariableDeclaration> mapVarDecs = new HashMap<String,VariableDeclaration>();
		
		if(node==null) return mapVarDecs;
		
		node.accept(new ASTVisitor() {
			
			public boolean visit(VariableDeclarationFragment node) {
				mapVarDecs.put(node.getName().toString(),node);
				return super.visit(node);
			}
			
			public boolean visit(SingleVariableDeclaration node) {
				mapVarDecs.put(node.getName().toString(),node);
				return super.visit(node);
			}
			
		});
		
		return mapVarDecs;
	}

	public ArrayList<String> getVariableNames(ASTNode node) {
		ArrayList<String> localVarNames = new ArrayList<String>();
		
		ArrayList<VariableDeclarationFragment> varDecFrags = getVariableDeclarationFragments(node);		
		for(VariableDeclarationFragment verDecFrag:varDecFrags){
			localVarNames.add(verDecFrag.getName().toString());
		}
		
		ArrayList<SingleVariableDeclaration> singleVarDecs = getSingleVariableDeclarations(node);
		for(SingleVariableDeclaration singleVarDec:singleVarDecs){
			localVarNames.add(singleVarDec.getName().toString());
		}
		
		return localVarNames;
	}

	private ArrayList<SingleVariableDeclaration> getSingleVariableDeclarations(ASTNode node) {
		final ArrayList<SingleVariableDeclaration> singleVarDecs = new ArrayList<SingleVariableDeclaration>();
		
		node.accept(new ASTVisitor() {
			
			public boolean visit(SingleVariableDeclaration node) {
				singleVarDecs.add(node);
				return super.visit(node);
			}
			
		});
		
		return singleVarDecs;
	}
	
	public AbstractTypeDeclaration getTypeDeclationOf(ASTNode node) {
		
		if(node==null) // null can be happen when there is no TypeDeclaration but EnumDeclaration
			return null;
		
		if(node.getParent() instanceof AbstractTypeDeclaration)
			return (AbstractTypeDeclaration) node.getParent();
		
		return getTypeDeclationOf(node.getParent());
	}

	public ArrayList<FieldAccess> getFieldAccesses(ASTNode node) {
		final ArrayList<FieldAccess> fieldAccesses = new ArrayList<FieldAccess>();
		
		node.accept(new ASTVisitor() {
			
			public boolean visit(FieldAccess node) {
				fieldAccesses.add(node);
				return super.visit(node);
			}
			
		});
		
		return fieldAccesses;
	}

	public MethodDeclaration getMethodDec(ASTNode node) {
		
		if(node.getParent() == null)
			return null;
		
		if(node.getParent() instanceof MethodDeclaration){
			return (MethodDeclaration) node.getParent();
		}
		
		return getMethodDec(node.getParent());
	}

	public TypeDeclaration getTypeDeclaration(ASTNode node) {
		
		if(node.getParent() == null) return null;
		
		if(node.getParent() instanceof TypeDeclaration)
			return (TypeDeclaration) node.getParent();
		
		return getTypeDeclaration(node.getParent());
	}

	public MethodDeclaration getMethodDecBelongTo(ASTNode node) {
		
		if(node.getParent() == null) return null;
		
		if(node.getParent() instanceof MethodDeclaration)
			return (MethodDeclaration) node.getParent();
		
		
		return getMethodDecBelongTo(node.getParent());
	}

	public ArrayList<ThrowStatement> getThrowStatements(ASTNode node) {
		final ArrayList<ThrowStatement> throwStatements = new ArrayList<ThrowStatement>();
		
		node.accept(new ASTVisitor() {
			
			public boolean visit(ThrowStatement node) {
				throwStatements.add(node);
				return super.visit(node);
			}
			
		});
		
		return throwStatements;
	}

	public ArrayList<Assignment> getAssignments(ASTNode node) {
		final ArrayList<Assignment> assignments = new ArrayList<Assignment>();
		
		node.accept(new ASTVisitor() {
			
			public boolean visit(Assignment node) {
				assignments.add(node);
				return super.visit(node);
			}
			
		});
		
		return assignments;
	}

	public ASTNode getInterface(ASTNode node) {
		
		if(node == null) return null;
		
		if(node.getParent() instanceof TypeDeclaration)
			return node.getParent();
		
		return getInterface(node.getParent());
	}
}
