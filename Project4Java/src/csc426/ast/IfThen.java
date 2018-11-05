package csc426.ast;

import csc426.interp.BoolValue;
import csc426.interp.SymbolTable;
import csc426.interp.Value;
import csc426.interp.VoidValue;

public class IfThen extends Stmt {

	private Expr expr;
	private Stmt s1;

	public IfThen(Expr expr, Stmt s1) {
		this.expr = expr;
		this.s1 = s1;
	}

	@Override
	public void display(String indentation) {
		System.out.println(indentation + "IfThen");
		expr.display(indentation + "  ");
		s1.display(indentation + "  ");
	}

	// TODO: Figure out this one as well?
	@Override
	public Value interpret(SymbolTable t) {
		BoolValue test = (BoolValue) expr.interpret(t);
		if (Boolean.parseBoolean(test.toString()))
			return s1.interpret(t);
		else
			return new VoidValue();
	}

}
