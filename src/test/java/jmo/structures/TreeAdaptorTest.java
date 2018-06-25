package jmo.structures;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class TreeAdaptorTest {

	TreeAdaptor<Integer> adaptor = new TreeAdaptor<Integer>() {
		@Override
		protected Integer resolveParent(Integer child) {
			int sqrt = (int) Math.round(Math.sqrt(child));
			return child == sqrt? 0 : sqrt;
		}
	};

	@Test
	public void test() {
		List<Integer> list = new ArrayList<>();
		for (int i = 1; i <= 16; i++) {
			list.add(i);
		}
	}
}
