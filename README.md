# YASL Compiler
This repository is created for CSC426: Compilers class at DePauw University taught by Dr. Brian Howard in Fall 2018.
YASL (Yet Another Simple Language), a simplified programming language created for pedagogical purposes, will be compiled. The textbook used in this class is available [here](http://hjemmesider.diku.dk/~torbenm/Basics/basics_lulu2.pdf) for free. This documentation will describe the steps toward building a compiler from scratch.

## Lexical Analysis (Project 1)
"This is the initial part of reading and analysing the program text: The text is read and divided into tokens, each of which corresponds to a symbol in the programming language, e.g., a variable name, keyword or number" (Ægidius Mogensen, 2).

Example input in YASL
```
program demo1;
/* Declare some constants */
val x = 6;
val y = 7;
begin
  print x * y // should print 42
end.
```
Expected output
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

## Syntax Analysis (Project 2)
"This phase takes the list of tokens produced by the lexical analysis and arranges these in a tree-structure (called the *syntax tree*) that reflects the structure of the program. This phase is often called *parsing*." (Ægidius Mogensen, 2).

Example input in YASL
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
Expected output
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
