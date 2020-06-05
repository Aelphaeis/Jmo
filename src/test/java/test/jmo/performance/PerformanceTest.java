package test.jmo.performance;

import static jmo.util.NamingConventions.toScreamingSnakeCaseFromCamelCase;
import static jmo.util.NamingConventions.toScreamingSnakeCaseFromCamelCase2;
import static jmo.util.NamingConventions.toScreamingSnakeCaseFromCamelCase3;
import static jmo.util.NamingConventions.toScreamingSnakeCaseFromCamelCase4;
import static org.junit.Assert.assertEquals;

import java.util.LongSummaryStatistics;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PerformanceTest {

	private static final Logger logger = LoggerFactory
			.getLogger(PerformanceTest.class);
	private static final String LC_HELLO_WORLD = "HelloWorld!";
	private static final String HELLO_WORLD = "HELLO_WORLD!";
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
		run(a, b, c, d);
	}

	private void run(Candidate... candidates) {
		for (int i = 0; i < candidates.length; i++) {
			candidates[i].doAction();
		}

		for (int i = 0; i < candidates.length; i++) {
			run(candidates[i]);
		}
	}

	private void run(Candidate can) {
		can.warmup();

		LongSummaryStatistics lss = new LongSummaryStatistics();

		String name = this.c.getName();
		for (int i = 0; i < 10; i++) {
			long dur = can.checkPerformance();
			lss.accept(dur);
			if (logger.isDebugEnabled()) {
				logger.debug(name + ":" + i + ":" + dur);
			}
		}
		logger.info(can.getName() + " : " + lss);
	}
}
