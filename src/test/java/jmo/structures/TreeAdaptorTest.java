package jmo.structures;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.junit.Test;

import jmo.patterns.visitor.Stringifier;

public class TreeAdaptorTest {

	TreeAdaptor<Integer> adaptor = child -> {
		int sqrt = (int) Math.round(Math.sqrt(child));
		return child == sqrt ? 0 : sqrt;
	};

	@Test
	public void toTree_intList_createTree() {
		List<Integer> list = IntStream.range(1, 16)
				.mapToObj(p -> p).collect(Collectors.toList());
		TreeNode<Integer> tn = adaptor.toTree(list);

		assertEquals(0, tn.getValue().intValue());
		assertEquals(1, tn.child(0).intValue());

		tn = tn.node(0);
		assertEquals(2, tn.child(0).intValue());

		tn = tn.getChildren().get(0);
		assertEquals(3, tn.child(0).intValue());
		assertEquals(4, tn.child(1).intValue());
	}

	@Test
	public void toTree_noRoot_createTree() {
		TreeNode<Integer> tn = adaptor.toTree(Arrays.asList(3, 4));
		assertEquals(2, tn.getValue().intValue());
		assertEquals(3, tn.child(0).intValue());
		assertEquals(4, tn.child(1).intValue());
	}

	@Test
	public void toTree_noRoot_artificalRoot() {
		TreeNode<Integer> tn = TreeAdaptor.toTree(Arrays.asList(3, 4), 
				p -> null);
		assertNull(tn.getValue());
		assertEquals(3, tn.child(0).intValue());
		assertEquals(4, tn.child(1).intValue());
	}
	
	@Test
	public void toTree_2Roots_artificalRoot() {
		TreeNode<Integer> tn = TreeAdaptor.toTree(Arrays.asList(3, 4),
				p -> p * 2);
		assertNull(tn.getValue());
		assertEquals(6, tn.child(0).intValue());
		assertEquals(8, tn.child(1).intValue());
		assertEquals(3, tn.node(0).child(0).intValue());
		assertEquals(4, tn.node(1).child(0).intValue());
	}

	@Test(expected = IllegalArgumentException.class)
	public void toTree_MultiRoot_artificalRoot() {
		List<Integer> list = new ArrayList<>();
		for (int i = 3; i <= 4; i++) {
			list.add(i);
		}
		TreeAdaptor.toTree(list, p -> p);
	}
}
