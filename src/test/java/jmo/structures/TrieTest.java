package jmo.structures;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class TrieTest {

	@Test
	public void test() {
		Trie<Boolean> names = new Trie<>();
		names.add("joseph", true);
		assertTrue(names.get("joseph").get().getData());
	}

}
