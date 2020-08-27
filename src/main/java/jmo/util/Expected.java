package jmo.util;

import java.util.function.BooleanSupplier;

public class Expected {

	private static final int DEFAULT_INTERVAL = 500;
	private static final int DEFAULT_TIMEOUT = 5000;

	public static boolean expect(BooleanSupplier p) {
		return expect(p, DEFAULT_TIMEOUT);
	}

	public static boolean expect(BooleanSupplier p, int timeout) {
		return expect(p, timeout, DEFAULT_INTERVAL);
	}

	public static boolean expect(BooleanSupplier p, int timeout, int interval) {
		try {
			long end = System.currentTimeMillis() + timeout;
			for (long i = 0; i < end; i = System.currentTimeMillis()) {
				long started = System.currentTimeMillis();
				if (p.getAsBoolean()) {
					return true;
				}
				long duration = System.currentTimeMillis() - started;
				Thread.sleep(interval - duration);
			}
		}
		catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		}
		return false;
	}

	public static void expectOrThrow(BooleanSupplier bs) {
		expectOrThrow(bs, DEFAULT_TIMEOUT);
	}

	public static void expectOrThrow(BooleanSupplier p, int time) {
		expectOrThrow(p, time, DEFAULT_INTERVAL);
	}

	public static void expectOrThrow(BooleanSupplier p, int time, int freq) {
		if (!expect(p, time, freq)) {
			String err = "timeout exceeded for expectation";
			throw new UnexpectedResultException(err);
		}
	}

	public static class UnexpectedResultException extends RuntimeException {

		private static final long serialVersionUID = 1L;

		private UnexpectedResultException(String message) {
			super(message);
		}
	}

	private Expected() {
		throw new IllegalStateException("Utility Method");
	}
}
