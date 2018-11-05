package csc426.ast;

public class Param extends ASTNode {

	private String id;
	private Type type;

	public Param(String id, Type type) {
		this.id = id;
		this.type = type;
	}
	
	public String getId() { return id; }
	
	public Type getType() { return type; }
	
	public void display(String indentation) {
		System.out.println(indentation + "Val " + id + " : " + type);
	}
	
	public String toString() {
		return id + " : " + type;
	}

}
