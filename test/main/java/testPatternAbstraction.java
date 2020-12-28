import java.util.ArrayList;

import org.junit.jupiter.api.Test;

import edu.handong.csee.isel.fcminer.fpcollector.pattern.Pattern;


public class testPatternAbstraction {
	@Test
	public void patternAbstractTester() {
		Pattern p = new Pattern(5, "A(a), B(b), C(c), ", "something");
		System.out.println(p.getPattern().getFirst());
		System.out.println(p.getPattern().getSecond());
		
		p.abstractL2();
		p.abstractL3();
		
		for(Pattern pa : p.getPatternL2()) {			
			System.out.println("L2: " + pa.getPattern().getSecond());
			
		}
		
		for(Pattern pa : p.getPatternL3()) {
			System.out.println("L3 : " + pa.getPattern().getSecond());
		}
		
		
		System.out.println();
		
	}
}
