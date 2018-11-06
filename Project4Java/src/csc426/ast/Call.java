package csc426.ast;

import java.util.ArrayList;
import java.util.List;

import csc426.interp.FunValue;
import csc426.interp.SymbolTable;
import csc426.interp.Value;

public class Call extends Expr {
	
	private String id;
	private List<Expr> args;
	
	public Call(String id, List<Expr> args) {
		this.id = id;
		this.args = args;
	}
	
	public Value call(List<Param> params, Block block, List<Value> as, SymbolTable t) {
		
		if (params.size() != as.size()) {
			System.err.println("The number of parameters does not match with the number of arguemnts in function " + id);
			System.exit(0);
		} else {
			for (int i = 0; i < params.size(); i++) {
				t.bind(params.get(i).getId(), as.get(i));
			}
			return block.interpret(t);
		}
		return null; //unreachable
	}

	@Override
	public void display(String indentation) {
		System.out.println(indentation + "Call " + id);
		for (Expr arg : args) {
			arg.display(indentation + "  ");
		}
	}
	
	@Override
	public Value interpret(SymbolTable t) {
		
		FunValue funV = (FunValue) t.lookup(id);
		
		List<Param> params = funV.getParams(); 
		Block block = funV.getBlock();
		List<Value> as = new ArrayList<Value>();
		
		for (Expr e : args) {
			as.add(e.interpret(t));
		}
		
		t.enter();
		Value result = call(params, block, as, t);
		t.exit();
		
		return result;
	}
	
}
