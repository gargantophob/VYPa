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

 		String inputString ="2018-May-05 14:20:24 ERROR Bad thing happened\n";
 		LogLexer lexer = new LogLexer(CharStreams.fromString(inputString));
 		CommonTokenStream tokens = new CommonTokenStream(lexer);
		LogParser parser = new LogParser(tokens);
		
		ParseTreeWalker walker = new ParseTreeWalker();

		LogListener listener = new LogListener();
		ParseTree tree = parser.log();
		walker.walk(listener, tree);

		// LogEntry entry = listener.entries.get(0);

		System.out.println("Finished.");
	}
}