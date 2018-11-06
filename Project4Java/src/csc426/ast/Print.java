package csc426.ast;

import java.util.List;

import csc426.interp.IntValue;
import csc426.interp.SymbolTable;
import csc426.interp.Value;
import csc426.interp.VoidValue;

public class Print extends Stmt {
	
	private List<Item> its;

	public Print(List<Item> its) {
		this.its = its;
	}
	
	@Override
	public void display(String indentation) {
		System.out.println(indentation + "Print");
		
		for (Item it : its) {
			it.display(indentation + "  ");
		}
	}

	public Value interpret(SymbolTable t) {
		for (Item it : its) {
			if (it instanceof ExprItem) {
				Expr expr = ((ExprItem) it).getExpr();
				Value value = expr.interpret(t);
				
				System.out.print(Integer.parseInt(value.toString()));
			} else if (it instanceof StringItem) {
				String msg = ((StringItem) it).getMsg();
				System.out.print(msg);
			}
		}
		System.out.println();
		return new VoidValue();
	}

}
