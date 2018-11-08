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
		Stmt s = parseStmt();
		
		return new Block(vs, rs, fs, s);
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
			return parseParams();
		else 
			return new ArrayList<Param>();
	}
	
	/*
	 * <Params> --> <Param> <ParamsRest>           FIRST = ID
	 */
	private List<Param> parseParams() {
		Param p = parseParam();
		return parseParamsRest(p);
	}
	
	/*
	 * <ParamsRest> --> , <Params>                 FIRST = COMMA
	 *                | ε                          FOLLOW = SEMI
	 */
	private List<Param> parseParamsRest(Param p) {
		if (check(TokenType.COMMA)) {
			match(TokenType.COMMA);
			List<Param> ps = parseParams();
			ps.add(0, p);
			return ps;
		} else {
			List<Param> ps = new ArrayList<Param>();
			ps.add(p);
			return ps;
		}
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
	private Stmt parseStmt() {
		if (check(TokenType.LET)) {
			match(TokenType.LET);
			Token id = match(TokenType.ID);
			match(TokenType.ASSIGN);
			Expr expr = parseExpr();
			return new Assign(id.lexeme, expr);
		} else if (check(TokenType.BEGIN)) {
			match(TokenType.BEGIN);
			List<Stmt> ss = parseStmtList();
			match(TokenType.END);
			return new Sequence(ss);
		} else if (check(TokenType.IF)) {
			match(TokenType.IF);
			Expr test = parseExpr();
			match(TokenType.THEN);
			Stmt s1 = parseStmt();
			return parseStmtRest(test, s1);
		} else if (check(TokenType.WHILE)) {
			match(TokenType.WHILE);
			Expr test = parseExpr();
			match(TokenType.DO);
			Stmt s1 = parseStmt();
			return new While(test, s1);
		} else if (check(TokenType.INPUT)) {
			match(TokenType.INPUT);
			String msg = match(TokenType.STRING).lexeme;
			return parseStmtRest2(msg);
		} else if (check(TokenType.PRINT)) {
			match(TokenType.PRINT);
			List<Item> its = parseItems();
			return new Print(its);
		} else if (check(TokenType.NUM, TokenType.ID, TokenType.TRUE, TokenType.FALSE, 
				         TokenType.MINUS, TokenType.NOT, TokenType.LPAREN)) {
			Expr expr = parseExpr();
			return new ExprStmt(expr);
		} else {
			System.err.println("Invalid statement: " + current);
			System.exit(1);
		}
		return null; // unreachable
	}
	
	/*
	 * <Items> --> <Item> <ItemRest>
	 */
	private List<Item> parseItems() {
		Item it = parseItem();
		return parseItemRest(it);
	}
	
	/*
	 * <ItemRest> --> , <Items>
	 *              | ε
	 */
	private List<Item> parseItemRest(Item it) {
		if (check(TokenType.COMMA)) {
			match(TokenType.COMMA);
			List<Item> its = parseItems();
			its.add(0, it);
			return its;
		} else {
			List<Item> its = new ArrayList<Item>();
			its.add(it);
			return its;
		}
	}
	
	/*
	 * <Item> --> string                           FIRST = STRING
	 *          | <Expr>                           FIRST = NUM, ID, TRUE, FALSE, MINUS, NOT, LPAREN
	 */
	private Item parseItem() {
		if (check(TokenType.STRING)) {
			String msg = match(TokenType.STRING).lexeme;
			return new StringItem(msg);
		} else if (check(TokenType.NUM, TokenType.ID, TokenType.TRUE, TokenType.FALSE, 
		                 TokenType.MINUS, TokenType.NOT, TokenType.LPAREN)) {
			Expr expr = parseExpr();
			return new ExprItem(expr);
		} else {
			System.err.println("Invalid item: " + current);
			System.exit(1);
		}
		return null; // unreachable
	}
	
	
	/*
	 * <StmtRest> --> else <Stmt>
	 *              | ε
	 */
	private Stmt parseStmtRest(Expr test, Stmt s1) {
		if (check(TokenType.ELSE)) {
			match(TokenType.ELSE);
			Stmt s2 = parseStmt();
			return new IfThenElse(test, s1, s2);
		} else {
			return new IfThen(test, s1);
		}
	}
	
	/*
	 * <StmtRest2> --> ε
	 *               | , id
	 */
	private Stmt parseStmtRest2(String msg) {
		if (check(TokenType.COMMA)) {
			match(TokenType.COMMA);
			String id = match(TokenType.ID).lexeme;
			return new Input2(msg, id);
		} else {
			return new Input(msg);
		}
	}
	
	
	/*
	 * <StmtList> --> <Stmts>                      FIRST = LET, BEGIN, IF, WHILE, INPUT, PRINT, 
	 *                                                     NUM, ID, TRUE, FALSE, MINUS, NOT, LPAREN
	 *              | ε                            FOLLOW = END
	 */
	private List<Stmt> parseStmtList() {
		if (check(TokenType.LET, TokenType.BEGIN, TokenType.IF, TokenType.WHILE, TokenType.INPUT, 
				  TokenType.PRINT, TokenType.NUM, TokenType.ID, TokenType.TRUE, TokenType.FALSE, 
				  TokenType.MINUS, TokenType.NOT, TokenType.LPAREN)) {
			return parseStmts();
		} else {
			return new ArrayList<Stmt>();
		}
	}
	
	/*
	 * <Stmts> --> <Stmt> <StmtRest>               FIRST = LET, BEGIN, IF, WHILE, INPUT, PRINT, 
	 *                                                     NUM, ID, TRUE, FALSE, MINUS, NOT, LPAREN
	 */
	private List<Stmt> parseStmts() {
		Stmt s = parseStmt();
		return parseStmtRest(s);
	}
	
	/*
	 * <StmtRest> --> ; <Stmts>
	 *              | ε
	 */
	private List<Stmt> parseStmtRest(Stmt s) {
		if (check(TokenType.SEMI)) {
			match(TokenType.SEMI);
			List<Stmt> ss = parseStmts();
			ss.add(0, s);
			return ss;
		} else {
			List<Stmt> ss = new ArrayList<Stmt>();
			ss.add(s);
			return ss;
		}
	}

	/*
	 * <Expr> --> <SimpleExpr> <ExprRest>          FIRST = NUM, ID, TRUE, FALSE, MINUS, NOT, LPAREN
	 */
	private Expr parseExpr() {
		Expr left = parseSimpleExpr();
		return parseExprRest(left);
	}
	
	/*
	 * <ExprRest> --> <RelOp> <SimpleExpr>         FIRST = EQ, NE, LE, GE, LT, GT
	 *              | ε                            FOLLOW = THEN, DO, SEMI, COMMA
	 */
	private Expr parseExprRest(Expr left) {
		if (check(TokenType.EQ, TokenType.NE, TokenType.LE, TokenType.GE, TokenType.LT, TokenType.GT)) {
			Op2 op = parseRelOp();
			Expr right = parseSimpleExpr();
			return new BinOp(left, op, right);
		} else {
			return left;
		}
	}
	
	/*
	 * <SimpleExpr> --> <Term> <SERest>            FIRST = NUM, ID, TRUE, FALSE, MINUS, NOT, LPAREN
	 */
	// Iterative version
//	private Expr parseSimpleExpr() {
//		Expr leftT = parseTerm();
//		while(check(TokenType.PLUS, TokenType.MINUS, TokenType.OR)) {
//			Op2 op = parseAddOp();
//			Expr rightT = parseTerm();
//			leftT = new BinOp(leftT, op, rightT);
//		}
//		return leftT;
//	}
	
	// Recursive version
	private Expr parseSimpleExpr() {
		Expr left = parseTerm();
		return parseSERest(left);
	}
	
	/*
	 * <SERest> --> <AddOp> <SimpleExpr>           FIRST = PLUS, MINUS, OR
	 *            | ε                              FOLLOW = FIRST(MulOp) = STAR, DIV, MOD, AND
	 */
	private Expr parseSERest(Expr left) {
		if(check(TokenType.PLUS, TokenType.MINUS, TokenType.OR)) {
			Op2 op = parseAddOp();
			Expr right = parseTerm();
			return parseSERest(new BinOp(left, op, right));
		} else {
			return left;
		}
	}
	
	/*
	 * <RelOp> --> = =                             FIRST = EQ
	 *           | < <LTRest>                      FIRST = LT
	 *           | > <GTRest>                      FIRST = GT
	 *           ???? Scanner should be able to handle this ????
	 */
	private Op2 parseRelOp() {
		if (check(TokenType.EQ)) {
			match(TokenType.EQ);
			return Op2.EQ;
		} else if (check(TokenType.NE)) {
			match(TokenType.NE);
			return Op2.NE;
		} else if (check(TokenType.LE)) {
			match(TokenType.LE);
			return Op2.LE;
		} else if (check(TokenType.GE)) {
			match(TokenType.GE);
			return Op2.GE;
		} else if (check(TokenType.LT)) {
			match(TokenType.LT);
			return Op2.LT;
		} else if (check(TokenType.GT)) {
			match(TokenType.GT);
			return Op2.GT;
		} else {
			System.err.println("Invalid relational operator: " + current);
			System.exit(1);
		}
		return null; // unreachable
	}
	
	/*
	 * <AddOp> --> +                               FIRST = PLUS
	 *           | -                               FIRST = MINUS
	 *           | or                              FIRST = OR
	 */
	private Op2 parseAddOp() {
		if (check(TokenType.PLUS)) {
			match(TokenType.PLUS);
			return Op2.Plus;
		} else if (check(TokenType.MINUS)) {
			match(TokenType.MINUS);
			return Op2.Minus;
		} else if (check(TokenType.OR)) {
			match(TokenType.OR);
			return Op2.Or;
		} else {
			System.err.println("Invalid addition operator: " + current);
			System.exit(1);
		}
		return null; // unreachable
	}
	
	/*
	 * <MulOp> --> *                               FIRST = STAR
	 *           | div                             FIRST = DIV
	 *           | mod                             FIRST = MOD
	 *           | and                             FIRST = AND
	 */
	private Op2 parseMulOp() {
		if (check(TokenType.STAR)) {
			match(TokenType.STAR);
			return Op2.Times;
		} else if (check(TokenType.DIV)) {
			match(TokenType.DIV);
			return Op2.Div;
		} else if (check(TokenType.MOD)) {
			match(TokenType.MOD);
			return Op2.Mod;
		} else {
			System.err.println("Invalid multiplication operator: " + current);
			System.exit(1);
		}
		return null; // unreachable
	}
	
	/*
	 * <UnOp> --> -                                FIRST = MINUS
	 *          | not                              FIRST = NOT
	 */
	private Op1 parseUnOp() {
		if (check(TokenType.MINUS)) {
			match(TokenType.MINUS);
			return Op1.Neg;
		} else if (check(TokenType.NOT)) {
			match(TokenType.NOT);
			return Op1.Not;
		} else {
			System.err.println("Invalid unary operator: " + current);
			System.exit(1);
		}
		return null; // unreachable
	}
	
	/*
	 * <Term> --> <Factor> <TermRest>
	 */
	// Iterative version
//	private Expr parseTerm() {
//		Expr leftF = parseFactor();
//		while(check(TokenType.STAR, TokenType.DIV, TokenType.MOD, TokenType.AND)) {
//			Op2 op = parseMulOp();
//			Expr rightF = parseFactor();
//			leftF = new BinOp(leftF, op, rightF);
//		}
//		return leftF;
//	}
	
	// Recursive version
	private Expr parseTerm() {
		Expr left = parseFactor();
		return parseTermRest(left);
	}
	
	/*
	 * <TermRest> --> <MulOp> <Factor>             FIRST = STAR, DIV, MOD, AND
	 *              | ε
	 */
	private Expr parseTermRest(Expr left) {
		if (check(TokenType.STAR, TokenType.DIV, TokenType.MOD, TokenType.AND)) {
			Op2 op = parseMulOp();
			Expr right = parseFactor();
			return parseTermRest(new BinOp(left, op, right));
		} else {
			return left;
		}
	}
	
	/*
	 * <Factor> --> num                            FIRST = NUM
	 *            | id <FactorRest>                FIRST = ID
	 *            | true                           FIRST = TRUE
	 *            | false                          FIRST = FALSE
	 *            | <UnOp> <Factor>                FIRST = MINUS, NOT
	 *            | ( <Expr> )                     FIRST = LPAREN
	 */
	private Expr parseFactor() {
		if (check(TokenType.NUM)) {
			int value = Integer.parseInt(match(TokenType.NUM).lexeme);
			return new Num(value);
		} else if (check(TokenType.ID)) {
			String id = match(TokenType.ID).lexeme;
			return parseFactorRest(id);
		} else if (check(TokenType.TRUE)) {
			match(TokenType.TRUE);
			return new True();
		} else if (check(TokenType.FALSE)) {
			match(TokenType.FALSE);
			return new False();
		} else if (check(TokenType.MINUS, TokenType.NOT)) {
			Op1 op = parseUnOp();
			Expr expr = parseFactor();
			return new UnOp(op, expr);
		} else if (check(TokenType.LPAREN)) {
			match(TokenType.LPAREN);
			Expr expr = parseExpr();
			match(TokenType.RPAREN);
			return expr;
		} else {
			System.err.println("Invalid factor: " + current);
			System.exit(1);
		}
		return null; // unreachable
	}
	
	/*
	 * <FactorRest> --> ε                          FOLLOW = STAR, DIV, MOD, AND
	 *                | ( <ArgList> )              FIRST = LPAREN
	 */
	private Expr parseFactorRest(String id) {
		if (check(TokenType.LPAREN)) {
			match(TokenType.LPAREN);
			List<Expr> args = parseArgList();
			match(TokenType.RPAREN);
			return new Call(id, args);
		} else {
			return new Id(id);
		}
	}
	
	/*
	 * <ArgList> --> Args                          FIRST = NUM, ID, TRUE, FALSE, MINUS, NOT, LPAREN
	 *             | ε
	 */
	private List<Expr> parseArgList() {
		if (check(TokenType.NUM, TokenType.ID, TokenType.TRUE, TokenType.FALSE, TokenType.MINUS, TokenType.NOT, TokenType.LPAREN)) {
			return parseArgs();
		} else {
			return new ArrayList<Expr>();
		}
	}
	
	/*
	 * <Args> --> <Expr> <ArgsRest>
	 */
	private List<Expr> parseArgs() {
		Expr a = parseExpr();
		return parseArgsRest(a);
	}
	
	/*
	 * <ArgsRest> --> , <Args>
	 *              | <Expr>
	 */
	private List<Expr> parseArgsRest(Expr a) {
		if (check(TokenType.COMMA)) {
			match(TokenType.COMMA);
			List<Expr> as = parseArgs();
			as.add(0, a);
			return as;
		} else {
			List<Expr> args = new ArrayList<Expr>();
			args.add(a);
			return args;
		}
	}

	private Scanner scanner;
	private Token current;
}