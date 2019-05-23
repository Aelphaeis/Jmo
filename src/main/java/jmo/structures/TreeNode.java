package jmo.structures;

import static java.util.stream.Collectors.toList;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import jmo.patterns.visitor.Visitor;

public class TreeNode<T> implements Iterable<T>{
	private T value = null;
	private final TreeNode<T> parent;
	private final List<TreeNode<T>> children;

	/**
	 * This creates a TreeNode that is intended as a root with no value or
	 * children.
	 */
	public TreeNode() {
		this(null, null);
	}

	public TreeNode(TreeNode<T> parent) {
		this(parent, null);
	}

	public TreeNode(T value) {
		this(null, value);
	}

	public TreeNode(TreeNode<T> parent, T value) {
		this.children = new ArrayList<>();
		this.parent = parent;
		setValue(value);
		
		if(parent != null) {
			parent.children.add(this);
		}
	}

	public boolean hasParent() {
		return getParent() != null;
	}

	public boolean hasValue() {
		return getValue() != null;
	}

	public void addChild(T child) {
		new TreeNode<>(this, child);
	}

	public T child(int i) {
		return getChildren().get(i).getValue();
	}

	public TreeNode<T> node(int i) {
		return getChildren().get(i);
	}

	public void addChildren(Iterable<T> children) {
		children.forEach(this::addChild);
	}

	public List<T> getChildrenValues() {
		return children.stream().map(TreeNode::getValue).collect(toList());
	}

	public <R extends Visitor<TreeNode<T>>> R transverseNodes(R visitor) {
		visitor.visit(this);
		children.forEach(p -> p.transverseNodes(visitor));
		return visitor;
	}

	public Visitor<T> transverse(Visitor<T> visitor) {
		transverseNodes(p -> visitor.visit(p.getValue()));
		return visitor;
	}

	public int getDepth() {
		int lv = -1;
		for (TreeNode<T> curr = this; curr != null; curr = curr.getParent()) {
			lv++;
		}
		return lv;
	}
	
	public <R> TreeNode<R> map(Function<? super T, ? extends R> m) {
		return transverseNodes(new Mapper<R>(m)).getRoot();
	}
	
	@Override
	public Iterator<T> iterator() {
		Collection<T> collection = new ArrayList<>();
		transverse(collection::add);
		return collection.iterator();
	}
	
	/**** Getters and Setters *****/
	public T getValue() {
		return value;
	}

	public void setValue(T value) {
		this.value = value;
	}

	public TreeNode<T> getParent() {
		return parent;
	}

	public List<TreeNode<T>> getChildren() {
		return Collections.unmodifiableList(children);
	}
	
	private class Mapper<R> implements Visitor<TreeNode<T>> {
		private Map<TreeNode<T>, TreeNode<R>> i = new HashMap<>(); // images
		private Function<? super T, ? extends R> m;

		public Mapper(Function<? super T, ? extends R> mapper) {
			this.m = mapper;
		}

		@Override
		public void visit(TreeNode<T> p) {
			i.put(p, new TreeNode<R>(i.get(p.parent), m.apply(p.value)));
		}

		public TreeNode<R> getRoot() {
			return i.values().stream().filter(p -> p.getParent() == null)
					.findFirst().orElseThrow(IllegalStateException::new);
		}
	}
}
