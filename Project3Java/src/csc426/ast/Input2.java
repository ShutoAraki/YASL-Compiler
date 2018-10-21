package csc426.ast;

public class Input2 extends Stmt {
	
	private String msg;
	private String id;

	public Input2(String msg, String id) {
		this.msg = msg;
		this.id = id;
	}

	@Override
	public void display(String indentation) {
		System.out.println(indentation + "Input2 \"" + msg + "\", " + id);
	}

}
