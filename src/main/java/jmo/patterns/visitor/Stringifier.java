package jmo.patterns.visitor;

import java.util.List;

import jmo.structures.TreeNode;

public class Stringifier<T> implements Visitor<TreeNode<T>> {
	StringBuilder builder = new StringBuilder();

	@Override
	public void visit(TreeNode<T> e) {
		StringBuilder segment = new StringBuilder();
		segment.append(" >-");

		TreeNode<? extends Object> n = e.getParent();
		TreeNode<? extends Object> c = e;
		//└
		while (n != null) {
			List<?> kids = n.getChildren();
			if(e == c) {
				segment.append("+   ");
			}
			else if (kids.indexOf(c) < kids.size() - 1) {
				segment.append("|   ");
			} else {
				segment.append("    ");
			}
			c = n;
			n = c.getParent();
		}

		builder.append(segment.reverse());
		builder.append(e.getValue());
		builder.append(System.lineSeparator());
	}
	@Override
	public String toString() {
		return builder.toString();
	}



}
