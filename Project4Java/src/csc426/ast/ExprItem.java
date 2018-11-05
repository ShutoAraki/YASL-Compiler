package csc426.ast;

public class ExprItem extends Item {
	
	private Expr expr;

	public ExprItem(Expr expr) {
		this.expr = expr;
	}
	
	public Expr getExpr() { return expr; }

	@Override
	public void display(String indentation) {
		System.out.println(indentation + "ExprItem");
		expr.display(indentation + "  ");
	}

}
