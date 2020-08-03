package jmo.util.naming;

import java.util.Properties;

import javax.naming.CompoundName;
import javax.naming.Name;
import javax.naming.NameParser;
import javax.naming.NamingException;

public class MemoryNameParser implements NameParser {

	@Override
	public Name parse(String name) throws NamingException {
		return new CompoundName(name, new Properties());
	}
}
