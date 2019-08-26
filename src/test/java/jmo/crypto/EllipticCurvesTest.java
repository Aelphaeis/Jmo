package jmo.crypto;

import static jmo.crypto.EllipticCurves.RECOMMENDED_KEY_SIZE;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.nio.charset.StandardCharsets;
import java.security.InvalidParameterException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;

import org.hamcrest.Matchers;
import org.junit.Test;

import jmo.crypto.EllipticCurves.ECRuntimeException;

public class EllipticCurvesTest {
	public static final int MAX_KEY_SIZE = EllipticCurves.MAX_KEY_SIZE;
	public static final int MIN_KEY_SIZE = EllipticCurves.MIN_KEY_SIZE;
	
	private String pangram = "The quick brown fox jumps over a lazy dog.";

	@Test(expected = InvalidParameterException.class)
	public void generateKeys_belowMin_exception() {
		EllipticCurves.generateKeys(MIN_KEY_SIZE - 1);
	}

	@Test(expected = InvalidParameterException.class)
	public void generateKeys_aboveMax_exception() {
		EllipticCurves.generateKeys(MAX_KEY_SIZE + 1);
	}
	
	@Test
	public void sign_signVerify_valid() {
		byte[] bytes = pangram.getBytes(StandardCharsets.UTF_8);

		KeyPair kp = EllipticCurves.generateKeys(RECOMMENDED_KEY_SIZE);
		byte[] sig = EllipticCurves.sign(kp.getPrivate(), bytes);

		assertTrue(EllipticCurves.verify(kp.getPublic(), bytes, sig));
		assertThat(104, Matchers.greaterThanOrEqualTo(sig.length));
		assertThat(101, Matchers.lessThanOrEqualTo(sig.length));
	}

	@Test
	public void sign_signVerify_valid2() {
		byte[] bytes = pangram.getBytes(StandardCharsets.UTF_8);

		KeyPair kp = EllipticCurves.generateKeys(RECOMMENDED_KEY_SIZE);
		int result = kp.getPrivate().getEncoded().length;
		assertThat(79, Matchers.lessThanOrEqualTo(result));
		assertEquals(120, kp.getPublic().getEncoded().length);

		byte[] sig = EllipticCurves.sign(kp.getPrivate(), bytes);

		assertTrue(EllipticCurves.verify(kp.getPublic(), bytes, sig));
		assertThat(104, Matchers.greaterThanOrEqualTo(sig.length));
		assertThat(101, Matchers.lessThanOrEqualTo(sig.length));
	}

	@Test(expected = ECRuntimeException.class)
	public void safeVerify_invalidKey_exception() {
		byte[] bytes = pangram.getBytes(StandardCharsets.UTF_8);
		byte[] sig;

		KeyPair kp = EllipticCurves.generateKeys();
		sig = EllipticCurves.sign(kp.getPrivate(), bytes);

		EllipticCurves.verify(kp.getPublic(), sig, "t".getBytes());
	}

	@Test(expected = ECRuntimeException.class)
	public void safeSign_invalidKey_exception()
			throws NoSuchAlgorithmException {
		KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
		KeyPair kp = kpg.generateKeyPair();

		byte[] bytes = pangram.getBytes(StandardCharsets.UTF_8);
		EllipticCurves.sign(kp.getPrivate(), bytes);
	}
}
