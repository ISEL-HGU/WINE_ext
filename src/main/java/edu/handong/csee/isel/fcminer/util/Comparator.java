package edu.handong.csee.isel.fcminer.util;

import java.util.ArrayList;

import edu.handong.csee.isel.fcminer.saresultminer.sat.pmd.Alarm;

public class Comparator {
	ArrayList<Alarm> fixedAlarms = new ArrayList<>();
	ArrayList<Alarm> maintainedAlarms = new ArrayList<>();
	ArrayList<Alarm> newGeneratedAlarms = new ArrayList<>();
	
	public void init() {
		fixedAlarms.clear();
		maintainedAlarms.clear();
		newGeneratedAlarms.clear();
	}
	
	public void compareAlarms(ArrayList<Alarm> newAlarms, ArrayList<Alarm> changedAlarms, ArrayList<Alarm> unchangedAlarms){
		ArrayList<Alarm> tempAlarms = new ArrayList<>();
		
		tempAlarms.addAll(changedAlarms);
		int idx = 0;
		for(Alarm fixedAlarm : tempAlarms) {
			if(fixedAlarm.getStartLineNum().equals("-1") || fixedAlarm.getStartLineNum().equals("0")) {
				fixedAlarms.add(fixedAlarm);
				changedAlarms.remove(idx);
				idx--;
			}
			idx++;
		}
		tempAlarms.clear();
		idx = 0;
		
		if(newAlarms.size() == 0) return;
		
		tempAlarms.addAll(newAlarms);		
		for(Alarm alarm : tempAlarms) {
			for(Alarm changedAlarm : changedAlarms) {
				if(alarm.getDir().trim().equals(changedAlarm.getDir().trim())) {
					if(alarm.getStartLineNum().equals(changedAlarm.getStartLineNum())) {
						maintainedAlarms.add(changedAlarm);
						newAlarms.remove(idx);
						idx--;
						break;						
					}	
				}
			}
			idx++;
		}
		tempAlarms.clear();
		idx = 0;
		
		if(newAlarms.size() == 0) return;		
		
		tempAlarms.addAll(newAlarms);
		for(Alarm alarm : tempAlarms) {
			for(Alarm unchangedAlarm : unchangedAlarms) {
				if(alarm.getDir().trim().equals(unchangedAlarm.getDir().trim()) && alarm.getStartLineNum().trim().equals(unchangedAlarm.getStartLineNum().trim())) {
					maintainedAlarms.add(unchangedAlarm);					
					newAlarms.remove(idx);				
					idx--;
					break;
				}
			}
			idx++;
		}
		tempAlarms.clear();
		
		newGeneratedAlarms.addAll(newAlarms);
	}
	
	public ArrayList<Alarm> getFixedAlarms(){
		return fixedAlarms;
	}
	
	public ArrayList<Alarm> getMaintainedAlarms(){
		return maintainedAlarms;
	}
	
	public ArrayList<Alarm> getNewGeneratedAlarms(){
		return newGeneratedAlarms;
	}	
}
