package csc426.ast;

import csc426.syntax.TokenType;

public class Type {
	
	private TokenType tokenType;
	
	public Type(TokenType tokenType) {
		this.tokenType = tokenType;
	}
	
	public String toString() {
		if (tokenType == TokenType.INT)
			return "Int";
		else if (tokenType == TokenType.BOOL) 
			return "Bool";
		else if (tokenType == TokenType.VOID)
			return "Void";
		return "";
	}

}
