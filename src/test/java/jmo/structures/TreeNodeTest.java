package jmo.structures;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.junit.Before;
import org.junit.Test;

public class TreeNodeTest {

	TreeNode<Integer> root;

	@Before
	public void setup() {
		List<Integer> elements = IntStream.range(1, 16).mapToObj(p -> p)
				.collect(Collectors.toList());
		TreeAdaptor<Integer> adaptor = p -> {
			int sqrt = (int) Math.round(Math.sqrt(p));
			return p == sqrt ? 0 : sqrt;
		};
		root = adaptor.toTree(elements);
	}

	@Test
	public void hasParent_noParent_false() {
		assertFalse(root.hasParent());
		assertTrue(root.node(0).hasParent());
	}

	@Test
	public void getParent_rootNode_null() {
		assertNull(root.getParent());
	}

	@Test
	public void getLevel_rootNode_0() {
		assertEquals(0, root.getDepth());
	}

	@Test
	public void getLevel_rootNode_visitAllNodes() {
		Set<Integer> results = new TreeSet<>();
		root.transverse(results::add);
		for (int i = 0; i < 16; i++) {
			assertTrue(results.contains(i));
		}
	}
	
	@Test
	public void getLevel_rootNode_getChildrenValues() {
		List<Integer> results = root.getChildrenValues();
		assertEquals(1, results.size());
		assertEquals(1, results.get(0).intValue());
	}
	
	@Test
	public void ctor_values_resolveCorrectly() {
		assertTrue(new TreeNode<Integer>(0).hasValue());
		assertFalse(new TreeNode<Integer>((Integer)null).hasValue());
	}
	
	@Test
	public void addChildren_values_added() {
		root = new TreeNode<>(0);
		root.addChildren(Arrays.asList(1, 2));
		assertEquals((Integer)1, root.child(0));
		assertEquals((Integer)2, root.child(1));
	}
}
