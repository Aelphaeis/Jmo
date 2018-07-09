package jmo.util;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.security.SecureRandom;
import java.util.Random;

public class Randomizer {
	public static final String DIGITS = "0123456789";
	public static final String LOWERCASE = "abcdefghijklmnopqrstuvwxyz";
	public static final String UPPERCASE = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
	public static final String ALPHANUMERIC = LOWERCASE + UPPERCASE + DIGITS;
	
	private final Random generator;
	
	public Randomizer(){
		this(new SecureRandom());
	}
	
	public Randomizer(Random r){
		generator = r;
	}
	
	public byte getByte(){
		byte [] bArr = new byte[1];
		generator.nextBytes(bArr);
		return bArr[0];
	}
	
	public short getShort(){
		byte [] bArr = new byte[2];
		generator.nextBytes(bArr);
		return ByteBuffer.wrap(bArr).order(ByteOrder.LITTLE_ENDIAN).getShort();
	}
	
	public int getInt(){
		return generator.nextInt();
	}
	
	public long getLong(){
		byte [] bArr = new byte[8];
		generator.nextBytes(bArr);
		return ByteBuffer.wrap(bArr).order(ByteOrder.LITTLE_ENDIAN).getLong();
	}
	
	public float getFloat(){
		byte[] bArr = new byte[4];
		generator.nextBytes(bArr);
		return  ByteBuffer.wrap(bArr).order(ByteOrder.LITTLE_ENDIAN).getFloat();
	}
	
	public double getDouble(){
		byte [] bArr = new byte[8];
		generator.nextBytes(bArr);
		return ByteBuffer.wrap(bArr).order(ByteOrder.LITTLE_ENDIAN).getDouble();
	}
	
	public char getChar(){
		byte [] bArr = new byte[2];
		generator.nextBytes(bArr);
		return ByteBuffer.wrap(bArr).order(ByteOrder.LITTLE_ENDIAN).getChar();
	}
	
	public String getString(final int charCount){
		return getString(charCount, ALPHANUMERIC);
	}
	
	public String getString(final int charCount, final String source) {
		StringBuilder builder = new StringBuilder();
		final int len = source.length();
		for(int i = 0; i < charCount; i++){
			builder.append(source.charAt(generator.nextInt(len)));
		}
		return builder.toString();
	}
}
