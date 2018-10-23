package csc426.ast;

public class True extends Expr {

	@Override
	public void display(String indentation) {
		System.out.println(indentation + "True");
	}
}
