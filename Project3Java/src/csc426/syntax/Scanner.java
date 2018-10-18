package csc426.syntax;

import java.io.IOException;
import java.io.Reader;
import java.util.HashMap;
import java.util.Map;

/**
 * A Lexical Analyzer for a subset of YASL. Uses a (Mealy) state machine to
 * extract the next available token from the input each time next() is called.
 * Input comes from a Reader, which will generally be a BufferedReader wrapped
 * around a FileReader or InputStreamReader (though for testing it may also be
 * simply a StringReader).
 * 
 * The documentation is available on README.md written by ShutoAraki.
 * 
 * NOTES:
 * 		For additional keywords, just add String:TokenType pairs in keywords HashMap.
 * 		For additional nonalphabetic tokens, add String:TokenType pairs in opsAndPunct
 * 		HashMap and end_chars list in state 3.
 * 		For more complicated modifications, create new states in Finite State Transducer.
 * 
 * @author bhoward
 * @student ShutoAraki
 */
public class Scanner {
	/**
	 * Construct the Scanner ready to read tokens from the given Reader.
	 * 
	 * @param in
	 */
	public Scanner(Reader in) {
		source = new Source(in);
		
		keywords = new HashMap<>();
		keywords.put("program", TokenType.PROGRAM);
		keywords.put("print", TokenType.PRINT);
		keywords.put("mod", TokenType.MOD);
		keywords.put("div", TokenType.DIV);
		keywords.put("val", TokenType.VAL);
		keywords.put("begin", TokenType.BEGIN);
		keywords.put("end", TokenType.END);
		keywords.put("var", TokenType.VAR);
		keywords.put("int", TokenType.INT);
		keywords.put("bool", TokenType.BOOL);
		keywords.put("void", TokenType.VOID);
		keywords.put("fun", TokenType.FUN);
		keywords.put("if", TokenType.IF);
		keywords.put("then", TokenType.THEN);
		keywords.put("else", TokenType.ELSE);
		keywords.put("while", TokenType.WHILE);
		keywords.put("do", TokenType.DO);
		keywords.put("input", TokenType.INPUT);
		keywords.put("and", TokenType.AND);
		keywords.put("or", TokenType.OR);
		keywords.put("true", TokenType.TRUE);
		keywords.put("false", TokenType.FALSE);
		keywords.put("let", TokenType.LET);

		opsAndPunct = new HashMap<>();
		opsAndPunct.put("+", TokenType.PLUS);
		opsAndPunct.put("-", TokenType.MINUS);
		opsAndPunct.put("*", TokenType.STAR);
		opsAndPunct.put(";", TokenType.SEMI);
		opsAndPunct.put(".", TokenType.PERIOD);
		opsAndPunct.put("=", TokenType.ASSIGN);
		opsAndPunct.put(":", TokenType.COLON);
		opsAndPunct.put("(", TokenType.LPAREN);
		opsAndPunct.put(")", TokenType.RPAREN);
		opsAndPunct.put(",", TokenType.COMMA);
		
	}

