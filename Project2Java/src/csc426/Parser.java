package csc426;

import java.util.HashMap;
import java.util.Stack;

/**
 * A Parser for a subset of YASL. It can only parse variable declarations 
 * and print statements given that the statements are grammatical.
 * The runParser() method prints out expression in print statements in postfix
 * order. The parser looks for variable declarations and print statements.
 * Print statements have to start with a BEGIN token and end with an END token, 
 * separated by a SEMI token for each print statement.
 * 
 * @version 09/26/2018
 * @author ShutoAraki
 *
 */

public class Parser {
	
	private Scanner scanner;
	private Token token;
	private HashMap<String, String> ids;
	private HashMap<TokenType, Integer> precedence;

	public Parser(Scanner s) {
		scanner = s;
		token = scanner.next();
		ids = new HashMap<String, String>();
		// Set up the precedence map
		precedence = new HashMap<TokenType, Integer>();
		precedence.put(TokenType.PRINT, 0);
		precedence.put(TokenType.PLUS, 1);
		precedence.put(TokenType.MINUS, 1);
		precedence.put(TokenType.STAR, 2);
		precedence.put(TokenType.DIV, 2);
		precedence.put(TokenType.MOD, 2);
	}
	
	public void runParser() throws Exception {
		
		// Skip whatever is before val declaration
		while (token.type != TokenType.VAL && token.type != TokenType.BEGIN) {
			token = scanner.next();
		}
		// parseValDecls part
		while (token.type == TokenType.VAL) {
			checkToken(TokenType.ID);
			String key = token.lexeme.toString();
			checkToken(TokenType.ASSIGN);
			checkToken(TokenType.NUM);
			String value = token.lexeme.toString();
			ids.put(key, value);
			checkToken(TokenType.SEMI);
			token = scanner.next();
		}
		token = scanner.next();
		// Parse the print statements using stack
		while (token.type != TokenType.END) {
			while (token.type == TokenType.PRINT) {
				parseStmtStack();
				if (token.type == TokenType.SEMI)
					// This should be a PRINT token (but we don't need to check)
					token = scanner.next();
				else if (token.type == TokenType.END)
					break;
			}
		}
		
	}
	
	/**
	 * This method parse a print statement using an operator stack and precedence.
	 * 
	 * precondition: The print statement is grammatically correct.
	 * This method does not take care of any ungrammatical statements.
	 * It throws an exception when an identifier is not declared.
	 * 
	 * @throws Exception
	 */
	public void parseStmtStack() throws Exception {
		Stack<Token> opStack = new Stack<Token>();
		
		// Push the PRINT token onto the stack first
		opStack.push(token);
		
		// Do stack algorithm until the stack gets empty
		while (!opStack.isEmpty()) {
			token = scanner.next();
			
			// Numbers
			if (token.type == TokenType.NUM)
				System.out.println(token.lexeme.toString());
			// Identifiers
			else if (token.type == TokenType.ID) {
				if (ids.containsKey(token.lexeme.toString()))
					System.out.println(ids.get(token.lexeme.toString()));
				else 
					throw new Exception("The identifier " + token.lexeme.toString() + " is not declared.");
			// Operators
			} else if (token.type == TokenType.PLUS || token.type == TokenType.MINUS || token.type == TokenType.STAR || token.type == TokenType.DIV || token.type == TokenType.MOD) {
				Token poppedOp = opStack.pop();
				while (precedence.get(token.type) <= precedence.get(poppedOp.type)) {
					System.out.println(poppedOp.type);
					poppedOp = opStack.pop();
				}
				opStack.push(poppedOp); // put the popped operator (low precedence) push back
				opStack.push(token);
			} else {
				// Empty the opStack
				while (!opStack.isEmpty()) {
					System.out.println(opStack.pop().type);
				}
			}
		}
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
