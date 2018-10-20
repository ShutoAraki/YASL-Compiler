package csc426.ast;

import java.util.List;

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

}
