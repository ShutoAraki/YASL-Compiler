package csc426.ast;

import java.util.List;

public class VarDecls {
	
	private List<VarDecl> vars;

	public VarDecls(List<VarDecl> vars) {
		this.vars = vars;
	}

	public void display(String indentation) {
		for (VarDecl var : vars) {
			var.display(indentation + "  ");
		}
	}
	
}
