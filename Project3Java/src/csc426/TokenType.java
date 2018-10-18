package csc426;

/**
 * Enumeration of the different kinds of tokens in the YASL subset.
 * 
 * @author bhoward
 * @student ShutoAraki
 */
public enum TokenType {
	NUM, // numeric literal
	SEMI, // semicolon (;)
	PERIOD, // period (.)
	ASSIGN, // assign operator (=)
	PLUS, // plus operator (+)
	MINUS, // minus operator (-)
	STAR, // times operator (*)
	DIV, // division operator (div)
	MOD, // mod operator (mod)
	PROGRAM, // program keyword
	VAL, // val keyword
	BEGIN, // begin keyword
	END, // end keyword
	PRINT, // print keyword
	VAR, // var keyword
	INT, // int keyword
	BOOL, // bool keyword
	VOID, // void keyword
	FUN, // function keyword
	IF, // if keyword
	THEN, // then keyword
	ELSE, // else keyword
	WHILE, // while keyword
	DO, // do keyword
	INPUT, // input keyword
	AND, // and keyword
	OR, // or keyword
	NOT, // not keyword
	TRUE, // true keyword
	FALSE, // false keyword
	COLON, // :
	LPAREN, // (
	RPAREN, // )
	COMMA, //,
	LE, // <=
	LT, // <
	GE, // >=
	GT, // >
	NE, // <>
	EQ, // ==
	LET, // let keyword
	ID, // identifier
	STRING, // String literal
	EOF // end-of-file
}
