package csc426.ast;

import java.util.List;

public class Call extends Expr {
	
	private String id;
	private List<Expr> args;
	
	public Call(String id, List<Expr> args) {
		this.id = id;
		this.args = args;
	}

	@Override
	public void display(String indentation) {
		System.out.println(indentation + "Call " + id);
		for (Expr arg : args) {
			arg.display(indentation + "  ");
		}
	}
	
}
