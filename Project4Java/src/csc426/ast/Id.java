package csc426.ast;

import csc426.interp.SymbolTable;
import csc426.interp.Value;

public class Id extends Expr {

	private String id;
	
	public Id(String id) {
		this.id = id;
	}

	@Override
	public void display(String indentation) {
		System.out.println(indentation + "Id " + id);
	}

	@Override
	public Value interpret(SymbolTable t) {
		return t.lookup(id);
	}
	
}
