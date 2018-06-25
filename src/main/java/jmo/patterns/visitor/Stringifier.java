package jmo.patterns.visitor;

import java.util.List;

import jmo.structures.TreeNode;

public class Stringifier<T> implements Visitor<TreeNode<T>> {
	StringBuilder builder = new StringBuilder();

	@Override
	public void visit(TreeNode<T> e) {
		int i = builder.length();

		TreeNode<T> n = e.getParent();
		TreeNode<T> c = e;
		while (n != null) {
			List<TreeNode<T>> kids = n.getChildren();
			if (e == c) {
				builder.insert(i, "   +");
			} else if (kids.indexOf(c) < kids.size() - 1) {
				builder.insert(i, "   |");
			} else {
				builder.insert(i, "    ");
			}
			c = n;
			n = c.getParent();
		}
		builder.append("-> ");
		builder.append(e.getValue());
		builder.append(System.lineSeparator());
	}

	@Override
	public String toString() {
		return builder.toString();
	}
}
