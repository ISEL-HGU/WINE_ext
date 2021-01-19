//import java.util.ArrayList;
//
//import org.junit.jupiter.api.Test;
//
//import edu.handong.csee.isel.fcminer.fpcollector.pattern.Pattern;
//import edu.handong.csee.isel.fcminer.fpcollector.pattern.PatternGenerator;
//
//
//public class testPatternAbstraction {
//	@Test
//	public void patternAbstractTester() {
//		PatternGenerator pg = new PatternGenerator(null);
//		ArrayList<Pattern> ps = new ArrayList<>();
//		ArrayList<Pattern> pSum ;
//		
//		
//		Pattern p1 = new Pattern(5, "A(a), B(b), C(c), ", "something");
//		Pattern p2 = new Pattern(3, "A(a), D(d), C(c), ", "something");
//		Pattern p3 = new Pattern(2, "A(a), C(c), ", "something");
//		
//		ps.add(p1);
//		ps.add(p2);
//		ps.add(p3);
//		
//		pSum = pg.patternSumUp(ps);
//		
//		for(int i = 0 ; i < ps.size(); i ++) {
//			System.out.println("Frq: " + pSum.get(i).getPattern().getFirst() + ", Pat: " + pSum.get(i).getPattern().getSecond());
//		}
//
//
//		
//		System.out.println();
//		
//		
//		
//	}
//}
