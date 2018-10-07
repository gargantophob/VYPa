package main;

import parser.*;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;

public class Main {
	public static void main(String[] args) {
		System.out.println("Starting...");

/*		String javaClassContent = "public1 class SampleClass { void DoSomething(){} }";
		Java8Lexer lexer = new Java8Lexer(CharStreams.fromString(javaClassContent));
		CommonTokenStream tokens = new CommonTokenStream(lexer);
		Java8Parser parser = new Java8Parser(tokens);
		ParseTree tree = parser.compilationUnit();

		ParseTreeWalker walker = new ParseTreeWalker();
		UppercaseMethodListener listener= new UppercaseMethodListener();

		walker.walk(listener, tree);
 
 		for(String error: listener.errors) {
 			// System.out.println(error);
 		}*/

 		String inputString ="class Lol : Object {}";
 		GrammarLexer lexer = new GrammarLexer(CharStreams.fromString(inputString));
 		CommonTokenStream tokens = new CommonTokenStream(lexer);
		GrammarParser parser = new GrammarParser(tokens);
		
		ParseTreeWalker walker = new ParseTreeWalker();

		GrammarListener listener = new GrammarListener();
		ParseTree tree = parser.program();
		walker.walk(listener, tree);

		System.out.println("Finished.");

		System.exit(42);
	}
}