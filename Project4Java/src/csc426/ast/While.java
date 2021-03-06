package csc426.ast;

import csc426.interp.SymbolTable;
import csc426.interp.Value;
import csc426.interp.VoidValue;

public class While extends Stmt {
	
	private Expr expr;
	private Stmt body;

	public While(Expr expr, Stmt body) {
		this.expr = expr;
		this.body = body;
	}

	@Override
	public void display(String indentation) {
		System.out.println(indentation + "While");
		expr.display(indentation + "  ");
		body.display(indentation + "  ");
	}

	@Override
	public Value interpret(SymbolTable t) {
		Value test = expr.interpret(t);
		
		while (test.boolValue()) {
			body.interpret(t);
			test = expr.interpret(t);
		}
		return new VoidValue();
	}

}
