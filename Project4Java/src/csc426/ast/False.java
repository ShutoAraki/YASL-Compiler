package csc426.ast;

import csc426.interp.BoolValue;
import csc426.interp.SymbolTable;
import csc426.interp.Value;

public class False extends Expr {

	@Override
	public void display(String indentation) {
		System.out.println(indentation + "False");
	}

	@Override
	public Value interpret(SymbolTable t) {
		return new BoolValue(false);
	}
}
