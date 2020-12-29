import java.util.ArrayList;

import org.junit.jupiter.api.Test;

import edu.handong.csee.isel.fcminer.fpcollector.pattern.Pattern;
import edu.handong.csee.isel.fcminer.fpcollector.pattern.PatternGenerator;


public class testPatternAbstraction {
	@Test
	public void patternAbstractTester() {
		PatternGenerator pg = new PatternGenerator(null);
		ArrayList<Pattern> ps = new ArrayList<>();
		ArrayList<Pattern> pL2 ;
		ArrayList<Pattern> pL3 ;
		
		Pattern p1 = new Pattern(5, "A(a), B(b), C(c), ", "something");
		Pattern p2 = new Pattern(3, "A(a), D(d), C(c), ", "something");
		
		ps.add(p1);
		ps.add(p2);		
		
		p1.abstractL2();
		p1.abstractL3();
		p2.abstractL2();
		p2.abstractL3();
		
//		for(Pattern pa : p1.getPatternL2()) {			
//			System.out.println("L2: " + pa.getPattern().getSecond());		
//		}
//		
//		for(Pattern pa : p1.getPatternL3()) {
//			System.out.println("L3 : " + pa.getPattern().getSecond());
//		}
		
		pL2 = pg.maxPoolingL2(ps);
		pL3 = pg.maxPoolingL3(ps);
		
		for(Pattern p: pL2) {
			System.out.println("(L2)Frq: " + p.getPattern().getFirst() + " Pattern: " + p.getPattern().getSecond());
		}
		
		for(Pattern p: pL3) {
			System.out.println("(L3)Frq: " + p.getPattern().getFirst() + " Pattern: " + p.getPattern().getSecond());
		}
		
		
		System.out.println();
		
		
		
	}
}
