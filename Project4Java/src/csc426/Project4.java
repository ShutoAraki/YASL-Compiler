package csc426;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.FileReader;

import csc426.ast.Program;
import csc426.syntax.*;

/**
 * Main class for Project 3
 * 	The main method displays the abstract syntax tree (AST)
 *  of the input with proper indentations.
 * 
 * @author ShutoAraki
 */
public class Project4 {
	
	public static void main(String[] args) throws IOException {
		
//		if (args.length != 1) {
//			System.out.println("Type in the file name as a command line argument.");
//		} else {
//			String filename = args[0];
			
//			Scanner scanner = new Scanner(new FileReader("src/test_files/" + filename));
			Scanner scanner = new Scanner(new FileReader("src/test_files/test2.txt"));
			Parser parser = new Parser(scanner);
			// This Program object is the root of the AST
			Program program = parser.parseProgram();
			program.interpret();
//			program.display("");
	 		scanner.close();
//		}
	}
}