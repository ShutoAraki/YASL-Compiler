package csc426.ast;

public class ExprStmt extends Stmt {

	private Expr expr;

	public ExprStmt(Expr expr) {
		this.expr = expr;
	}
	
	@Override
	public void display(String indentation) {
		System.out.println(indentation + "ExprStmt");
		expr.display(indentation + "  ");
	}

}
