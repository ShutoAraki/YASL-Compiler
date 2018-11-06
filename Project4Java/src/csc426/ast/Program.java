package csc426.ast;

//import java.util.HashMap;

import csc426.interp.SymbolTable;

public class Program extends ASTNode {

	private String name;
    private Block block;

    public Program(String name, Block block) {
        this.name = name;
        this.block = block;
    }

    public String getName() { return name; }
    
    public Block getBlock() { return block; }
    
    public void display(String indentation) {
        System.out.println(indentation + "Program " + name);
        block.display(indentation + "  ");
    }
    
    public void interpret() {
    		SymbolTable t = new SymbolTable();
    		t.enter();
    		block.interpret(t);
    		t.exit();
    }
} 