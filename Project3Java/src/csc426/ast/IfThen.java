package csc426.ast;

public class IfThen extends Stmt {

	private Expr test;
	private Stmt s1;

	public IfThen(Expr test, Stmt s1) {
		this.test = test;
		this.s1 = s1;
	}

	@Override
	public void display(String indentation) {
		System.out.println(indentation + "IfThen");
		test.display(indentation + "  ");
		s1.display(indentation + "  ");
	}

}
