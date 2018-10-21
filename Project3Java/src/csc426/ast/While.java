package csc426.ast;

public class While extends Stmt {
	
	private Expr test;
	private Stmt body;

	public While(Expr test, Stmt body) {
		this.test = test;
		this.body = body;
	}

	@Override
	public void display(String indentation) {
		System.out.println(indentation + "While");
		test.display(indentation + "  ");
		body.display(indentation + "  ");
	}

}
