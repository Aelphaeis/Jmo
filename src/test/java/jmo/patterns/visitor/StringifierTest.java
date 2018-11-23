package jmo.patterns.visitor;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import org.junit.Test;

import jmo.structures.TreeAdaptor;
import jmo.structures.TreeNode;
public class StringifierTest {

	TreeAdaptor<Integer> adaptor = child -> {
		int sqrt = (int) Math.round(Math.sqrt(child));
		return child == sqrt ? 0 : sqrt;
	};

	@Test
	public void visit_sqParentList_perfectTree() {
		Stream<Integer> stream = IntStream.range(1, 8).mapToObj(p -> p);
		List<Integer> list = stream.collect(Collectors.toList());
		TreeNode<Integer> tn = adaptor.toTree(list);
		Stringifier<Integer> s = new Stringifier<>();
		tn.transverseNodes(s);
		String result = s.toString();
		assertEquals(1, result.chars().filter(p -> p == '0').count());
		assertEquals(1, result.chars().filter(p -> p == '1').count());
		assertEquals(1, result.chars().filter(p -> p == '2').count());
		assertEquals(1, result.chars().filter(p -> p == '3').count());
		assertEquals(1, result.chars().filter(p -> p == '4').count());
		assertEquals(1, result.chars().filter(p -> p == '5').count());
		assertEquals(1, result.chars().filter(p -> p == '6').count());
		assertEquals(1, result.chars().filter(p -> p == '7').count());
	}
	
	@Test
	public void visit_only1NodeLevel_displayed() {
		final String expected = "-> 1" + System.lineSeparator();
		Stringifier<Integer> s = new Stringifier<>();
		TreeNode<Integer> n = new TreeNode<>(1);
		n.transverseNodes(s);
		assertEquals(expected, s.toString());
	}
	
	@Test
	public void ctor_noArgs_noException() {
		assertNotNull(new Stringifier<Integer>());
	}
}
