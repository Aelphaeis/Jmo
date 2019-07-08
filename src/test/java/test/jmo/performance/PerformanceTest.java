package test.jmo.performance;

import static jmo.util.NamingConventions.toScreamingSnakeCaseFromCamelCase;
import static jmo.util.NamingConventions.toScreamingSnakeCaseFromCamelCase2;
import static jmo.util.NamingConventions.toScreamingSnakeCaseFromCamelCase3;
import static jmo.util.NamingConventions.toScreamingSnakeCaseFromCamelCase4;
import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PerformanceTest {
	private static final String LC_HELLO_WORLD = "HelloWorld!";
	private static final String HELLO_WORLD = "HELLO_WORLD!";

	private static final Logger logger = LoggerFactory.getLogger("debug");

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
				String s = LC_HELLO_WORLD;
				String result = toScreamingSnakeCaseFromCamelCase(s);
				assertEquals(HELLO_WORLD, result);
			}
		};
		B = new Candidate() {

			@Override
			public String getName() {
				return "B";
			}

			@Override
			public void doAction() {
				String s = LC_HELLO_WORLD;
				String result = toScreamingSnakeCaseFromCamelCase2(s);
				assertEquals(HELLO_WORLD, result);
			}
		};
		C = new Candidate() {

			@Override
			public String getName() {
				return "C";
			}

			@Override
			public void doAction() {
				String s = LC_HELLO_WORLD;
				assertEquals(HELLO_WORLD,
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
				String s = LC_HELLO_WORLD;
				assertEquals(HELLO_WORLD,
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
