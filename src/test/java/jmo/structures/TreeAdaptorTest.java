package jmo.structures;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import jmo.patterns.visitor.Stringifier;

public class TreeAdaptorTest {

	TreeAdaptor<Integer> adaptor = child -> {
		int sqrt = (int) Math.round(Math.sqrt(child));
		return child == sqrt ? 0 : sqrt;
	};

	@Test
	public void toTree_intList_createTree() {
		List<Integer> list = new ArrayList<>();
		for (int i = 1; i <= 4; i++) {
			list.add(i);
		}
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
		List<Integer> list = new ArrayList<>();
		for (int i = 3; i <= 4; i++) {
			list.add(i);
		}

		TreeNode<Integer> tn = adaptor.toTree(list);
		assertEquals(2, tn.getValue().intValue());
		assertEquals(3, tn.child(0).intValue());
		assertEquals(4, tn.child(1).intValue());

		System.out.println(tn.transverseNodes(new Stringifier<>()).toString());
	}

	@Test
	public void toTree_noRoot_artificalRoot() {
		List<Integer> list = new ArrayList<>();
		for (int i = 3; i <= 4; i++) {
			list.add(i);
		}
		TreeNode<Integer> tn = TreeAdaptor.toTree(list, p -> null);
		assertNull(tn.getValue());
		assertEquals(3, tn.child(0).intValue());
		assertEquals(4, tn.child(1).intValue());
	}
	
	@Test
	public void toTree_2Roots_artificalRoot() {
		List<Integer> list = new ArrayList<>();
		for (int i = 3; i <= 4; i++) {
			list.add(i);
		}
		TreeNode<Integer> tn = TreeAdaptor.toTree(list, p -> p * 2);
		System.out.println(tn.transverseNodes(new Stringifier<>()));
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
