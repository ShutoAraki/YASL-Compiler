package csc426.ast;

import java.util.List;

public class FunDecl extends ASTNode {

	private String id;
	private Type type;
	private List<Param> ps;
	private Block block;

	public FunDecl(String id, Type type, List<Param> ps, Block block) {
		this.id = id;
		this.type = type;
		this.ps = ps;
		this.block = block;
	}
	
	public void display(String indentation) {
		System.out.println(indentation + "Fun " + id + " : " + type);
		for (Param p : ps)
				p.display(indentation + "  ");
		block.display(indentation + "  ");
	}

}
