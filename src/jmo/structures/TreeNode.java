package jmo.structures;

import java.util.ArrayList;
import java.util.List;

public class TreeNode<T>{
	private T value = null;
	private TreeNode<T> parent = null;
	private List<TreeNode<T>> children = null;
	
	public TreeNode(){
		setChildren(new ArrayList<TreeNode<T>>());
	}
	
	public boolean hasParent(){
		return parent != null;
	}
	
	public void addChildren(List<TreeNode<T>> children){
		getChildren().addAll(children);
	}
	
	public void transverse(Callable<T> action){
		transverse(this, action);
	}
	
	private void transverse(TreeNode<T> n, Callable<T> action){
		if( value != null){
			action.function(value);
		}
		for(TreeNode<T> node : children){
			transverse(node, action);
		}
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
