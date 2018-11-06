package csc426.ast;

import csc426.interp.SymbolTable;
import csc426.interp.Value;
import csc426.interp.BoolValue;
import csc426.interp.IntValue;

public class UnOp extends Expr {
	
	private Op1 op;
	private Expr expr;
	
	public UnOp(Op1 op, Expr expr) {
		this.op = op;
		this.expr = expr;
	}

	@Override
	public void display(String indentation) {
		System.out.println(indentation + "UnOp " + op);
		expr.display(indentation + "  ");
	}

	@Override
	public Value interpret(SymbolTable t) {
		Value value = expr.interpret(t);
		switch(op) {
		case Neg:
			return new IntValue(- Integer.parseInt(value.toString()));
		case Not:
			return new BoolValue(! Boolean.parseBoolean(value.toString()));
		default:
			return null; // unreachable
		}
	}

}
