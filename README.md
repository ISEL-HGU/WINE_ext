# WINE: Warning mINEr for improving bug finders
- We ran many software projects published by Apache Software Foundation and collected lots of warnings. To extract meaningful warnings, we mined ```the representative warnings``` based on all warnings.
- An artifact and data published in Information and Software Technology:
  - Yoon-ho Choi, Jaechang Nam, **"WINE: Warning miner for improving bug finders"** , Information and Software Technology, Volume 155, 2023, 107109, ISSN 0950-5849, https://doi.org/10.1016/j.infsof.2022.107109. (https://www.sciencedirect.com/science/article/pii/S095058492200218X)

## Our package is divided in folders and it is organized as follows:

- **src** : This folder contains the implementation of our tool, WINE. You can build the project by using ```gradle build``` command.
- **selectedRulesResult** : The warning data generated by PMD 6.25.0 by using the ```selected``` rules is located in the ```selectedRuleResult``` folder. It contains Warning data (raw_data) and the ```representative warnings```.
- **allRuleResult** : The warning data generated by PMD 6.25.0 by using the ```all``` rules is located in the ```allRuleResult``` folder. It contains Warning data (raw_data) and the ```representative warnings```.
- **ResultFromPMDs** : The warning data generated by various versions of PMD by using the ```selected``` rules is located in the ```ResultFromPMDs``` folder. It contains Warning data (raw_data) and the ```representative warnings```.
- **pmds** : Various versions of PMD could be found in ```pmds```.
- **example** : An example for replicate our experiment is located in the ```example``` folder. It contains the built ```WINE``` , the list of target projects, and scripts to run ```WINE```

## Implementations
There are two modules: 
1) **SAResultMiner** runs a bug finder and collects warnings. Then, it processes warnings to extract ```path, start line number, end line num, and code``` information from the report of the bug finder.
```
input: PMD rule, GitHub address of the target projects, executable file of PMD
output: a csv file, which has columns named as detection id, path, start line number, end line number, and code
```
2) **FPCollector** read the result of SAResultMiner, then generate warning patterns, and extract the ```representative warnings```.
```
input: a csv file, which is the result of SAResultMiner
output: a csv file, which contains the representative warnings
```

## Usage:
- only SAResultMiner: ./WINE_ext -s -R \<PMD Rule Context> -t \<TargetAddress.txt Path> -p \<pmd runfile path>

- only FPCollector  : ./WINE_ext -f -e \<SAResultMiner_Result.csv Path> 
              
- Both              : ./WINE_ext -m -R \<PMD Rule Context> -t \<TargetAddress.txt Path> -p \<pmd runfile path>
  
```
 -f,--fpcollector    run only False Positive candidate Collector.
                     SAResultMiner_Result.csv and TargetAddress.txt
                     are needed as an arg.
 -m,--fcminer        run both SAResultMiner and FPCollector.
                     TargetAddress.txt and Rule are needed as args.
 -o,--out <arg>      path of output
 -p,--pmd <arg>      path of PMD run file
                     ex) ./pmd-bin-6.25.0/bin/run.sh
 -R,--rule <arg>     need an argument, rule command of a static analysis
                     tool
 -s,--saresult       run only Static Analysis Result Miner.
                     TargetAddress.txt and Rule are needed as args.
 -t,--target <arg>   path of TargetAddress.txt
```

### Example:
Example scripts are included in ```example``` directory.

## Abstract:
- Context:
Bug finders have been actively used to efficiently detect bugs. However, developers and researchers found that the bug finders show high false positive rate. The false positives can be caused by two major reasons: (1) users rejecting warnings and (2) false-positive inducing issues (FPI), i.e., incorrect or incomplete rule implementations.

- Objective:
The objective of this study is to reduce warning validation costs for developers of bug finders when they validate the implementation of bug finders to reduce false positives caused by FPI.

- Methods:
To achieve the objective, we propose a novel approach, WINE. The key idea of WINE is to extract representative warnings that are structurally equal to other warnings, or structurally contain other warnings from numerous warnings. The rationale behind the approach is that the warnings detected based on structural information and tokens might be equal to each other, or contain other warnings structurally.

- Results:
We evaluated our approach with PMD, an open source bug finder, and 1,008 Java open source projects maintained by Apache Software Foundation. As a result, WINE extracted just about 2% of all warnings. Among the 2% of warnings, we could find the 28 FPIs of PMD. Among them, ten FPIs were already fixed among them. In addition, we simulated our approach in regression testing of PMD with twelve versions changes of PMD (6.25.0 to 6.37.0). As a result, we observed that WINE can effectively reduce the inspection costs by removing about 95% changed warnings.

- Conclusion:
Based on the results, we suggest that WINE could be adopted to improve the bug finders in terms of reducing false positives cause by FPI. In addition, WINE is helpful in the development processes of bug finders to identify false positives and false negatives, especially in regression testing of bug finders.

## Contact:
- Yoon-ho Choi (yhchoi@handong.ac.kr)
- Jaechang Nam (jcnam@handong.edu)
