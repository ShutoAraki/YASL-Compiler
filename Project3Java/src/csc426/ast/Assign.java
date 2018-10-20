package csc426.ast;

public class Assign {

	private String id;
	private Expr expr;
	
	public Assign(String id, Expr expr) {
		this.id = id;
		this.expr = expr;
	}
	
	public void display(String indentation) {
		System.out.println(indentation + "Assign " + id);
	}
}
