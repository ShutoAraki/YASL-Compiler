package csc426.ast;

public class IfThenElse extends Stmt {
	
	private Expr test;
	private Stmt s1;
	private Stmt s2;
	
	public IfThenElse(Expr test, Stmt s1, Stmt s2) {
		this.test = test;
		this.s1 = s1;
		this.s2 = s2;
	}

	@Override
	public void display(String indentation) {
		System.out.println(indentation + "IfThenElse");
		test.display(indentation + "  ");
		s1.display(indentation + "  ");
		s2.display(indentation + "  ");
	}

}
