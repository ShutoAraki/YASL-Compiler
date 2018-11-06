package csc426.interp;

import java.util.HashMap;
import java.util.Scanner;
import java.util.Stack;

public class SymbolTable {
	
	// Stack to keep track of different scopes
	private Stack<HashMap<String, Value>> scopes;
	// Scanner for input stream
	public static Scanner sc = new Scanner(System.in);
	
	public SymbolTable() {
		scopes = new Stack<>();
		enter();
	}
	
	public void bind(String id, Value v) {
		HashMap<String, Value> t = scopes.peek();
		if (t.containsKey(id)) {
			System.err.println("Variable " + id + " already exists in the same scope.");
		} else {
			t.put(id, v);
		}
	}
	
	public Value lookup(String id) {
		for (int i = scopes.size() - 1; i >= 0; i--) {
			if (scopes.get(i).containsKey(id)) {
				return scopes.get(i).get(id);
			}
		}
		// Default variable
		Value defaultValue = new IntCell(0);
		bind(id, defaultValue);
		return defaultValue;
	}
	
	// For compiler, this method has to take a name to keep track of scopes
	// in intermediate code generation
	public void enter() {
		scopes.push(new HashMap<>());
	}
	
	public void exit() {
		scopes.pop();
	}
}