package csc426.ast;

public class StringItem extends Item {

	private String msg;

	public StringItem(String msg) {
		this.msg = msg;
	}
	
	public String getMsg() { return msg; }
	
	@Override
	public void display(String indentation) {
		System.out.println(indentation + "StringItem \"" + msg + "\"");
	}

}
