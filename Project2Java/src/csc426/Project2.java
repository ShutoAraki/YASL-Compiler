package csc426;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.FileReader;

/**
 * Main class for Project 2
 * 
 * @author ShutoAraki
 */
public class Project2 {
	public static void main(String[] args) throws IOException {
		Scanner scanner = new Scanner(new BufferedReader(new InputStreamReader(System.in)));
		Parser parser = new Parser(scanner);
 		parser.parseProgram();
	}
}