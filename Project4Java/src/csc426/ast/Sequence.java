package csc426.ast;

import java.util.List;

import csc426.interp.SymbolTable;
import csc426.interp.Value;

public class Sequence extends Stmt {
	
	private List<Stmt> ss;

	public Sequence(List<Stmt> ss) {
		this.ss = ss;
	}

	@Override
	public void display(String indentation) {
		System.out.println(indentation + "Sequence");
		
		for (Stmt stmt : ss) {
			stmt.display(indentation + "  ");
		}
	}

	@Override
	public Value interpret(SymbolTable t) {
		for (int i = 0; i < ss.size() - 1; i++) {
			ss.get(i).interpret(t);
		}
		return ss.get(ss.size() - 1).interpret(t);
	}

}
