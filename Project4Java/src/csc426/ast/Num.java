package csc426.ast;

import csc426.interp.IntValue;
import csc426.interp.SymbolTable;
import csc426.interp.Value;

public class Num extends Expr {

	private int value;
	
	public Num(int value) {
		this.value = value;
	}
	
	@Override
	public void display(String indentation) {
		System.out.println(indentation + "Num " + value);
	}

	@Override
	public Value interpret(SymbolTable t) {
		return new IntValue(value);
	}
}
