package jmo.structures;

import java.util.ArrayList;
import java.util.List;

import jmo.patterns.visitor.Visitor;

public class TreeNode<T> {
	private T value = null;
	private TreeNode<T> parent = null;
	private List<TreeNode<T>> children = null;
	
	public TreeNode(){
		setChildren(new ArrayList<TreeNode<T>>());
	}
	
	public TreeNode(TreeNode<T> parent){
		this();
		this.parent = parent;
	}
	
	public TreeNode(TreeNode<T> parent, T value) {
		this(parent);
		this.value = value;
	}
	
	public boolean hasParent(){
		return getParent() != null;
	}
	
	public boolean hasValue(){
		return getValue() != null;
	}

	public void addChild(T child){
		TreeNode<T> node = new TreeNode<>();
		node.setValue(child);
		node.setParent(this);
		getChildren().add(node);
	}
	
	public void addChildren(List<T> children){
		for(T v : children){
			addChild(v);
		}
	}
	
	public List<T> getChildrenValues(){
		List<T> output = new ArrayList<>();
		for(TreeNode<T> child : getChildren()){
			output.add(child.getValue());
		}
		return output;
	}
	
	public void transverseNodes(Visitor<TreeNode<T>> visitor){
		transverseNodes(this, visitor);
	}
	
	private void transverseNodes(TreeNode<T> n, Visitor<TreeNode<T>> visitor){
		visitor.visit(n);
		for(TreeNode<T> node : n.getChildren()){
			n.transverseNodes(node, visitor);
		}
	}
	
	public void transverse(Visitor<T> visitor){
		transverse(this, visitor);
	}
	
	private void transverse(TreeNode<T> n, Visitor<T> action){
		if( n.getValue() != null){
			action.visit(n.getValue());
		}
		for(TreeNode<T> node : n.getChildren()){
			n.transverse(node, action);
		}
	}
	
	public int getLevel(){
		int lv = 0;
		TreeNode<T> current =  this;
		while(current!= null) {
			current = current.getParent();
			lv++;
		}
		return lv;
	}
	
	/****     Getters and Setters    *****/
	public T getValue() {
		return value;
	}

	public void setValue(T value) {
		this.value = value;
	}

	public TreeNode<T> getParent() {
		return parent;
	}

	public void setParent(TreeNode<T> parent) {
		this.parent = parent;
	}

	public List<TreeNode<T>> getChildren() {
		return children;
	}

	public void setChildren(List<TreeNode<T>> children) {
		this.children = children;
	}
}
