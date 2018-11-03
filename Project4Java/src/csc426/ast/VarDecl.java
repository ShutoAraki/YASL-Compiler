package csc426.ast;

public class VarDecl {

	private String id;
	private Type type;

	public VarDecl(String id, Type type) {
		this.id = id;
		this.type = type;
	}
	
	public void display(String indentation) {
		System.out.println(indentation + "Var " + id + " : " + type);
	}
}
