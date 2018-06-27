package jmo.patterns.visitor;

@FunctionalInterface
public interface Visitor<T> {
	void visit(T element);
}
