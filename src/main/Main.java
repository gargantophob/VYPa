package main;

import parser.*;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;

public class Main {
    
    public static void exit() {
        System.exit(0);
    }
    
    public static void exit(int code, String msg) {
        System.err.println(msg);
        System.exit(code);
    }

    public static CharStream loadFile(String path) {
        assert path != null;
        try{
            return (CharStream) new ANTLRFileStream(path);
        } catch (java.io.IOException e) {
            return null;
        }
    }

    public static void checkLexicalErrors(Lexer lexer) {
        assert lexer != null;

        lexer.reset();
        BaseErrorListener listener = new BaseErrorListener() {
            @Override
            public void syntaxError(
                Recognizer<?,?> recognizer, Object offendingSymbol,
                int line, int charPositionInLine, String msg,
                RecognitionException e
            ) {
                exit(43, "line " + line + ":" + charPositionInLine + " " + msg);
            }
        };
        lexer.addErrorListener(listener);

        Token token = lexer.nextToken();
        while (token.getType() != GrammarLexer.EOF) {
            // System.out.println(token.getText());
            token = lexer.nextToken();
        }

        lexer.removeErrorListener(listener);
        lexer.reset();
    }

    public static void checkSyntacticErrors(GrammarParser parser) {
        parser.reset();

        BaseErrorListener listener = new BaseErrorListener() {
            @Override
            public void syntaxError(
                Recognizer<?,?> recognizer, Object offendingSymbol,
                int line, int charPositionInLine, String msg,
                RecognitionException e
            ) {
                exit(43, "line " + line + ":" + charPositionInLine + " " + msg);
            }
        };

        parser.addErrorListener(listener);

        parser.program();

        parser.removeErrorListener(listener);
        parser.reset();
    }
    
    public static void main(String[] args) {
        assert args.length == 2;

        String inputPath = args[0];
        String outputPath = args[1];
        
        // Load input
        CharStream input = loadFile(inputPath);
        if(input == null) {
            exit(42, "cannot read input file");
        }

        // Lexical analysis
        GrammarLexer lexer = new GrammarLexer(input);
        lexer.removeErrorListener(ConsoleErrorListener.INSTANCE);   // mute
        checkLexicalErrors(lexer);

        // Syntactic analysis
        GrammarParser parser = new GrammarParser(new CommonTokenStream(lexer));
        parser.removeErrorListener(ConsoleErrorListener.INSTANCE);   // mute
        checkSyntacticErrors(parser);

        // ParseTreeWalker walker = new ParseTreeWalker();
        // GrammarListener listener = new GrammarListener();
        // ParseTree tree = parser.program();
        // walker.walk(listener, tree);
        
        // Write to output
        
        System.out.println("Finished");

        exit();
    }
}