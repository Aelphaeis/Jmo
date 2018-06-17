package test.jmo.performance;


import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;

import jmo.util.Strings;

public class PerformanceTest {
	Logger logger = LogManager.getLogger(PerformanceTest.class);
	
	private static final String ITFORMAT = "Candidate [%s] run %s : %s ms";
	private static final String TIFORMAT = "Canadidate [%s] ran %s times";
	private static final String TOFORMAT = "Candidate [%s] total :  %s ms";
	private static final String AVGFORMAT = "Candidate [%s] average : %s ms";
	
	Candidate A;
	Candidate B;
	
	@Before
	public void setup() {
		A = new Candidate() {
			@Override
			public String getName() {
				return "remove w substring";
			}
			
			@Override
			public void doAction() {
				Strings.remove("chicken", 'c');
			}
		};
		B = new Candidate() {
			
			@Override
			public String getName() {
				return "remove /w split";
			}
			
			@Override
			public void doAction() {
			}
		};
		
		
	}
	
	@Test
	public void performanceTest() {
		run(A);
		run(B);
	}
	
	
	void run (Candidate can) {
		can.warmup();
		int limit = 10;
		long total = 0;
		
		for(int i = 0; i < limit; i ++) {
			long duration = can.checkPerformance();
			logger.debug(String.format(ITFORMAT, can.getName(), i, duration));
			total += duration;
		}
		
		logger.info(String.format(TIFORMAT, can.getName(), limit));
		logger.info(String.format(TOFORMAT, can.getName(), total));
		logger.info(String.format(AVGFORMAT, can.getName(), total/limit));
	}
}
