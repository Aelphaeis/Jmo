package jmo.structures;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

@FunctionalInterface
public interface TreeAdaptor<T> {

	/**
	 * Take multiple value map turn them into TreeNodes where key is parent
	 * and elements of the map are the children.
	 * 
	 * @param m
	 * @param v
	 * @param p
	 * @return
	 */
	static <T> TreeNode<T> subTree(Map<T, List<T>> m, T v, TreeNode<T> p) {
		List<T> children = m.getOrDefault(v, Collections.emptyList());
		TreeNode<T> root = new TreeNode<>(p, v);
		for (T child : children) {
			subTree(m, child, root);
		}
		return root;
	}

	/**
	 * Given a child, this method should return the parent of that child.
	 * @param child
	 * @return
	 */
	T resolve(T child);

	/**
	 * This method takes an iterable and returns a tree based on the parent
	 * resolution strategy.
	 * @param i
	 * @return
	 */
	default TreeNode<T> toTree(Iterable<T> i) {
		Map<T, T> parents = new HashMap<>();
		Iterator<T> it = i.iterator();

		// find the parent of each node
		while (it.hasNext()) {
			T value = it.next();
			// if a node has no parent its the root
			T parent = resolve(value);
			parents.put(value, parent);
		}

		// find the root
		List<T> roots = new ArrayList<>(
				parents.values().stream().filter(p -> !parents.containsKey(p))
						.collect(Collectors.toSet()));

		// child parent mappings
		Map<T, List<T>> cpm = new HashMap<>();
		for (Entry<T, T> p : parents.entrySet()) {
			T val = p.getValue();
			T key = p.getKey();
			cpm.computeIfAbsent(val, k -> new ArrayList<T>()).add(key);
		}

		if (roots.isEmpty()) {
			String err = "Unable to resolve root. Circular dependency?";
			throw new IllegalArgumentException(err);
		}
		
		// if multiple roots create an artificial root
		if (roots.size() > 1) {
			TreeNode<T> artificalRoot = new TreeNode<>();
			for (T subValue : roots) {
				subTree(cpm, subValue, artificalRoot);
			}
			return artificalRoot;
		} else {
			return subTree(cpm, roots.get(0), null);
		}
	}
}
