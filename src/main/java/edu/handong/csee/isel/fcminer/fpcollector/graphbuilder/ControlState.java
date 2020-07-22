package edu.handong.csee.isel.fcminer.fpcollector.graphbuilder;
//S for start node(root-MethodDeclaration)
//E for Leaf Control Node
//M for the rest
//L for Loop Control Node(Do, While, For, EnhancedFor)
//T for Terminate Control Node(Throw, Return)
//C for Conditional Control Node(Switch, If, ConditionalExpression, SwitchCase, SwitchExpression, Try, Catch)
public enum ControlState {
	S, E, M, L, T, C
}
