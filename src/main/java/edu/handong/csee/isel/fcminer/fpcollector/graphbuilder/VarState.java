package edu.handong.csee.isel.fcminer.fpcollector.graphbuilder;
/*
*State
1.Define
	1) DIN for definition + Initialization(with Null)
	2) DI for definition + Initialization(with none-Null)
	3) D for definition without Initialization
	4) FDIN for field definition + Initialization(with Null)
	5) FDI for definition + Initialization(with none-Null)
	6) FD for definition without Initialization
2.Reference
	1) Ass for being used to be Assigned, but Not Initialization
	2) FAss for being used to be field Assigned, but Not Initialization
	3) Ref for being used
	4) FRef for field being used
	5) NAss for being used to be assigned with null
	6) FRef for field being used to be assigned with null
--------------------------------------
In Condition
1. I for in conditional statement/loop
2. O for not in conditional statement/loop
---------------------------------------
In Annotation
1. IA for in annotation
2. NA for not in annotation 
---------------------------------------
About Type
1. ArrIdxC for ArrayType whose index is composed with ++, -- or var
2. ArrIdxF for ArrayType whose index is fixed integer
3. NArr for None-ArrayType
4. Str for String value
 * */

public enum VarState {
	DIN, DI, D, FDIN, FDI, FD, I, O, Ass, FAss, NAss, FNAss, Ref, FRef, ArrIdxC, ArrIdxF, NArr, Str, IA, NA
}
