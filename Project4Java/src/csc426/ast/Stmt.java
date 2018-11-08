package csc426.ast;

import csc426.interp.SymbolTable;
import csc426.interp.Value;

public abstract class Stmt extends ASTNode {
	public abstract Value interpret(SymbolTable t);
}
