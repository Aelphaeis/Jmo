package jmo.util;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.security.SecureRandom;

public class Randomizer {
	private final static String chars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
	SecureRandom generator;
	
	public Randomizer(){
		generator = new SecureRandom();
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
	
	public String getString(int characterCount){
		String s = "";
		for(int i = 0; i < characterCount; i++){
			s += chars.charAt(generator.nextInt(chars.length()));
		}
		return s;
	}
}
