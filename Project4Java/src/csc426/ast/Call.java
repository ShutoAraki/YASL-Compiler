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
		
		System.out.println("Function name: " + id);
		System.out.println("Params: " + params);
		System.out.println("Evaluated args: " + as);
		
		if (params.size() == 0 && as.size() == 0) {
			return block.interpret(t);
		} else if (params.size() != as.size()){
			System.err.println("The number of parameters does not match with the number of arguments.");
			System.exit(0);
		} else {
			Param firstParam = params.remove(0);
			Value firstArg = as.remove(0);
			
			String pId = firstParam.getId();
//			Type pType = firstParam.getType();
			
			// TODO: A little bit of type checking?
			System.out.println(firstParam + "  |  " + firstArg);
			System.out.println();
			
			t.bind(pId, firstArg);
			return call(params, block, as, t);
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
		
		System.out.println(funV);
		
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
