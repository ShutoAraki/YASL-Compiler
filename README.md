# YASL Compiler
This repository is created for CSC426: Compilers class at DePauw University taught by Dr. Brian Howard in Fall 2018.
YASL (Yet Another Simple Language), a simplified programming language created for pedagogical purposes, will be compiled. The textbook used in this class is available [here](http://hjemmesider.diku.dk/~torbenm/Basics/basics_lulu2.pdf) for free. This documentation will describe the steps toward building a compiler from scratch.

<b>A subset of YASL has the following grammar rules (for now).</b>
```
<Program> -->
  PROGRAM ID SEMI <Block> PERIOD

<Block> -->
  <ConstDecls> BEGIN <Stmts> END

<ConstDecls> -->
  <ConstDecl> <ConstDecls>
|

<ConstDecl> --> VAL ID ASSIGN NUM SEMI

<Stmts> -->
  <Stmt> SEMI <Stmts>
| <Stmt>

<Stmt> -->
  PRINT <Expr>

<Expr> -->
  <Expr> PLUS <Term>
| <Expr> MINUS <Term>
| <Term>

<Term> -->
  <Term> STAR <Factor>
| <Term> DIV <Factor>
| <Term> MOD <Factor>
| <Factor>

<Factor> -->
  NUM
| ID
```

## Lexical Analysis (Project 1)
"This is the initial part of reading and analysing the program text: The text is read and divided into tokens, each of which corresponds to a symbol in the programming language, e.g., a variable name, keyword or number" (Ægidius Mogensen, 2).

Example input in YASL:
```
program demo1;
/* Declare some constants */
val x = 6;
val y = 7;
begin
  print x * y // should print 42
end.
```
Expected output:
```
PROGRAM 1:1
ID demo1 1:9
SEMI 1:14
VAL 3:1
ID x 3:5
ASSIGN 3:7
NUM 6 3:9
SEMI 3:10
VAL 4:1
ID y 4:5
ASSIGN 4:7
NUM 7 4:9
SEMI 4:10
BEGIN 5:1
PRINT 6:3
ID x 6:9
STAR 6:11
ID y 6:13
END 7:1
PERIOD 7:4
EOF 8:1
```

For example, the following Deterministic Finite State Transducer ([FST](http://web.cs.ucdavis.edu/~rogaway/classes/120/spring13/eric-transducers.pdf)) diagram helps with coding the `NUM` token type.

![](https://github.com/ShutoAraki/YASL-Compiler/blob/master/doc/images/FST_num.png)

## Syntax Analysis -Part 1- (Project 2)
"This phase takes the list of tokens produced by the lexical analysis and arranges these in a tree-structure (called the *syntax tree*) that reflects the structure of the program. This phase is often called *parsing*." (Ægidius Mogensen, 2).

Example input in YASL:
```
program demo2;
/* Declare some constants */
val x = 6;
val y = 7;
begin
  print x * y; // should print 42
  print 12 div y * y + 12 mod y - 12 // should print 0
end.
```
Expected output:
```
6
7
*
PRINT
12
7
DIV
7
*
12
7
MOD
+
12
-
PRINT
```
`Porject2Java/src/csc426/Project2.java` file recognizes val declarations and print statements using the operator stack method based on precedence on different operators. It not only ignores everything else but also makes a bold assumption that the input is going to be always grammatical on the subset of YASL grammar specified above.

## Syntax Analysis -Part 2- (Project 3)
In this stage, the recursive descent parser is constructed and the output will represent the abstract syntax tree of the input. 

Example input that reflects most of the YASL grammar:
```
program demo3;
  /* Declare some constants */
  val x = 6;
  val y = 7;
  val z = -120;
  /* Declare some variables */
  var a: int;
  var b: bool;
  var c1: int; var c2: int;
  /* Declare some functions */
  fun foo(): void;
    val a = 42; // local to foo
    var b: bool;
  begin
    c1 = x * y;
    b = a == c1;
    if b then
      if b or not b then
        print "Hooray!"
    else
      print “Boo!”
  end;
  fun bar(n: int, b: bool): int;
    fun fact(n: int): int;
      if n > 0 then
      begin
        fact(n - 1) * n
      end
      else
        (y + x) mod 2;
  begin
    while b do
    begin
      b = not b;
      foo()
    end;
    fact(n)
  end;
begin
  input "Enter a number", a;
  c2 = bar(a, x <> y);
  print "The answer is """, c2--z+c1, """!";
  input "Hit any key to end"
end.
```

The output should be something like the following:
```
Program demo3
  Block
    Val x = 6
    Val y = 7
    Val z = -120
    Var a : Int
    Var b : Bool
    Var c1 : Int
    Var c2 : Int
    Fun foo : Void
      Block
        Val a = 42
        Var b : Bool
        Sequence
          Assign c1
            BinOp Times
              Id x
              Id y
          Assign b
            BinOp EQ
              Id a
              Id c1
          IfThen
            Id b
            IfThenElse
              BinOp Or
                Id b
                UnOp Not
                  Id b
              Print
                StringItem "Hooray!"
              Print
                StringItem “Boo!”
    Fun bar : Int
      Val n : Int
      Val b : Bool
      Block
        Fun fact : Int
          Val n : Int
          Block
            IfThenElse
              BinOp GT
                Id n
                Num 0
              Sequence
                ExprStmt
                  BinOp Times
                    Call fact
                      BinOp Minus
                        Id n
                        Num 1
                    Id n
              ExprStmt
                BinOp Mod
                  BinOp Plus
                    Id y
                    Id x
                  Num 2
        Sequence
          While
            Id b
            Sequence
              Assign b
                UnOp Not
                  Id b
              ExprStmt
                Call foo
          ExprStmt
            Call fact
              Id n
    Sequence
      Input2 "Enter a number", a
      Assign c2
        Call bar
          Id a
          BinOp NE
            Id x
            Id y
      Print
        StringItem "The answer is ""
        ExprItem
          BinOp Plus
            BinOp Minus
              Id c2
              UnOp Neg
                Id z
            Id c1
        StringItem ""!"
      Input "Hit any key to end"
```

### The full YASL grammar
```
<Program> -->
  program ID ; <Block> .

<Block> -->
  <ValDecls> <VarDecls> <FunDecls> <Stmt>

<ValDecls> -->
  <ValDecl> <ValDecls>
| ε

<ValDecl> -->
  val ID = <Sign> NUM ;

<Sign> -->
  -
| ε

<VarDecls> -->
  <VarDecl> <VarDecls>
| ε

<VarDecl> -->
  var ID : <Type> ;

<Type> -->
  int
| bool
| void

<FunDecls> -->
  <FunDecl> <FunDecls>
| ε

<FunDecl> -->
  fun ID ( <ParamList> ) : <Type> ; <Block> ;

<ParamList> -->
  <Params>
| ε

<Params> -->
  <Param> , <Params>
| <Param>

<Param> -->
  ID : <Type>

<StmtList> —>
  <Stmts>
| ε

<Stmts> -->
  <Stmt> ; <Stmts>
| <Stmt>

<Stmt> -->
  ID = <Expr>
| begin <StmtList> end
| if <Expr> then <Stmt>
| if <Expr> then <Stmt> else <Stmt>
| while <Expr> do <Stmt>
| input STRING
| input STRING , ID
| print <Items>
| <Expr>

<Items> -->
  <Item> , <Items>
| <Item>

<Item> -->
  <Expr>
| STRING

<Expr> -->
  <SimpleExpr> <RelOp> <SimpleExpr>
| <SimpleExpr>

<RelOp> -->
  == | <> | <= | >= | < | >

<SimpleExpr> -->
  <SimpleExpr> <AddOp> <Term>
| <Term>

<AddOp> -->
  + | - | or

<Term> -->
  <Term> <MulOp> <Factor>
| <Factor>

<MulOp> -->
  * | div | mod | and

<Factor> -->
  NUM
| ID
| ID ( <ArgList> )
| true
| false
| <UnOp> <Factor>
| ( <Expr> )

<UnOp> -->
  - | not

<ArgList> -->
  <Args>
| ε

<Args> -->
  <Expr> , <Args>
| <Expr>
```
