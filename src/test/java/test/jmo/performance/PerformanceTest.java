package test.jmo.performance;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import jmo.patterns.visitor.Stringifier;
import jmo.structures.TreeAdaptor;
import jmo.structures.TreeNode;

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
		List<Integer> range = IntStream.rangeClosed(1, 16)
				.mapToObj(p-> Integer.valueOf(p)).collect(Collectors.toList());
		
		TreeNode<Integer> root = TreeAdaptor.toTree(range, p -> {
			int sqrt = (int) Math.round(Math.sqrt(p));
			return p == sqrt ? 0 : sqrt;
		});

		A = new Candidate() {

			@Override
			public String getName() {
				return "visitor with reserve";
			}

			@Override
			public void doAction() {
				root.transverseNodes(new Stringifier<>());
			}
		};
		B = new Candidate() {

			@Override
			public String getName() {
				return "visitor with insert";
			}

			@Override
			public void doAction() {
				root.transverseNodes(new Stringifier<>());
			}
		};

	}

	@Test
	@Ignore
	public void performanceTest() {
		run(A);
		run(B);
		run(A);
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
