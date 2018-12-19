/*
 * VYPa 2018 - VYPcode compiler.
 * Roman Andriushchenko (xandri03)
 */
package main;

import parser.*;
import org.antlr.v4.runtime.*;

import java.io.PrintStream;
import java.io.FileOutputStream;

/**
 * Program entry point.
 */
public class Main {
	public static void main(String[] args) {
        assert args.length == 2;

        // Load input
        CharStream input = null;
        try{
            input = new ANTLRFileStream(args[0]);
        } catch (java.io.IOException e) {
            Recover.internal("cannot open input file");
        }

        // Lexical analysis
        GrammarLexer lexer = new GrammarLexer(input);
        lexer.removeErrorListener(ConsoleErrorListener.INSTANCE);
        BaseErrorListener listenerLexical = new BaseErrorListener() {
            @Override
            public void syntaxError(
                Recognizer<?,?> recognizer, Object offendingSymbol,
                int line, int charPositionInLine, String msg,
                RecognitionException e
            ) {
                Recover.lexical("line " + line + ":" + charPositionInLine + " " + msg);
            }
        };
        lexer.addErrorListener(listenerLexical);
        lexer.getAllTokens();
        lexer.removeErrorListener(listenerLexical);
        lexer.reset();

        // Syntactic analysis
        GrammarParser parser = new GrammarParser(new CommonTokenStream(lexer));
        parser.removeErrorListener(ConsoleErrorListener.INSTANCE);
        BaseErrorListener listenerSyntactic = new BaseErrorListener() {
            @Override
            public void syntaxError(
                Recognizer<?,?> recognizer, Object offendingSymbol,
                int line, int charPositionInLine, String msg,
                RecognitionException e
            ) {
                Recover.syntactic("line " + line + ":" + charPositionInLine + " " + msg);
            }
        };
        parser.addErrorListener(listenerSyntactic);
        parser.program();
        parser.removeErrorListener(listenerSyntactic);
        parser.reset();

        // Parse the tree and construct the program
        Program program = new Program(parser);
        
        // Produce output
        String output = program.code();
        try (PrintStream out = new PrintStream(new FileOutputStream(args[1]))) {
		    out.print(output);
		} catch(java.io.FileNotFoundException e) {
			Recover.internal("cannot open output file");
		}

		// Success
        Recover.success();
    }
}