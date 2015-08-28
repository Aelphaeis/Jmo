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
		return getParent() != null;
	}
	
	public boolean hasValue(){
		return getValue() != null;
	}

	public void addChild(T child){
		TreeNode<T> node = new TreeNode<T>();
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
		List<T> output = new ArrayList<T>();
		for(TreeNode<T> child : getChildren()){
			output.add(child.getValue());
		}
		return output;
	}
	
	public void transverse(Callable action){
		transverse(this, action);
	}
	
	private void transverse(TreeNode<T> n, Callable action){
		if( n.getValue() != null){
			action.function(n.getValue(), n);
		}
		for(TreeNode<T> node : n.getChildren()){
			n.transverse(node, action);
		}
	}
	public int getLevel(){
		int lv = 0;
		for(TreeNode<T>current = this; current != null; current = current.getParent())
			++lv;
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
