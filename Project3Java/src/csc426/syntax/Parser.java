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

	/*
	 * <Program> --> program id ; <Block> . EOF    FIRST = program
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
	 * <Block> --> <ValDecls> <VarDecls> <FunDecls> <Stmt>     FIRST = VAL, VAR, FUN, FIRST(Stmt)
	 */
	private Block parseBlock() {
		List<ValDecl> vs = parseValDecls();
		List<VarDecl> rs = parseVarDecls();
		List<FunDecl> fs = parseFunDecls();
		
		return new Block(vs, rs, fs);
	}
	
	/*
	 * <ValDecls> --> <ValDecl> <ValDecls>		   FIRST = VAL
	 *              | ε							   FOLLOW = VAR
	 */
	private List<ValDecl> parseValDecls() {
		List<ValDecl> vdList = new ArrayList<ValDecl>();
		while (check(TokenType.VAL)) {
			vdList.add(parseValDecl());
		}
		return vdList;
	}
	
	/*
	 * <ValDecl> --> val id = <Sign> num ;         FIRST = VAL
	 */
	private ValDecl parseValDecl() {
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
	 *          | ε								   FOLLOW = NUM
	 */
	private int parseSign() {
		if (check(TokenType.MINUS)) {
			match(TokenType.MINUS);
			return -1;
		}
		return 1;
	}
	
	/*
	 * <VarDecls> --> <VarDecl> <VarDecls>         FIRST = VAR
	 *              | ε                            FOLLOW = FUN, FIRST(Stmt)
	 */
	private List<VarDecl> parseVarDecls() {
		List<VarDecl> vrList = new ArrayList<VarDecl>();
		while (check(TokenType.VAR)) {
			vrList.add(parseVarDecl());
		}
		return vrList;
	}
	
	/*
	 * <VarDecl> --> var id : <Type> ;             FIRST = VAR
	 */
	private VarDecl parseVarDecl() {
		match(TokenType.VAR);
		Token id = match(TokenType.ID);
		match(TokenType.COLON);
		Type type = parseType();
		match(TokenType.SEMI);
		return new VarDecl(id.lexeme, type);
	}
	
	// Better implementation? Too much redundant code
	private Type parseType() {
		
		if (check(TokenType.INT)) {
			match(TokenType.INT);
			return Type.Int;
		} else if (check(TokenType.BOOL)) {
			match(TokenType.BOOL);
			return Type.Bool;
		} else if (check(TokenType.VOID)) {
			match(TokenType.VOID);
			return Type.Void;
		} else {
			System.err.println("Invalid var type: Var type has to be INT, BOOL, or VOID");
			System.exit(1);
		}
		
		return null; // Unreachable
	}
	
	/*
	 * <FunDecls> --> <FunDecl> <FunDecls>         FIRST = FUN
	 *              | ε                            FOLLOW = FIRST(Stmt)
	 */
	private List<FunDecl> parseFunDecls() {
		List<FunDecl> fdList = new ArrayList<FunDecl>();
		while (check(TokenType.FUN)) {
			fdList.add(parseFunDecl());
		}
		return fdList;
	}
	
	/*
	 * <FunDecl> --> fun id ( <ParamList> ) : <Type> ; <Block> ;    FIRST = FUN
	 */
	private FunDecl parseFunDecl() {
		match(TokenType.FUN);
		Token id = match(TokenType.ID);
		match(TokenType.LPAREN);
		List<Param> ps = parseParamList();
		match(TokenType.RPAREN);
		match(TokenType.COLON);
		Type type = parseType();
		match(TokenType.SEMI);
		Block block = parseBlock();
		match(TokenType.SEMI);
		return new FunDecl(id.lexeme, type, ps, block);
	}
	
	/*
	 * <ParamList> --> <Params>                    FIRST = ID
	 *               | ε                           FOLLOW = RPAREN
	 */
	private List<Param> parseParamList() {
		if (check(TokenType.ID))
			return parseParams().getParams();
		else 
			return new ArrayList<Param>();
	}
	
	/*
	 * <Params> --> <Param> <ParamsRest>           FIRST = ID
	 */
	private Params parseParams() {
		List<Param> ps = new ArrayList<Param>();
		Param param = parseParam();
		ps.add(param);
		ps.addAll(parseParamsRest());
		return new Params(ps);
	}
	
	/*
	 * <ParamsRest> --> , <Params>                 FIRST = COMMA
	 *                | ε                          FOLLOW = SEMI
	 */
	private List<Param> parseParamsRest() {
		if (check(TokenType.COMMA)) {
			match(TokenType.COMMA);
			Params ps = parseParams();
			return ps.getParams();
		} else
			return new ArrayList<Param>();
	}
	
	/*
	 * <Param> --> id : <Type>                     FIRST = ID
	 */
	private Param parseParam() {
		Token id = match(TokenType.ID);
		match(TokenType.COLON);
		Type type = parseType();
		return new Param(id.lexeme, type);
	}
	
	
	

	
	
	
	/*
	 * <Stmt> --> let id = <Expr>                  FIRST = LET
	 *          | begin <StmtList> end             FIRST = BEGIN
	 *          | if <Expr> then <Stmt> <StmtRest> FIRST = IF
	 *          | while <Expr> do <Stmt>           FIRST = WHILE
	 *          | input string <StmtRest2>         FIRST = INPUT
	 *          | print <Items>                    FIRST = PRINT
	 *          | <Expr>                           FIRST = NUM, ID, TRUE, FALSE, MINUS, NOT, LPAREN
	 */
//	private Stmt parseStmt() {
//		if (check(TokenType.LET)) {
//			match(TokenType.LET);
//			Token id = match(TokenType.ID);
//			match(TokenType.ASSIGN);
//			Expr expr = parseExpr();
//			return new Assign(id.lexeme, expr);
//		} else if (check(TokenType.BEGIN)) {
//			match(TokenType.BEGIN);
//			List<Stmt> ss = parseStmtList();
//			match(TokenType.END);
//			return new Sequence(ss);
//		}
//	}


	/*
	 * <Expr> --> <SimpleExpr> <ExprRest>          FIRST = NUM, ID, TRUE, FALSE, MINUS, NOT, LPAREN
	 */
//	private Expr parseExpr() {
//		if (check(TokenType.NUM, TokenType.ID, TokenType.TRUE, TokenType.FALSE, TokenType.MINUS, TokenType.NOT, TokenType.LPAREN)) {
//			parseSimpleExpr();
//			parseExprRest();
//		}
//	}
	
	/*
	 * <ExprRest> --> <RelOp> <SimpleExpr>         FIRST = EQ, NE, LE, GE, LT, GT
	 *              | ε                            FOLLOW = THEN, DO, SEMI, COMMA
	 */
//	private Expr parseExprRest() {
//		if (check(TokenType.EQ, TokenType.NE, TokenType.LE, TokenType.GE, TokenType.LT, TokenType.GT)) {
//			parseRelOp();
//			parseSimpleExpr();
//		}
//	}
	
	/*
	 * <SimpleExpr> --> <Term> <SERest>            FIRST = NUM, ID, TRUE, FALSE, MINUS, NOT, LPAREN
	 */
//	private Expr parseSimpleExpr() {
//		if (check(TokenType.NUM, TokenType.ID, TokenType.TRUE, TokenType.FALSE, TokenType.MINUS, TokenType.NOT, TokenType.LPAREN)) {
//			parseTerm();
//			parseSERest();
//		}
//	}
	
	/*
	 * <SERest> --> <AddOp> <SimpleExpr>           FIRST = PLUS, MINUS, OR
	 *            | ε                              FOLLOW = FIRST(MulOp) = STAR, DIV, MOD, AND
	 */
//	private Expr parseSERest() {
//		if(check(TokenType.PLUS, TokenType.MINUS, TokenType.OR)) {
//			parseAddOp();
//			parseSimpleExpr();
//		}
//	}

	

	private Scanner scanner;
	private Token current;
}