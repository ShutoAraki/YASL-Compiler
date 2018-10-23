package csc426.ast;

public class Num extends Expr {

	private int value;
	
	public Num(int value) {
		this.value = value;
	}
	
	@Override
	public void display(String indentation) {
		System.out.println(indentation + "Num " + value);
	}
}
