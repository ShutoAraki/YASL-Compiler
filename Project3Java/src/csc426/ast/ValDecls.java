package csc426.ast;

import java.util.List;

public class ValDecls {

	private List<ValDecl> vals;
	
	public ValDecls(List<ValDecl> vals) {
		this.vals = vals;
	}
	
	public void display(String indentation) {
		for (ValDecl val : vals) {
			val.display(indentation + "  ");
		}
	}

}