	/**
	 * Extract the next available token. When the input is exhausted, it will
	 * return an EOF token on all future calls.
	 * 
	 * @return the next Token object
	 */
	public Token next() {
		
		int state = 0;
		StringBuilder lexeme = new StringBuilder();
		int startLine = source.line;
		int startColumn = source.column;
		
		while (true) {
			switch (state) {
			case 0:
				if (source.atEOF) {
					return new Token(source.line, source.column, TokenType.EOF, null);
				// State 1 deals with the NUM 0
				} else if (source.current == '0') {
					startLine = source.line;
					startColumn = source.column;
					lexeme.append(source.current);
					source.advance();
					state = 1;
				// State 2 deals with all the other NUMs
				} else if (Character.isDigit(source.current)) {
					startLine = source.line;
					startColumn = source.column;
					lexeme.append(source.current);
					source.advance();
					state = 2;
				// State 3 deals with keywords and identifiers
				} else if (Character.isAlphabetic(source.current)) {
					startLine = source.line;
					startColumn = source.column;
					lexeme.append(source.current);
					source.advance();
					state = 3;
			    // States 4-7 deal with comments
				} else if (source.current == '/') {
					state = 4;
					source.advance();
				// State 8 deals with operators and punctuation
				} else if ("+-*;.=:(),".contains(String.valueOf(source.current))) {
					state = 8;
					lexeme.append(source.current);
					source.advance();
				// State 9 deals with <= and <> as well as <
				} else if (source.current == '<') {
					startLine = source.line;
					startColumn = source.column;
					state = 9;
					source.advance();
				// State 10 deals with >= and >
				} else if (source.current == '>') {
					startLine = source.line;
					startColumn = source.column;
					state = 10;
					source.advance();
				// States 11-12 deals with string literal
				} else if (source.current == '"') {
					startLine = source.line;
					startColumn = source.column;
					state = 11;
					source.advance();
				} else if (Character.isWhitespace(source.current)) {
					source.advance();
				} else {
					System.err.println("Unexpected character '" + source.current + "' at line " + source.line + " column " + source.column + " was skipped.");
					source.advance();
				}
				break;
				
			case 1:
				return new Token(startLine, startColumn, TokenType.NUM, lexeme.toString());
				
			case 2:
				if (source.atEOF || !Character.isDigit(source.current)) {
					return new Token(startLine, startColumn, TokenType.NUM, lexeme.toString());
				} else {
					lexeme.append(source.current);
					source.advance();
				}
				break;
			case 3:
				// a list of legal characters to signal the end of lexeme
				String end_chars = "+-*;.=/ \t\n:(),<>";
				
				// Alphabets, digits, and underscores are parts of legal identifier
				if (Character.isAlphabetic(source.current) || Character.isDigit(source.current) || source.current == '_') {
					lexeme.append(source.current);
					source.advance();
				// This case is the end of token
				} else if (end_chars.contains(String.valueOf(source.current))) {
					String lex = lexeme.toString();
					
					if (keywords.containsKey(lex))
						return new Token(startLine, startColumn, keywords.get(lex), null);
					else
						return new Token(startLine, startColumn, TokenType.ID, lex);
				// If there is a illegal character, print an error message
				} else {
					System.err.println("Unexpected character '" + source.current + "' at line " + source.line + " column " + source.column + " was skipped.");
					source.advance();
				}
				break;
			case 4:
				if (source.current == '/') {
					state = 5;
					source.advance();
				}
				else if (source.current == '*') {
					state = 6;
					source.advance();
				}
				else {
					System.err.println("You have to use // for a one-line comment.");
					state = 0;
				}
				break;
			case 5:
				if (source.current == '\n')
					state = 0;
				source.advance();
				break;
			case 6:
				if (source.current == '*')
					state = 7;
				else if (source.atEOF) {
					System.err.println("Comment has to be closed with '*/' before EOF");
					state = 0;
				}
				source.advance();
				break;
			case 7:
				if (source.current == '/')
					state = 0;
				else if (source.current == '*')
					state = 7;
				else if (source.atEOF) {
					System.err.println("Comment has to be closed with '*/' before EOF");
					state = 0;
				} else
					state = 6;
				source.advance();
				break;
			case 8:
				String lex = lexeme.toString();
				return new Token(startLine, startColumn, opsAndPunct.get(lex), null);
			case 9:
				if (source.current == '=') {
					source.advance();
					return new Token(startLine, startColumn, TokenType.LE, null);
				}
				else if (source.current == '>') {
					source.advance();
					return new Token(startLine, startColumn, TokenType.NE, null);
				}
				else
					return new Token(startLine, startColumn, TokenType.LT, null);
			case 10:
				if (source.current == '=') {
					source.advance();
					return new Token(startLine, startColumn, TokenType.GE, null);
				} else 
					return new Token(startLine, startColumn, TokenType.GT, null);
			case 11:
				if (source.current == '"')
					state = 12;
				else if (source.current == '\n')
					System.err.println("String literal has to end with \"");
					// System.exit(1); // Lexical error does not exit the program?
				else
					lexeme.append(source.current);
				source.advance();
				break;
			case 12:
				if (source.current == '"') {
					state = 11;
					// Two quotation marks ("") is considered a " in the string literal
					lexeme.append('"');
					source.advance();
				} else 
					return new Token(startLine, startColumn, TokenType.STRING, lexeme.toString());
				break;
			default:
				// This part will NOT be executed. The error will be thrown just in case.
				throw new RuntimeException("Unreachable. Something is wrong with the lexer.");
			}
		}
	}

	/**
	 * Close the underlying Reader.
	 * 
	 * @throws IOException
	 */
	public void close() throws IOException {
		source.close();
	}

	private Source source;
	private Map<String, TokenType> keywords;
	private Map<String, TokenType> opsAndPunct;
}
