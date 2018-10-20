package csc426.ast;

import java.util.List;

public class Params extends ASTNode {

	private List<Param> params;
	
	public Params(List<Param> params) {
		this.params = params;
	}
	
	public List<Param> getParams() { return params; }
	
	public void display(String indentation) {
		for (Param p : params) {
			p.display(indentation + "  ");
		}
	}
}
