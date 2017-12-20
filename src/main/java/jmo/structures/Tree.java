package jmo.structures;

import java.util.Collection;
import java.util.Iterator;

import jmo.patterns.visitor.Visitor;

public class Tree<T> implements Collection<TreeNode<T>> {
	
	TreeNode<T> root;
	
	@Override
	public int size() {
		if(root == null) {
			return 0;
		}
		else {
			Counter counter = new Counter();
			root.transverse(counter);
			return counter.getCount();
		}
	}

	@Override
	public boolean isEmpty() {
		return size() == 0;
	}

	@Override
	public boolean contains(Object o) {
		return false;
	}

	@Override
	public Iterator<TreeNode<T>> iterator() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object[] toArray() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T> T[] toArray(T[] a) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean add(TreeNode<T> e) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean remove(Object o) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean containsAll(Collection<?> c) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean addAll(Collection<? extends TreeNode<T>> c) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean removeAll(Collection<?> c) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean retainAll(Collection<?> c) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void clear() {
		root = null;
	}
	
	class Counter implements Visitor<T>{
		private int i;

		@Override
		public void visit(T element) {
			i++;
		}
		
		public int getCount() {
			return i;
		}
	}
	

}
