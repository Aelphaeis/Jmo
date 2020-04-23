package jmo.structures;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import jmo.patterns.visitor.Visitor;

public class Trie<T> {

	private final TrieNode root;

	public Trie() {
		this.root = new TrieNode("");
	}

	public TrieNode add(CharSequence seq) {
		return add(seq, null);
	}

	public TrieNode add(CharSequence sequence, T data) {
		TrieNode n = root;
		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < sequence.length(); i++) {
			builder.append(sequence.charAt(i));
			CharSequence path = builder.toString();
			n = n.children.computeIfAbsent(path, TrieNode::new);
		}
		n.setData(data);
		return n;
	}

	public Optional<TrieNode> get(CharSequence sequence) {
		TrieNode n = root;
		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < sequence.length(); i++) {
			builder.append(sequence.charAt(i));
			CharSequence path = builder.toString();
			n = n.children.get(path);
			if (n == null) {
				break;
			}
		}
		return Optional.ofNullable(n);
	}
	
	public <R extends Visitor<TrieNode>> R transverse(R visitor) {
		return root.transverse(visitor);
	}

	public class TrieNode {

		private final Map<CharSequence, TrieNode> children;
		private final CharSequence path;
		private T data;

		protected TrieNode(CharSequence path) {
			this(path, null);
		}

		protected TrieNode(CharSequence path, T data) {
			this.path = Objects.requireNonNull(path);
			this.children = new LinkedHashMap<>();
			setData(data);
		}

		public <R extends Visitor<TrieNode>> R transverse(R visitor) {
			visitor.visit(this);
			children.values().forEach(p -> p.transverse(visitor));
			return visitor;
		}

		public T getData() {
			return data;
		}

		public void setData(T data) {
			this.data = data;
		}

		public CharSequence getPath() {
			return path;
		}
	}
}
