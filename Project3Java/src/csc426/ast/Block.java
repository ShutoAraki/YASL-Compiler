package csc426.ast;

public class Block {
	
	private ValDecls valDecls;
//	private VarDecls varDecls;
//	private FunDecls funDecls;
//	private Stmt stmt;
	
//	public Block(ValDecls valDecls, VarDecls varDecls, FunDecls funDecls, Stmt stmt) {
	public Block(ValDecls valDecls) {
		this.valDecls = valDecls;
//		this.varDecls = varDecls;
//		this.funDecls = funDecls;
//		this.stmt = stmt;
	}
	
	public void display(String indentation) {
        System.out.println(indentation + "Block");
        valDecls.display(indentation + "  ");
//        varDecls.display(indentation + "  ");
//        funDecls.display(indentation + "  ");
//        stmt.display(indentation + "  ");
    }
}
