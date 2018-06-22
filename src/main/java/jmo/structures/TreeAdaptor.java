package jmo.structures;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.mapping;
import static java.util.stream.Collectors.toList;

import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

public abstract class TreeAdaptor<T> {

	protected abstract T resolveParent(T child);
	
	public TreeNode<T> toTree(Iterable<T> iterable){
		boolean hasRoot = false;
		Map<T, T> parents = new HashMap<>();
		Iterator<T> it = iterable.iterator();
	
		
		//find the parent of each node
		while(it.hasNext()) {
			T value = it.next();
			
			//if a node has no parent its the root
			T parent = resolveParent(value);
			if(parent != null) {
				parents.put(value, parent);
			}
			else if(!hasRoot){
				hasRoot = true;
			}
			else {
				String err = "Multiple roots found";
				throw new IllegalStateException(err);
			}
		}
		//find the root
		List<T> roots = parents.values().stream()
				.filter(p -> !parents.containsKey(p))
				.collect(Collectors.toList());
		
		//child parent mappings
		Map<T, List<T>> cpm = parents.entrySet().stream()
				.collect(groupingBy(Entry::getValue,
						mapping(Entry::getKey, toList())));
		
		//if multiple roots create an artifical root
		if(roots.isEmpty() || roots.size() > 1) {
			TreeNode<T> artificalRoot = new TreeNode<>();
			for(T subValue : roots) {
				subTree(cpm, subValue, artificalRoot);
			}
			return artificalRoot;
		}
		else {
			return subTree(cpm, roots.get(0), null);
		}
	}
	
	//take the map and put it to a tree format.
	private TreeNode<T> subTree(Map<T, List<T>> m, T v, TreeNode<T> p) {
		List<T> children = m.getOrDefault(v, Collections.emptyList());
		TreeNode<T> root = new TreeNode<>(p, v);
		for (T child : children) {
			subTree(m, child, root);
		}
		return root;
	}
}
