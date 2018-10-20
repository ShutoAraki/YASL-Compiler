package csc426.ast;

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
} 