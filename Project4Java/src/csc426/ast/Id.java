package csc426.ast;

public class Id extends Expr {

	private String id;
	
	public Id(String id) {
		this.id = id;
	}

	@Override
	public void display(String indentation) {
		System.out.println(indentation + "Id " + id);
	}
	
}
