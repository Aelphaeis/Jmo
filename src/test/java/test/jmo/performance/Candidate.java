package test.jmo.performance;

@FunctionalInterface
public interface Candidate {

	void doAction();

	default String getName() {
		return getClass().getName();
	}

	default void warmup() {
		int limit = getWarmupIterations();
		for (int i = 0; i < limit; i++) {
			doAction();
		}
	}

	default int getWarmupIterations() {
		return 10000;
	}

	default int getPerformanceIterations() {
		return 1000000;
	}

	default long checkPerformance() {
		long start = System.currentTimeMillis();
		int performanceCount = getPerformanceIterations();
		for (int i = 0; i < performanceCount; i++) {
			doAction();
		}
		long end = System.currentTimeMillis();
		return end - start;
	}
}
