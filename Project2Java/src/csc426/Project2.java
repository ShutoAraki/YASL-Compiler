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
	public static void main(String[] args) {
		
		try {
			// Scanner scanner = new Scanner(new BufferedReader(new InputStreamReader(System.in)));
			Scanner scanner = new Scanner(new FileReader("src/test_files/test1.txt"));
			Parser parser = new Parser(scanner);
	 		parser.runParser();
		} catch (Exception e) {
			System.err.println(e);
		}
	}
}