package edu.handong.csee.isel.fcminer.fpcollector;

import edu.handong.csee.isel.fcminer.fpcollector.subset.SuperWarning;
import edu.handong.csee.isel.fcminer.fpcollector.tokendiff.ast.ITree;
import edu.handong.csee.isel.fcminer.fpcollector.tokendiff.ast.gen.Property;
import edu.handong.csee.isel.fcminer.fpcollector.tokendiff.datapreproc.Node;
import edu.handong.csee.isel.fcminer.fpcollector.tokendiff.datapreproc.NodeList;
import edu.handong.csee.isel.fcminer.fpcollector.tokendiff.datapreproc.RawData;
import edu.handong.csee.isel.fcminer.util.CliCommand;
import edu.handong.csee.isel.fcminer.util.CliOptions.RunState;
import edu.handong.csee.isel.fcminer.util.OSValidator;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;
import org.eclipse.jdt.core.dom.ASTNode;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class SuperWarningCollector {
	private enum Relation {
		NULL, SubWarning, Equivalent;
	}

	public void run(CliCommand command, int numOfAlarms) {
		if(command.getState().equals(RunState.SAResultMiner)) return;

		final int timeInterval = 5;
		ArrayList<SuperWarning> superWarnings = new ArrayList<>();
		ArrayList<CSVRecord> records = readReport(command.getResultPath());
		long startTime = System.currentTimeMillis();
		boolean timerFlag = false;
		int cnt = 0;

		for(CSVRecord record: records) {
			if (record.get(0).equals("Detection ID")) continue;

			RawData tmpData = new RawData(modifyFilePathToOS(record.get(1)), record.get(2), record.get(2), record.get(3));
			if (tmpData.isPass()) continue;

			cnt++;
			NodeList tmpNodeList = divide(tmpData);

			//get compare data through process data by using raw data
			if (superWarnings.size() == 0) {
				superWarnings.add(new SuperWarning(tmpNodeList.getvLineCode(), tmpNodeList.getvNodeCode(), tmpNodeList));
			}
			else {
				generateSubWarning(tmpNodeList, superWarnings);
			}

			long currentTime = System.currentTimeMillis();
			long sec = (currentTime - startTime) / 1000;

			if (sec > timeInterval)
				timerFlag = true;

			if (numOfAlarms != 0)
				printProgress(cnt, superWarnings.size(), numOfAlarms);
			else if (timerFlag == true) {
				startTime = System.currentTimeMillis();
				timerFlag = false;
				printProgress(cnt, superWarnings.size());
			}
		}
		numOfAlarms = cnt;
		System.out.println("INFO: Finished to mining representative warnings");
		System.out.println("INFO: [Inspected # of warnings, # of super warnings]: ["  + cnt + ",  " + superWarnings.size() + "]");

		superWarnings = sortByLowFrequency(superWarnings);
		writeConcreteCodePattern(superWarnings, numOfAlarms);
	}

	private ArrayList<CSVRecord> readReport(String filePath){
		ArrayList<CSVRecord> records = new ArrayList<>();
		try {
			Reader outputFile = new FileReader(filePath);
			CSVParser parser = CSVFormat.EXCEL.parse(outputFile);
			records = new ArrayList<>(parser.getRecords());
		} catch(IOException e) {
			e.printStackTrace();
			System.exit(-1);
		}
		return records;
	}

	private String modifyFilePathToOS(String filePath) {
		String osName = OSValidator.getOS();
		String newFilePath = "";
		try {
			if (filePath.contains("\\") && osName.equals("linux")) {
				for (int i = 0; i < filePath.length(); i++) {
					if (filePath.charAt(i) == '\\') {
						newFilePath += '/';
					} else {
						newFilePath += filePath.charAt(i);
					}
				}
				filePath = "" + newFilePath;
			} else if (filePath.contains("/") && osName.equals("window")) {
				for (int i = 0; i < filePath.length(); i++) {
					if (filePath.charAt(i) == '/') {
						newFilePath += '\\';
					} else {
						newFilePath += filePath.charAt(i);
					}
				}
			}
		} catch(NullPointerException e){
			System.out.println("Failed to get OS information");
			System.exit(-1);
		}

		return filePath;
	}

	private NodeList divide(RawData rawData) {
		List<ITree> currents = new ArrayList<>();
		NodeList nodeList = new NodeList();
		//collect all leaves
		currents.add(rawData.getVNode());
		List<ITree> leaves = new ArrayList<>();
		while(currents.size() > 0){
			ITree c = currents.remove(0);
			if(c.isLeaf()) {
				leaves.add(c);
			}
			currents.addAll(c.getChildren());
		}
		sortLeaves(leaves, rawData.getVLineNum());

		for(int i = 0; i < leaves.size(); i ++){
			ITree l = leaves.get(i);

			if(rawData.getStart() <= l.getStartLineNum() && l.getEndLineNum() <= rawData.getEnd()) {
				if(l.getStartLineNum() == rawData.getVLineNum()) {
					nodeList.addNode(new Node
							(l.getParentProps(), l.getType(), l.getPos(), l.getDepth(), rawData.getVNode().getNode2String(), true, l.getLabel()));
					sort(nodeList);
				}
				else if (l.getStartLineNum() > rawData.getVLineNum()) {
					ArrayList<Property> pp = l.getParentProps();
					ArrayList<Node> cDataList = nodeList.getNodeList();
					ArrayList<Property> cDataProperty = nodeList.getNodeList().get(cDataList.size() - 1).getParentProperty();
					//c's last parent property
					Property currentLastProperty = pp.get(pp.size()-1);
					Property cDataLastProperty = cDataProperty.get(cDataProperty.size() - 1);

					if(currentLastProperty.getNodeType() == cDataLastProperty.getNodeType() &&
							currentLastProperty.getProp().equals(cDataLastProperty.getProp())) {
						nodeList.addNode(new Node
								(l.getParentProps(), l.getType(), l.getPos(), l.getDepth(), rawData.getVNode().getNode2String(), true, l.getLabel()));
						sort(nodeList);
					}
				}
				else if (l.isLeaf() && l.getStartLineNum() < rawData.getVLineNum()) {
					ArrayList<Property> pp = l.getParentProps();
					ArrayList<Node> cDataList = nodeList.getNodeList();
					ArrayList<Property> cDataProperty = nodeList.getNodeList().get(0).getParentProperty();
					//c's last parent property
					Property currentLastProperty = pp.get(pp.size()-1);
					Property cDataLastProperty = cDataProperty.get(cDataProperty.size() - 1);

					if(currentLastProperty.getNodeType() == cDataLastProperty.getNodeType() &&
							currentLastProperty.getProp().equals(cDataLastProperty.getProp())) {
						nodeList.addNode(new Node
								(l.getParentProps(), l.getType(), l.getPos(), l.getDepth(), rawData.getVNode().getNode2String(), true, l.getLabel()));
						sort(nodeList);
					}
				}
			}
		}

		nodeList.setvLineCode(rawData.getVLine());
		nodeList.setvNodeCode(rawData.getVNode().getNode2String());

		return nodeList;
	}

	private void sortLeaves (List<ITree> currents, int vLineNum) {
		currents.sort(new Comparator<ITree>() {
			public int compare(ITree t1, ITree t2) {
				if(Math.abs(t1.getStartLineNum() - vLineNum) > Math.abs(t2.getStartLineNum() - vLineNum)){
					return 1;
				}
				else if (Math.abs(t1.getStartLineNum() - vLineNum) == Math.abs(t2.getStartLineNum() - vLineNum)){
					if(t1.getPos() > t2.getPos()) {
						return 1;
					} else if(t1.getPos() == t2.getPos()) {
						if(t1.getDepth() >= t2.getDepth()) {
							return 1;
						} else return -1;
					} else return -1;
				}
				else return -1;
			}
		});
	}

	private void sort(NodeList nodeList) {
		nodeList.getNodeList().sort(new Comparator<Node>() {
			public int compare(Node node1, Node node2) {
				if(node1.getPos() > node2.getPos()) {
					return 1;
				} else if(node1.getPos() == node2.getPos()) {
					if(node1.getDepth() >= node2.getDepth()) {
						return 1;
					} else return -1;
				} else return -1;
			}
		});
	}

	private void generateSubWarning(NodeList nodeList, ArrayList<SuperWarning> superWarnings) {
		for(int i = 0; i < superWarnings.size(); i ++) {
			SuperWarning superWarning = superWarnings.get(i);
			if(superWarning == null || superWarning.getLineNodes().getNodeList().size() == 0 || nodeList == null || nodeList.getNodeList().size() == 0) {
				continue;
			}
			SuperWarningCollector.Relation relation = findRelation(superWarning.getLineNodes(), nodeList);
			//superWarning is subset of nodeList
			if(relation == SuperWarningCollector.Relation.SubWarning) {
				SuperWarning tmpSW = new SuperWarning(nodeList.getvLineCode(), nodeList.getvNodeCode(), nodeList);
				tmpSW.addSubWarning();
				tmpSW.addSubWarnings(superWarning.getNumOfSubWarnings());
				superWarnings.set(i, tmpSW);
				return;
			}
			//nodeList and superWarning are the same
			else if(relation == SuperWarningCollector.Relation.Equivalent) {
				superWarning.addEqualWarning();
				return;
			}
			//nodeList is subset of superWarning
			relation = findRelation(nodeList, superWarning.getLineNodes());
			if(relation == SuperWarningCollector.Relation.SubWarning) {
				superWarning.addSubWarning();
				return;
			}
		}

		if(nodeList != null)
			superWarnings.add(new SuperWarning(nodeList.getvLineCode(), nodeList.getvNodeCode(), nodeList));
	}

	private SuperWarningCollector.Relation findRelation(NodeList line2, NodeList line1) {
		int nodeNumInLine1 = line1.getNodeList().size();
		int nodeNumInLine2 = line2.getNodeList().size();

		//is line2 subet of line1?
		if(nodeNumInLine1 >= nodeNumInLine2) {
			return isSubset(line2, line1);
		}
		else return SuperWarningCollector.Relation.NULL;
	}

	private SuperWarningCollector.Relation isSubset(NodeList line2, NodeList line1) {
		int numOfNodeLine1 = line1.getNodeList().size();
		int numOfNodeLine2 = line2.getNodeList().size();

		boolean[] included = new boolean[numOfNodeLine2];

		int curIdx = 0;

		for(int i = 0; i < numOfNodeLine2; i ++) {
			Node node = line2.getNodeList().get(i);
			curIdx = line1.contain(node, curIdx);
			if(curIdx == -1)
				return SuperWarningCollector.Relation.NULL;
			else {
				included[i] = true;
			}
		}

		for(boolean b : included) {
			if(b == false) return SuperWarningCollector.Relation.NULL;
		}

		if(numOfNodeLine1 == numOfNodeLine2)
			return SuperWarningCollector.Relation.Equivalent;
		else
			return SuperWarningCollector.Relation.SubWarning;
	}

	private void printProgress(int cnt, int numSuperWarnings) {
		System.out.println("INFO: [Current Progress, # of super warnings]: ["  + cnt + ",  " + numSuperWarnings + "]");
	}

	private void printProgress(int cnt, int numSuperWarnings, int total) {
		if(total / 10 == cnt) {
			System.out.print("10%...");
		}
		else if(total * 2 / 10 == cnt) {
			System.out.print("20%...");
		}
		else if(total * 3 / 10 == cnt) {
			System.out.print("30%...");
		}
		else if(total * 4 / 10 == cnt) {
			System.out.print("40%...");
		}
		else if(total * 5/ 10 == cnt) {
			System.out.print("50%...");
		}
		else if(total * 6 / 10 == cnt) {
			System.out.print("60%...");
		}
		else if(total * 7 / 10 == cnt) {
			System.out.print("70%...");
		}
		else if(total * 8 / 10 == cnt) {
			System.out.print("80%...");
		}
		else if(total * 9 / 10 == cnt) {
			System.out.print("90%...");
		}
		else if(total-1 == cnt) {
			System.out.print("done!\n");
		}
	}

	private ArrayList<SuperWarning> sortByLowFrequency(ArrayList<SuperWarning> superWarnings){
		Collections.sort(superWarnings, new Comparator<SuperWarning>() {
			@Override
			public int compare(SuperWarning set1, SuperWarning set2) {
				return set1.getNumOfEqualWarnings() - set2.getNumOfEqualWarnings();
			}

		});
		return superWarnings;
	}

	public void writeConcreteCodePattern(ArrayList<SuperWarning> sets, int warningsInMethod) {
		String fileName = "./FPC_Patterns_ConcreteCode.csv";

		try(
				BufferedWriter writer = Files.newBufferedWriter(Paths.get(fileName));
				CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT
						.withHeader("Pattern ID", "Pattern", "Context", "# of Eq list",  "# of Frq", "complexity", "Num of Warnings in Method", "NCL"));
		) {
			int cnt = 0;


			for(int i = 0 ; i < sets.size(); i ++) {
				if(sets.get(i) == null) continue;

				ArrayList<Node> cds = sets.get(i).getLineNodes().getNodeList();
				StringBuilder ncl = new StringBuilder();
				for(int j = 0; j < cds.size(); j ++) {
					ncl.append(ASTNode.nodeClassForType(cds.get(j).getType()).getSimpleName()+"(");
					ArrayList<Property> pp = cds.get(j).getParentProperty();
					for(int k = 0; k < pp.size(); k ++) {
						ncl.append(pp.get(k).getTypeName() + "-" + pp.get(k).getProp());
						ncl.append(", ");
					}
					ncl.append("),\n");
				}

				String pattern = sets.get(i).getCode();
				String context = sets.get(i).getContextCode();
				cnt++;
				String patternID = "" + cnt;
				String eqNum = "" + sets.get(i).getNumOfEqualWarnings();
				String f = "" + (sets.get(i).getNumOfEqualWarnings() + sets.get(i).getNumOfSubWarnings());
				String complexity = "" + sets.get(i).getLineNodes().getNodeList().size();
				if(cnt == 1)
					csvPrinter.printRecord(patternID, pattern, context, eqNum, f, complexity, "" + warningsInMethod, ncl.toString());
				else
					csvPrinter.printRecord(patternID, pattern, context, eqNum, f, complexity, "", ncl.toString());
			}

			writer.flush();
			writer.close();
		} catch(IOException e){
			e.printStackTrace();
		}
	}
}
