package csc426;

import java.util.HashMap;

public class Parser {
	
	private Scanner scanner;
	private Token token;
	private HashMap<String, Integer> ids;

	public Parser(Scanner s) {
		scanner = s;
		token = null;
		ids = new HashMap<String, Integer>();
	}
	
	public void parseProgram() {
		checkToken(TokenType.PROGRAM);
		checkToken(TokenType.ID);
		checkToken(TokenType.SEMI);
		parseBlock();
		checkToken(TokenType.PERIOD);
	}
	
	public void parseBlock() {
		parseValDecls();
		checkToken(TokenType.BEGIN);
		parseStmts();
		checkToken(TokenType.END);
	}
	
	public void parseValDecls() {
		parseValDecl();
		parseValDecls();
	}
	
	public void parseValDecl() {
		checkToken(TokenType.VAL);
		checkToken(TokenType.ID);
		String key = token.lexeme.toString();
		checkToken(TokenType.ASSIGN);
		checkToken(TokenType.NUM);
		Integer value = Integer.valueOf(token.lexeme.toString());
		ids.put(key, value);
		System.out.println(ids);
		checkToken(TokenType.SEMI);
	}
	
	//TODO: CHECK THIS METHOD LATER
	public void parseStmts() {
		parseStmt();
		stmtRest();
	}
	
	public void stmtRest() {
		if (checkToken(TokenType.SEMI)) // This if statement shouldn only check, not consume the token.
			parseStmts();
		else
			;
	}
	
	public void parseStmt() {
		checkToken(TokenType.PRINT);
		parseExpr();
	}
	
	public void parseExpr() {
		
	}
	
	public boolean checkToken(TokenType tt) {
		token = scanner.next();
		if (token.type == tt)
			return true;
		else {
			System.err.println("Syntax error: Type " + tt.toString() + " expected at " + token.line + ":" + token.column);
			return false;
		}
	}
}
