package test.jmo.structures;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import jmo.patterns.visitor.Visitor;
import jmo.structures.TreeNode;

public class TreeNodeTest {
	
	TreeNode<Integer> numTree;
	NumberListVisitor visitor;
	
	@Before
	public void setup() {
		TreeNode<Integer> t1 = new TreeNode<>(1);
		TreeNode<Integer> t2 = new TreeNode<>(2);
		TreeNode<Integer> t3 = new TreeNode<>(3);
		TreeNode<Integer> t4 = new TreeNode<>(4);
		TreeNode<Integer> t5 = new TreeNode<>(5);

		t2.addChild(t4);
		t2.addChild(t5);
		
		t1.addChild(t2);
		t1.addChild(t3);
		
		numTree = t1;
		
		visitor = new NumberListVisitor();
	}
	

	@Test
	public void hasParent_noParent_false() {
		TreeNode<Integer> tn = new TreeNode<>();
		assertNull(tn.getParent());
		assertFalse(tn.hasParent());
	}
	@Test
	public void hasValue_noValue_false() {
		TreeNode<Integer> tn = new TreeNode<>();
		assertFalse(tn.hasValue());
	}
	
	public void getLevel_numTree_correctLevels() {
		
	}
	
	@Test
	public void transverse_default_12453() {
		List<Integer> numbers = Arrays.asList(new Integer[] { 1,2,4,5,3 });
		numTree.transverse(visitor);
		assertEquals(numbers, visitor.getNumbers());
	}
	
	class NumberListVisitor implements Visitor<Integer> {
		List<Integer> numbers = new ArrayList<>();
		
		@Override
		public void visit(Integer element) {
			numbers.add(element);
		}
		
		public List<Integer> getNumbers(){
			return numbers;
		}
	}
	
}
