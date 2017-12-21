package jmo.structures;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

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
		Checker checker = new Checker(o);
		root.transverseNodes(checker);
		return checker.hasElement;
	}

	@Override
	public Iterator<TreeNode<T>> iterator() {
		final List<TreeNode<T>> treeNodeList = new ArrayList<>();
		root.transverseNodes(treeNodeList::add);
		return treeNodeList.iterator();
	}

	@Override
	public Object[] toArray() {
		int size = size();
		Object[] arr = new Object[size];
		Iterator<TreeNode<T>> it = iterator();
		for(int count = 0; count < size; count++) {
			arr[count] = it.hasNext();
		}
		return arr;
	}

	@Override
	public <E> E[] toArray(E[] a) {
		final List<TreeNode<T>> treeNodeList = new ArrayList<>();
		root.transverseNodes(treeNodeList::add);
		return treeNodeList.toArray(a);
	}

	@Override
	public boolean add(TreeNode<T> e) {
		root.addChild(e);
		return true;
	}

	@Override
	public boolean remove(Object o) {
		Checker checker = new Checker(o, true);
		root.transverseNodes(checker);
		return checker.hasElement;
	}

	@Override
	public boolean containsAll(Collection<?> c) {
		boolean containsAll = true;
		for(Iterator<?> it = c.iterator(); it.hasNext() && containsAll;) {
			containsAll &= contains(it.next());
		}
		return containsAll;
	}

	@Override
	public boolean addAll(Collection<? extends TreeNode<T>> c) {
		for(TreeNode<T> element : c) {
			add(element);
		}
		return !c.isEmpty();
	}

	@Override
	public boolean removeAll(Collection<?> c) {
		boolean isCollectionModified = false;
		Iterator<?> it = c.iterator();
		while(it.hasNext()) {
			isCollectionModified |= remove(it.next());
		}
		return isCollectionModified;
	}

	@Override
	public boolean retainAll(Collection<?> c) {
		boolean isCollectionModified = false;
		Iterator<TreeNode<T>> it = iterator();
		while(it.hasNext()) {
			Object o = it.next();
			if(!c.contains(o)) {
				isCollectionModified = remove(o);
			}
		}
		return isCollectionModified;
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
	
	class Checker implements Visitor<TreeNode<T>>{
		
		private boolean hasElement;
		private boolean rem;
		private Object obj;
		
		
		public Checker(Object object) {
			this(object, false);
		}
		
		public Checker(Object object, boolean remove) {
			hasElement = false;
			obj = object;
			rem = remove;
		}
		
		@Override
		public void visit(TreeNode<T> element) {
			if(obj == null) {
				hasElement |= element == null;
			}
			else {
				hasElement |= obj.equals(element);
				if(rem) {
					TreeNode<T> parent = element.getParent();
					List<TreeNode<T>> children = parent.getChildren();
					children.remove(element);
				}
			}
		}
	}
}
