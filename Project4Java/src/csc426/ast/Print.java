package csc426.ast;

import java.util.List;

public class Print extends Stmt {
	
	private List<Item> its;

	public Print(List<Item> its) {
		this.its = its;
	}
	
	@Override
	public void display(String indentation) {
		System.out.println(indentation + "Print");
		
		for (Item it : its) {
			it.display(indentation + "  ");
		}
	}

}
