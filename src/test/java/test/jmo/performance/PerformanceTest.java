package test.jmo.performance;

import static jmo.util.NamingConventions.*;
import static org.junit.Assert.assertEquals;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

public class PerformanceTest {
	Logger logger = LogManager.getLogger("debug");

	private static final String ITFORMAT = "Candidate [%s] run %s : %s ms";
	private static final String TIFORMAT = "Canadidate [%s] ran %s times";
	private static final String TOFORMAT = "Candidate [%s] total :  %s ms";
	private static final String AVGFORMAT = "Candidate [%s] average : %s ms";

	Candidate A;
	Candidate B;
	Candidate C;
	Candidate D;

	@Before
	public void setup() {
		A = new Candidate() {

			@Override
			public String getName() {
				return "A";
			}

			@Override
			public void doAction() {
				String s = "HelloWorld!";
				assertEquals("HELLO_WORLD!",
						toScreamingSnakeCaseFromCamelCase(s));
			}
		};
		B = new Candidate() {

			@Override
			public String getName() {
				return "B";
			}

			@Override
			public void doAction() {
				String s = "HelloWorld!";
				assertEquals("HELLO_WORLD!",
						toScreamingSnakeCaseFromCamelCase2(s));
			}
		};
		C = new Candidate() {

			@Override
			public String getName() {
				return "C";
			}

			@Override
			public void doAction() {
				String s = "HelloWorld!";
				assertEquals("HELLO_WORLD!",
						toScreamingSnakeCaseFromCamelCase3(s));
			}
		};
		D = new Candidate() {

			@Override
			public String getName() {
				return "D";
			}

			@Override
			public void doAction() {
				String s = "HelloWorld!";
				assertEquals("HELLO_WORLD!",
						toScreamingSnakeCaseFromCamelCase4(s));
			}
		};

	}

	@Test
	@Ignore
	public void performanceTest() {
		A.doAction();
		B.doAction();
		C.doAction();
		D.doAction();

		run(A);
		run(B);
		run(C);
		run(D);
	}

	void run(Candidate can) {
		can.warmup();
		int limit = 10;
		long total = 0;

		for (int i = 0; i < limit; i++) {
			long duration = can.checkPerformance();
			logger.debug(String.format(ITFORMAT, can.getName(), i, duration));
			total += duration;
		}

		logger.info(String.format(TIFORMAT, can.getName(), limit));
		logger.info(String.format(TOFORMAT, can.getName(), total));
		logger.info(String.format(AVGFORMAT, can.getName(), total / limit));
	}
}
