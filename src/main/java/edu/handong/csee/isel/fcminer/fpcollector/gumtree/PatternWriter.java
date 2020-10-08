package edu.handong.csee.isel.fcminer.fpcollector.gumtree;

public class PatternWriter {
	
	public void writePatterns(Patterns patterns) {
		System.out.println(patterns.getFixed().path);
		for(Pattern pattern: patterns.getPatterns()) {
			pattern.printPattern();
			System.out.println("\n\n\n");
		}
	}
}
