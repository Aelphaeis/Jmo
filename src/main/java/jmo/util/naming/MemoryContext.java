package jmo.util.naming;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.Objects;

import javax.naming.Binding;
import javax.naming.Context;
import javax.naming.Name;
import javax.naming.NameAlreadyBoundException;
import javax.naming.NameClassPair;
import javax.naming.NameParser;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;

public class MemoryContext implements Context {

	public static final String PREFIX = "java:";// comp/env/jdbc/database"
	private static final String NIY = "Not Implemented Yet";
	private final Map<String, Object> bindings;
	private Hashtable<?, ?> environment;

	public MemoryContext() {
		this(new Hashtable<>());
	}

	protected MemoryContext(Hashtable<?, ?> environment) {
		this.environment = Objects.requireNonNull(environment);
		this.bindings = new HashMap<>();
	}

	@Override
	public Object lookup(Name name) throws NamingException {
		Objects.requireNonNull(name);
		return lookup(name.toString());
	}

	@Override
	public Object lookup(String name) throws NamingException {
		Objects.requireNonNull(name);
		return bindings.get(name);
	}

	@Override
	public void bind(Name name, Object obj) throws NamingException {
		bind(name.toString(), obj);
	}

	@Override
	public void bind(String name, Object obj) throws NamingException {
		if (bindings.containsKey(name)) {
			String err = "object has already been bound to name : %s";
			throw new NameAlreadyBoundException(err);
		}
		bindings.put(name, obj);
	}

	@Override
	public void rebind(Name name, Object obj) throws NamingException {
		rebind(name.toString(), obj);
	}

	@Override
	public void rebind(String name, Object obj) throws NamingException {
		bindings.put(name, obj);
	}

	@Override
	public void unbind(Name name) throws NamingException {
		unbind(name.toString());

	}

	@Override
	public void unbind(String name) throws NamingException {
		bindings.remove(name);
	}

	@Override
	public void rename(Name oldName, Name newName) throws NamingException {
		rename(oldName.toString(), newName.toString());

	}

	@Override
	public void rename(String oldName, String newName) throws NamingException {
		Object value = lookup(oldName);
		unbind(oldName);
		bind(newName, value);
	}

	@Override
	public NamingEnumeration<NameClassPair> list(Name name)
			throws NamingException {
		return list(name.toString());
	}

	@Override
	public NamingEnumeration<NameClassPair> list(String name)
			throws NamingException {
		throw new UnsupportedOperationException(NIY);
	}

	@Override
	public NamingEnumeration<Binding> listBindings(Name name)
			throws NamingException {
		return listBindings(name.toString());
	}

	@Override
	public NamingEnumeration<Binding> listBindings(String name)
			throws NamingException {
		throw new UnsupportedOperationException(NIY);
	}

	@Override
	public void destroySubcontext(Name name) throws NamingException {
		destroySubcontext(name.toString());
	}

	@Override
	public void destroySubcontext(String name) throws NamingException {
		throw new UnsupportedOperationException(NIY);

	}

	@Override
	public Context createSubcontext(Name name) throws NamingException {
		return createSubcontext(name.toString());
	}

	@Override
	public Context createSubcontext(String name) throws NamingException {
		throw new UnsupportedOperationException(NIY);
	}

	@Override
	public Object lookupLink(Name name) throws NamingException {
		return lookupLink(name.toString());
	}

	@Override
	public Object lookupLink(String name) throws NamingException {
		throw new UnsupportedOperationException(NIY);
	}

	@Override
	public NameParser getNameParser(Name name) throws NamingException {
		return getNameParser(name.toString());
	}

	@Override
	public NameParser getNameParser(String name) throws NamingException {
		return new MemoryNameParser();
	}

	@Override
	public Name composeName(Name name, Name prefix) throws NamingException {
		throw new UnsupportedOperationException(NIY);
	}

	@Override
	public String composeName(String name, String prefix)
			throws NamingException {
		throw new UnsupportedOperationException(NIY);
	}

	@Override
	public Object addToEnvironment(String propName, Object propVal)
			throws NamingException {
		throw new UnsupportedOperationException(NIY);
	}

	@Override
	public Object removeFromEnvironment(String propName) {
		return environment.remove(propName);
	}

	@Override
	public Hashtable<?, ?> getEnvironment() throws NamingException {
		return environment;
	}

	@Override
	public void close() {
		// no resources to release
	}

	@Override
	public String getNameInNamespace() throws NamingException {
		throw new UnsupportedOperationException(NIY);
	}
}
