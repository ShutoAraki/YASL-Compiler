package csc426.ast;

import java.util.List;

public class Block extends ASTNode {
	
	private List<ValDecl> valDecls;
	private List<VarDecl> varDecls;
	private List<FunDecl> funDecls; 
//	private Stmt stmt;
	
//	public Block(ValDecls valDecls, VarDecls varDecls, FunDecls funDecls, Stmt stmt) {
	public Block(List<ValDecl> valDecls, List<VarDecl> varDecls, List<FunDecl> funDecls) {
		this.valDecls = valDecls;
		this.varDecls = varDecls;
		this.funDecls = funDecls;
//		this.stmt = stmt;
	}
	
	public void display(String indentation) {
        System.out.println(indentation + "Block");
        
        for (ValDecl val : valDecls)
        		val.display(indentation + "  ");
        for (VarDecl var : varDecls)
        		var.display(indentation + "  ");
        for (FunDecl fun : funDecls)
        		fun.display(indentation + "  ");
        
//        stmt.display(indentation + "  ");
    }
}
