package jmo.patterns.visitor;

public interface Visitor<T> {
	void visit(T element);
}
