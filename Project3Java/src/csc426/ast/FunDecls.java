package csc426.ast;

import java.util.List;

public class FunDecls {

	private List<FunDecl> funs;
	
	public FunDecls(List<FunDecl> funs) {
		this.funs = funs;
	}
	
	public void display(String indentation) {
		for (FunDecl fun : funs) {
			fun.display(indentation + "  ");
		}
	}


}
