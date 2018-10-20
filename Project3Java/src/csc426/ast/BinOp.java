package csc426.ast;

public class BinOp extends Expr {
	
	private Expr left;
	private Op2 op;
	private Expr right;

	public BinOp(Expr left, Op2 op, Expr right) {
		this.left = left;
		this.op = op;
		this.right = right;
	}

	public Expr getLeft() {
		return left;
	}

	public Op2 getOp() {
		return op;
	}

	public Expr getRight() {
		return right;
	}

	public void display(String indent) {
		System.out.println(indent + "BinOp " + op);
		left.display(indent + "  ");
		right.display(indent + "  ");
	}
	
}
