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
	
	private static final Logger logger = LoggerFactory.getLogger("debug");

	private static final String LC_HELLO_WORLD = "HelloWorld!";
	private static final String HELLO_WORLD = "HELLO_WORLD!";
	
	private static final String ITFORMAT = "Candidate [%s] run %s : %s ms";
	private static final String TIFORMAT = "Canadidate [%s] ran %s times";
	private static final String TOFORMAT = "Candidate [%s] total :  %s ms";
	private static final String AVGFORMAT = "Candidate [%s] average : %s ms";
	Candidate a;
	Candidate b;
	Candidate c;
	Candidate d;

	@Before
	@Ignore
	public void setup() {
		a = () -> {
			String s = LC_HELLO_WORLD;
			assertEquals(HELLO_WORLD, toScreamingSnakeCaseFromCamelCase(s));
		};
		b = () -> {
			String s = LC_HELLO_WORLD;
			assertEquals(HELLO_WORLD, toScreamingSnakeCaseFromCamelCase2(s));
		};
		c = () -> {
			String s = LC_HELLO_WORLD;
			assertEquals(HELLO_WORLD, toScreamingSnakeCaseFromCamelCase3(s));
		};
		d = () -> {
			String s = LC_HELLO_WORLD;
			assertEquals(HELLO_WORLD, toScreamingSnakeCaseFromCamelCase4(s));
		};
	}

	@Test
	public void performanceTest() {
		a.doAction();
		b.doAction();
		c.doAction();
		d.doAction();

		run(a);
		run(b);
		run(c);
		run(d);
	}

	void run(Candidate can) {
		can.warmup();
		int limit = 10;
		long total = 0;

		if (logger.isDebugEnabled()) {
			for (int i = 0; i < limit; i++) {
				long dur = can.checkPerformance();
				logger.debug(String.format(ITFORMAT, can.getName(), i, dur));
				total += dur;
			}
		}

		if (logger.isInfoEnabled()) {
			logger.info(String.format(TIFORMAT, can.getName(), limit));
			logger.info(String.format(TOFORMAT, can.getName(), total));
			logger.info(String.format(AVGFORMAT, can.getName(), total / limit));
		}

	}
}
