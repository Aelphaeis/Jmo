package jmo.structures;

import static java.util.stream.Collectors.toList;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import jmo.patterns.visitor.Visitor;

public class TreeNode<T> {
	private T value = null;
	private final TreeNode<T> parent;
	private final List<TreeNode<T>> children;

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

	public Visitor<TreeNode<T>> transverseNodes(Visitor<TreeNode<T>> visitor) {
		visitor.visit(this);
		children.forEach(p -> p.transverseNodes(visitor));
		return visitor;
	}

	public Visitor<T> transverse(Visitor<T> visitor) {
		transverseNodes(p -> visitor.visit(p.getValue()));
		return visitor;
	}

	public int getLevel() {
		int lv = -1;
		for (TreeNode<T> curr = this; curr != null; curr = curr.getParent()) {
			lv++;
		}
		return lv;
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
}
