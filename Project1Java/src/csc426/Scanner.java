package csc426;

import java.io.IOException;
import java.io.Reader;

/**
 * A Lexical Analyzer for a subset of YASL. Uses a (Mealy) state machine to
 * extract the next available token from the input each time next() is called.
 * Input comes from a Reader, which will generally be a BufferedReader wrapped
 * around a FileReader or InputStreamReader (though for testing it may also be
 * simply a StringReader).
 * 
 * @author bhoward
 */
public class Scanner {
	/**
	 * Construct the Scanner ready to read tokens from the given Reader.
	 * 
	 * @param in
	 */
	public Scanner(Reader in) {
		source = new Source(in);
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
				} else if (source.current == '0') {
					startLine = source.line;
					startColumn = source.column;
					lexeme.append(source.current);
					source.advance();
					state = 1;
				} else if (Character.isDigit(source.current)) {
					startLine = source.line;
					startColumn = source.column;
					lexeme.append(source.current);
					source.advance();
					state = 2;
				} else if (source.current == ';') {
					startLine = source.line;
					startColumn = source.column;
					lexeme.append(source.current);
					source.advance();
					state = 3;
				} else if (source.current == 'v') {
					startLine = source.line;
					startColumn = source.column;
					lexeme.append(source.current);
					source.advance();
					state = 4;
				} else if (source.current == '=') {
					startLine = source.line;
					startColumn = source.column;
					lexeme.append(source.current);
					source.advance();
					state = 8;
				} else if (source.current == '+') {
					startLine = source.line;
					startColumn = source.column;
					lexeme.append(source.current);
					source.advance();
					state = 9;
				} else if (source.current == '-') {
					startLine = source.line;
					startColumn = source.column;
					lexeme.append(source.current);
					source.advance();
					state = 10;
				} else if (source.current == '*') {
					startLine = source.line;
					startColumn = source.column;
					lexeme.append(source.current);
					source.advance();
					state = 11;
				} else if (source.current == 'm') {
					startLine = source.line;
					startColumn = source.column;
					lexeme.append(source.current);
					source.advance();
					state = 12;
				} else if (source.current == 'd') {
					startLine = source.line;
					startColumn = source.column;
					lexeme.append(source.current);
					source.advance();
					state = 15;
				} else if (source.current == 'p') {
					startLine = source.line;
					startColumn = source.column;
					lexeme.append(source.current);
					source.advance();
					state = 18;
				} else if (source.current == 'b') {
					startLine = source.line;
					startColumn = source.column;
					lexeme.append(source.current);
					source.advance();
					state = 25;
				} else if (source.current == 'e') {
					startLine = source.line;
					startColumn = source.column;
					lexeme.append(source.current);
					source.advance();
					state = 30;
				} else if (source.current == '.') {
					startLine = source.line;
					startColumn = source.column;
					lexeme.append(source.current);
					source.advance();
					state = 33;
				} else if (Character.isAlphabetic(source.current)){
						startLine = source.line;
						startColumn = source.column;
						lexeme.append(source.current);
						source.advance();
						state = 7;
				} else if (Character.isWhitespace(source.current)) {
					source.advance();
				} else if (source.current == '/') {
					source.advance();
					state = 37;
				} else {
					System.err.println("Illegal character: " + source.current);
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
				/*
				if (source.current == '\n' || source.atEOF) {
					return new Token(startLine, startColumn, TokenType.SEMI, lexeme.toString());
				} else {
					System.err.println();
				}
				*/
				return new Token(startLine, startColumn, TokenType.SEMI, null);
				
			case 4:
				if (source.current == 'a') {
					lexeme.append(source.current);
					source.advance();
					state = 5;
				} else {
					// questionable code
					lexeme.append(source.current);
					source.advance();
					state = 7;
				}
				break;
			case 5:
				if (source.current == 'l') {
					lexeme.append(source.current);
					source.advance();
					state = 6;
				} else {
					lexeme.append(source.current);
					source.advance();
					state = 7;
				}
				break;
			case 6:
				return new Token(startLine, startColumn, TokenType.VAL, null);
			case 7:
				if (Character.isAlphabetic(source.current) || Character.isDigit(source.current) || source.current == '_' || source.current == '-') {
					lexeme.append(source.current);
					source.advance();
				} else if (source.atEOF || Character.isWhitespace(source.current) || source.current == '\n' || source.current == '.' || source.current == ';') {
					return new Token(startLine, startColumn, TokenType.ID, lexeme.toString());
				} else {
					lexeme.append(source.current);
					System.err.println("Illegal character: " + source.current);
					source.advance();
				}
				break;
			case 8:
				return new Token(startLine, startColumn, TokenType.ASSIGN, null);
			case 9:
				return new Token(startLine, startColumn, TokenType.PLUS, null);
			case 10:
				return new Token(startLine, startColumn, TokenType.MINUS, null);
			case 11:
				return new Token(startLine, startColumn, TokenType.STAR, null);
			case 12:
				if (source.current == 'o') {
					lexeme.append(source.current);
					source.advance();
					state = 13;
				} else {
					lexeme.append(source.current);
					source.advance();
					state = 7;
				}
				break;
			case 13:
				if (source.current == 'd') {
					lexeme.append(source.current);
					source.advance();
					state = 14;
				} else {
					lexeme.append(source.current);
					source.advance();
					state = 7;
				}
				break;
			case 14:
				if (source.atEOF || Character.isWhitespace(source.current) || source.current == '\n' || source.current == '.' || source.current == ';')
					return new Token(startLine, startColumn, TokenType.MOD, null);
				else {
					lexeme.append(source.current);
					source.advance();
					state = 7;
				}
				break;
			case 15:
				if (source.current == 'i') {
					lexeme.append(source.current);
					source.advance();
					state = 16;
				} else {
					lexeme.append(source.current);
					source.advance();
					state = 7;
				}
				break;
			case 16:
				if (source.current == 'v') {
					lexeme.append(source.current);
					source.advance();
					state = 17;
				} else {
					lexeme.append(source.current);
					source.advance();
					state = 7;
				}
				break;
			case 17:
				if (source.atEOF || Character.isWhitespace(source.current) || source.current == '\n' || source.current == '.' || source.current == ';')
					return new Token(startLine, startColumn, TokenType.DIV, null);
				else {
					lexeme.append(source.current);
					source.advance();
					state = 7;
				}
				break;
			case 18:
				if (source.current == 'r') {
					lexeme.append(source.current);
					source.advance();
					state = 19;
				} else {
					lexeme.append(source.current);
					source.advance();
					state = 7;
				}
				break;
			case 19:
				if (source.current == 'o') {
					lexeme.append(source.current);
					source.advance();
					state = 20;
				} else if (source.current == 'i') {
					lexeme.append(source.current);
					source.advance();
					state = 34;
				} else {
					lexeme.append(source.current);
					source.advance();
					state = 7;
				}
				break;
			case 20:
				if (source.current == 'g') {
					lexeme.append(source.current);
					source.advance();
					state = 21;
				} else {
					lexeme.append(source.current);
					source.advance();
					state = 7;
				}
				break;
			case 21:
				if (source.current == 'r') {
					lexeme.append(source.current);
					source.advance();
					state = 22;
				} else {
					lexeme.append(source.current);
					source.advance();
					state = 7;
				}
				break;
			case 22:
				if (source.current == 'a') {
					lexeme.append(source.current);
					source.advance();
					state = 23;
				} else {
					lexeme.append(source.current);
					source.advance();
					state = 7;
				}
				break;
			case 23:
				if (source.current == 'm') {
					lexeme.append(source.current);
					source.advance();
					state = 24;
				} else {
					lexeme.append(source.current);
					source.advance();
					state = 7;
				}
				break;
			case 24:
				if (source.atEOF || Character.isWhitespace(source.current) || source.current == '\n' || source.current == '.' || source.current == ';')
					return new Token(startLine, startColumn, TokenType.PROGRAM, null);
				else {
					lexeme.append(source.current);
					source.advance();
					state = 7;
				}
				break;
			case 25:
				if (source.current == 'e') {
					lexeme.append(source.current);
					source.advance();
					state = 26;
				} else {
					lexeme.append(source.current);
					source.advance();
					state = 7;
				}
				break;
			case 26:
				if (source.current == 'g') {
					lexeme.append(source.current);
					source.advance();
					state = 27;
				} else {
					lexeme.append(source.current);
					source.advance();
					state = 7;
				}
				break;
			case 27:
				if (source.current == 'i') {
					lexeme.append(source.current);
					source.advance();
					state = 28;
				} else {
					lexeme.append(source.current);
					source.advance();
					state = 7;
				}
				break;
			case 28:
				if (source.current == 'n') {
					lexeme.append(source.current);
					source.advance();
					state = 29;
				} else {
					lexeme.append(source.current);
					source.advance();
					state = 7;
				}
				break;
			case 29:
				if (source.atEOF || Character.isWhitespace(source.current) || source.current == '\n' || source.current == '.' || source.current == ';')
					return new Token(startLine, startColumn, TokenType.BEGIN, null);
				else {
					lexeme.append(source.current);
					source.advance();
					state = 7;
				}
				break;
			case 30:
				if (source.current == 'n') {
					lexeme.append(source.current);
					source.advance();
					state = 31;
				} else {
					lexeme.append(source.current);
					source.advance();
					state = 7;
				}
				break;
			case 31:
				if (source.current == 'd') {
					lexeme.append(source.current);
					source.advance();
					state = 32;
				} else {
					lexeme.append(source.current);
					source.advance();
					state = 7;
				}
				break;
			case 32:
				if (source.atEOF || Character.isWhitespace(source.current) || source.current == '\n' || source.current == '.' || source.current == ';')
					return new Token(startLine, startColumn, TokenType.END, null);
				else {
					lexeme.append(source.current);
					source.advance();
					state = 7;
				}
				break;
			case 33:
				return new Token(startLine, startColumn, TokenType.PERIOD, null);
			case 34:
				if (source.current == 'n') {
					lexeme.append(source.current);
					source.advance();
					state = 35;
				} else {
					lexeme.append(source.current);
					source.advance();
					state = 7;
				}
				break;
			case 35:
				if (source.current == 't') {
					lexeme.append(source.current);
					source.advance();
					state = 36;
				} else {
					lexeme.append(source.current);
					source.advance();
					state = 7;
				}
				break;
			case 36:
				if (source.atEOF || Character.isWhitespace(source.current) || source.current == '\n' || source.current == '.' || source.current == ';')
					return new Token(startLine, startColumn, TokenType.PRINT, null);
				else {
					lexeme.append(source.current);
					source.advance();
					state = 7;
				}
				break;
			case 37:
				if (source.current == '/') {
					source.advance();
					state = 38;
				} else if (source.current == '*') {
					source.advance();
					state = 39;
				} else {
					System.err.println("Illegal character: " + source.current);
					source.advance();
					state = 0;
				}
				break;
			case 38:
				if (source.current == '\n') {
					source.advance();
					state = 0;
				} else
					source.advance();
				break;
			case 39:
				if (source.current == '*') {
					source.advance();
					state = 40;
				} else 
					source.advance();
				break;
			case 40:
				if (source.current == '/') {
					source.advance();
					state = 0;
				} else {
					source.advance();
					state = 39;
				}
				break;
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
}
