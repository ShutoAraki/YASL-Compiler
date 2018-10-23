package csc426.ast;

public class False extends Expr {

	@Override
	public void display(String indentation) {
		System.out.println(indentation + "False");
	}
}
