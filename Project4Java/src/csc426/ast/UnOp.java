package csc426.ast;

public class UnOp extends Expr {
	
	private Op1 op;
	private Expr expr;
	
	public UnOp(Op1 op, Expr expr) {
		this.op = op;
		this.expr = expr;
	}

	@Override
	public void display(String indentation) {
		System.out.println(indentation + "UnOp " + op);
		expr.display(indentation + "  ");
	}

}
