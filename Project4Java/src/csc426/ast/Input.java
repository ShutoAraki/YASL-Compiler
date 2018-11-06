package csc426.ast;

import java.util.Scanner;

import csc426.interp.SymbolTable;
import csc426.interp.Value;
import csc426.interp.VoidValue;

public class Input extends Stmt {
	
	private String msg;

	public Input(String msg) {
		this.msg = msg;
	}
	
	@Override
	public void display(String indentation) {
		System.out.println(indentation + "Input \"" + msg + "\"");
	}

	@Override
	public Value interpret(SymbolTable t) {
		Scanner sc = new Scanner(System.in);
		System.out.print(msg);
		
		// TODO: Needs to figure this one out?
		if (sc.hasNextLine())
			sc.nextLine();
		sc.close();
		return new VoidValue();
	}
}
