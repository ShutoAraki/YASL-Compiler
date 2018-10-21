package csc426.ast;

public class ExprItem extends Item {
	
	private Expr expr;

	public ExprItem(Expr expr) {
		this.expr = expr;
	}

	@Override
	public void display(String indentation) {
		System.out.println(indentation + "ExprItem");
		expr.display(indentation + "  ");
	}

}
