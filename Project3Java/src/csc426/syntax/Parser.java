package csc426.syntax;

import java.util.ArrayList;
import java.util.List;

import csc426.ast.*;

public class Parser {
	public Parser(Scanner scanner) {
		this.scanner = scanner;
		this.current = scanner.next();
	}

	/**
	 * If the current token matches the given type, return it and advance to the
	 * next token; otherwise print an error message and exit. Harsh.
	 * 
	 * @param type
	 */
	private Token match(TokenType type) {
		if (current.type == type) {
			Token result = current;
			current = scanner.next();
			return result;
		} else {
			System.err.println("Expected " + type + " but found " + current);
			System.exit(1);
			return null; // never reaches this
		}
	}

	/**
	 * Check whether the current token's type is one of a given list of type.
	 * 
	 * @param types a variable number of arguments listing the expected token types
	 * @return true if the current token type is in the given list
	 */
	private boolean check(TokenType... types) {
		for (TokenType type : types) {
			if (current.type == type) {
				return true;
			}
		}
		return false;
	}

	/*-
	 * <Program> --> program id ; <Block> . EOF      FIRST = program
	 */
	public Program parseProgram() {
		match(TokenType.PROGRAM);
		Token nameToken = match(TokenType.ID);
		String name = nameToken.lexeme;
		match(TokenType.SEMI);
		Block block = parseBlock();
		match(TokenType.PERIOD);
		match(TokenType.EOF);
		
		return new Program(name, block);
	}
	
	/*
	 * <Block> --> <ValDecls>                      FIRST = VAL
	 */
	public Block parseBlock() {
		ValDecls vs = parseValDecls();
		return new Block(vs);
	}
	
	/*
	 * <ValDecls> --> <ValDecl> <ValDecls>		   FIRST = VAL
	 * 			    | ε							   FOLLOW = VAR
	 */
	public ValDecls parseValDecls() {
		if (check(TokenType.VAL)) {
			List<ValDecl> vdList = new ArrayList<ValDecl>();
			while (check(TokenType.VAL)) {
				vdList.add(parseValDecl());
			}
			return new ValDecls(vdList);
		}
		return null;
	}
	
	/*
	 * <ValDecl> --> val id = <Sign> num ;         FIRST = VAL
	 */
	public ValDecl parseValDecl() {
		match(TokenType.VAL);
		Token id = match(TokenType.ID);
		match(TokenType.ASSIGN);
		int sign = parseSign();
		Token numToken = match(TokenType.NUM);
		int num = sign * Integer.parseInt(numToken.lexeme);
		match(TokenType.SEMI);
		return new ValDecl(id.lexeme, num);
	}
	
	/*
	 * <Sign> --> -								   FIRST = MINUS
	 * 			| ε								   FOLLOW = NUM
	 */
	public int parseSign() {
		if (check(TokenType.MINUS)) {
			match(TokenType.MINUS);
			return -1;
		}
		return 1;
	}

	private Scanner scanner;
	private Token current;
}