package jmo.crypto;

import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

public class EllipticCurves {
	public static final int MAX_KEY_SIZE = 571;
	public static final int MIN_KEY_SIZE = 112;
	//key size of roughly 101 - 104 
	public static final int RECOMMENDED_KEY_SIZE = 384;

	public static final String KEY_ALGO = "EC";
	public static final String SIG_ALGO = "SHA384withECDSA";
	
	public static KeyPair generateKeys() {
		return generateKeys(RECOMMENDED_KEY_SIZE);
	}

	public static KeyPair generateKeys(int size) {
		KeyPairGenerator kpg = generator();
		kpg.initialize(size);
		return kpg.genKeyPair();
	}

	public static PrivateKey buildPrivateKey(byte[] key)
			throws InvalidKeySpecException {
		KeyFactory factory = keyFactory();
		return factory.generatePrivate(new PKCS8EncodedKeySpec(key));
	}

	public static PrivateKey safeBuildPrivateKey(byte[] key) {
		try {
			return buildPrivateKey(key);
		} catch (InvalidKeySpecException e) {
			throw new ECRuntimeException(e);
		}
	}

	public static PublicKey buildPublicKey(byte[] key)
			throws InvalidKeySpecException {
		KeyFactory factory = keyFactory();
		return factory.generatePublic(new X509EncodedKeySpec(key));
	}

	public static PublicKey safeBuildPublicKey(byte[] key) {
		try {
			return buildPublicKey(key);
		} catch (InvalidKeySpecException e) {
			throw new ECRuntimeException(e);
		}
	}

	public static byte[] sign(PrivateKey key, byte[] digest)
			throws InvalidKeyException, SignatureException {
		Signature signature = signature();
		signature.initSign(key);
		signature.update(digest);
		return signature.sign();
	}

	public static byte[] safeSign(PrivateKey key, byte[] digest) {
		try {
			return sign(key, digest);
		} catch (InvalidKeyException | SignatureException e) {
			String err = "Error occurred signing digest";
			throw new ECRuntimeException(err, e);
		}
	}
	/**
	 * Uses a public key to validate content signature. Returns true if
	 * the signature is valid, otherwise false.
	 */
	public static boolean verify(PublicKey key, byte[] digest, byte[] sig)
			throws InvalidKeyException, SignatureException {
		Signature signature = signature();
		signature.initVerify(key);
		signature.update(digest);
		return signature.verify(sig);
	}

	public static boolean safeVerify(PublicKey key, byte[] digest, byte[] sig) {
		try {
			return verify(key, digest, sig);
		} catch (InvalidKeyException | SignatureException e) {
			String err = "Error occurred verifying digest";
			throw new ECRuntimeException(err, e);
		}
	}

	private static Signature signature() {
		try {
			return Signature.getInstance(SIG_ALGO);
		} catch (NoSuchAlgorithmException e) {
			// Should support ECDSA
			throw new IllegalStateException(e);
		}
	}

	private static KeyPairGenerator generator() {
		try {
			return KeyPairGenerator.getInstance(KEY_ALGO);
		} catch (NoSuchAlgorithmException e) {
			// Should support EC.
			throw new IllegalStateException(e);
		}
	}

	private static KeyFactory keyFactory() {
		try {
			return KeyFactory.getInstance(KEY_ALGO);
		} catch (NoSuchAlgorithmException e) {
			// Should support EC.
			throw new IllegalStateException(e);
		}
	}

	private EllipticCurves() {
		// utility class
	}
	
	public static class ECRuntimeException extends RuntimeException{

		private static final long serialVersionUID = 1L;

		public ECRuntimeException(String message, Throwable cause) {
			super(message, cause);
		}

		public ECRuntimeException(Throwable cause) {
			super(cause);
		}
	}
}
