package csc426.ast;

import csc426.interp.BoolCell;
import csc426.interp.IntCell;
import csc426.interp.SymbolTable;
import csc426.interp.Value;

public class Assign extends Stmt {

	private String id;
	private Expr expr;
	
	public Assign(String id, Expr expr) {
		this.id = id;
		this.expr = expr;
	}
	
	public void display(String indentation) {
		System.out.println(indentation + "Assign " + id);
		expr.display(indentation + "  ");
	}

	@Override
	public Value interpret(SymbolTable t) {
		
		Value lhs = t.lookup(id);
		Value rhs = expr.interpret(t);
		
		if (lhs instanceof IntCell)
			((IntCell) lhs).set(rhs.intValue());
		else 
			((BoolCell) lhs).set(rhs.boolValue());
		
		return rhs;
	}
}
