package jmo.structures;

import java.util.ArrayList;
import java.util.Collection;
import java.util.EnumMap;
import java.util.Iterator;
import java.util.List;

import jmo.patterns.visitor.Visitor;

public class TreeNode<T> implements Iterable<T>{
	private static final TreeTraversal DEFAULT = TreeTraversal.PREORDER;
	
	private T value = null;
	private TreeNode<T> parent;
	List<TreeNode<T>> children;
	
	private EnumMap<TreeTraversal, Transveral<T>> transverals;
	
	public TreeNode(){
		setChildren(new ArrayList<TreeNode<T>>());
		transverals = new EnumMap<>(TreeTraversal.class);
		transverals.put(TreeTraversal.PREORDER, new PreoderTransveral());
	}
	
	public TreeNode(T value) {
		this();
		setValue(value);
	}
	
	/**
	 * @return whether or not this node has a parent.
	 */
	public boolean hasParent(){
		return getParent() != null;
	}
	
	/**
	 * @return whether or not this node has a value.
	 */
	public boolean hasValue(){
		return getValue() != null;
	}

	/**
	 * Assume this tree node is tree node A.
	 * Takes data and inserts it into tree node B.
	 * Takes tree node B and inserts it as a child of tree node A.
	 * @param child data we want to insert into tree.
	 */
	public void addChild(T child){
		TreeNode<T> node = new TreeNode<>();
		node.setValue(child);
		node.setParent(this);
		addChild(node);
		
	}
	
	public void addChild(TreeNode<T> child) {
		getChildren().add(child);
	}
	
	/**
	 * Calls {@link #addChild(T)} on every object in iterable
	 * @param children
	 */
	public void addChildren(Collection<T> children){
		for(T v : children){
			addChild(v);
		}
	}
	
	public void addChild(Collection<TreeNode<T>> children) {
		getChildren().addAll(children);
	}

	/**
	 * @return all direct children of this node
	 */
	public List<T> getChildrenValues(){
		List<T> output = new ArrayList<>();
		for(TreeNode<T> child : getChildren()){
			output.add(child.getValue());
		}
		return output;
	}
	
	public void transverseNodes(Visitor<TreeNode<T>> visitor){
		transverseNodes(visitor, DEFAULT);
	}
	
	public void transverseNodes(Visitor<TreeNode<T>> visitor, TreeTraversal transveralMethod){
		Transveral<T> transveral = transverals.get(transveralMethod);
		transveral.transverseNodes(this, visitor);
	}
	
	public void transverse(Visitor<T> visitor){
		transverse(visitor, DEFAULT);
	}
	
	private void transverse(Visitor<T> visitor, TreeTraversal transveralMethod){
		Transveral<T> transveral = transverals.get(transveralMethod);
		transveral.transverse(this, visitor);
	}
	
	public int getLevel(){
		int lv = 0;
		for(TreeNode<T> current = this; current != null; current = current.getParent())
			++lv;
		return lv;
	}
	
	@Override
	public Iterator<T> iterator() {
		return iterator(DEFAULT);
	}
	public Iterator<T> iterator(TreeTraversal transveral) {
		final List<T> treeNodeList = new ArrayList<>();
		transverals.get(transveral).transverse(this, treeNodeList::add);
		return treeNodeList.iterator();
	}
	
	protected interface Transveral<T> {
		void transverse(TreeNode<T> n, Visitor<T> action);
		void transverseNodes(TreeNode<T> n, Visitor<TreeNode<T>> visitor);
	}
	
	protected class PreoderTransveral implements Transveral<T>{
		@Override
		public void transverse(TreeNode<T> n, Visitor<T> action) {
			if( n.getValue() != null){
				action.visit(n.getValue());
			}
			for(TreeNode<T> node : n.getChildren()){
				transverse(node, action);
			}
		}
		
		@Override
		public void transverseNodes(TreeNode<T> n, Visitor<TreeNode<T>> visitor) {
			visitor.visit(n);
			for(TreeNode<T> node : n.getChildren()){
				transverseNodes(node, visitor);
			}
		}
	}
	

	protected class PostorderTransveral implements Transveral<T>{
		@Override
		public void transverse(TreeNode<T> n, Visitor<T> action) {
			if( n.getValue() != null){
				action.visit(n.getValue());
			}
			for(TreeNode<T> node : n.getChildren()){
				transverse(node, action);
			}
		}
		
		@Override
		public void transverseNodes(TreeNode<T> n, Visitor<TreeNode<T>> visitor) {
			visitor.visit(n);
			for(TreeNode<T> node : n.getChildren()){
				transverseNodes(node, visitor);
			}
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
