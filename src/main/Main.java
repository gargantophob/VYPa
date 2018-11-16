package main;

import parser.*;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;

import java.io.FileOutputStream;
import java.io.PrintStream;

public class Main {
	
	public static void checkLexicalErrors(Lexer lexer) {
        BaseErrorListener listener = new BaseErrorListener() {
            @Override
            public void syntaxError(
                Recognizer<?,?> recognizer, Object offendingSymbol,
                int line, int charPositionInLine, String msg,
                RecognitionException e
            ) {
                Recover.exit(43, "line " + line + ":" + charPositionInLine + " " + msg);
            }
        };
        
        lexer.addErrorListener(listener);
        lexer.getAllTokens();
        lexer.removeErrorListener(listener);
        lexer.reset();
    }

    public static void checkSyntacticErrors(GrammarParser parser) {
        BaseErrorListener listener = new BaseErrorListener() {
            @Override
            public void syntaxError(
                Recognizer<?,?> recognizer, Object offendingSymbol,
                int line, int charPositionInLine, String msg,
                RecognitionException e
            ) {
                Recover.exit(43, "line " + line + ":" + charPositionInLine + " " + msg);
            }
        };

        parser.addErrorListener(listener);
        parser.program();
        parser.removeErrorListener(listener);
        parser.reset();
    }

    public static void main(String[] args) {
        assert args.length == 2;

        // Load input
        CharStream input = null;
        try{
            input = new ANTLRFileStream(args[0]);
        } catch (java.io.IOException e) {
            Recover.exit(42, "cannot read input file");
        }

        // Lexical analysis
        GrammarLexer lexer = new GrammarLexer(input);
        lexer.removeErrorListener(ConsoleErrorListener.INSTANCE);
        checkLexicalErrors(lexer);

        // Syntactic analysis
        GrammarParser parser = new GrammarParser(new CommonTokenStream(lexer));
        parser.removeErrorListener(ConsoleErrorListener.INSTANCE);
        checkSyntacticErrors(parser);

        // Translate
        Program program = new Program(parser);
        String output = program.translated();

        // Store output
        try (PrintStream out = new PrintStream(new FileOutputStream(args[1]))) {
		    out.print(output);
		} catch(java.io.FileNotFoundException e) {
			Recover.exit(42, "cannot open output file");
		}

		// Success
        Recover.exit();
    }
}