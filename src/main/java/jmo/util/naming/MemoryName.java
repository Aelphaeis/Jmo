package jmo.util.naming;

import java.util.Enumeration;

import javax.naming.InvalidNameException;
import javax.naming.Name;

public class MemoryName implements Name {

	private static final long serialVersionUID = 1L;

	@Override
	public int compareTo(Object obj) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int size() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean isEmpty() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Enumeration<String> getAll() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String get(int posn) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Name getPrefix(int posn) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Name getSuffix(int posn) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean startsWith(Name n) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean endsWith(Name n) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Name addAll(Name suffix) throws InvalidNameException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Name addAll(int posn, Name n) throws InvalidNameException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Name add(String comp) throws InvalidNameException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Name add(int posn, String comp) throws InvalidNameException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object remove(int posn) throws InvalidNameException {
		// TODO Auto-generated method stub
		return null;
	}

	public Object clone() {
		return this;
	}
}
