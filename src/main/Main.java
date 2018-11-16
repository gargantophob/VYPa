package main;

import parser.*;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;

import java.util.List;
import java.util.ArrayList;

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
            exit(42, "cannot read input file");
        }
        return null;
    }

    public static void checkLexicalErrors(Lexer lexer) {
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
            token = lexer.nextToken();
        }

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
                exit(43, "line " + line + ":" + charPositionInLine + " " + msg);
            }
        };

        parser.addErrorListener(listener);
        parser.program();
        parser.removeErrorListener(listener);
        parser.reset();
    }










    public static enum Type {
    	VOID, INT, STRING, CLASS;

    	public static Type recognize(GrammarParser.TypeContext ctx) {
    		switch(ctx.getStart().getType()) {
    			case GrammarParser.Void: return Type.VOID;
    			case GrammarParser.Int: return Type.INT;
    			case GrammarParser.String: return Type.STRING;
    			default: return Type.CLASS;
    		}
    	}
    }

    public static class DataType {
    	private Type type;
    	private String name;

    	public DataType(GrammarParser.TypeContext ctx) {
    		this.type = Type.recognize(ctx);
    		if(this.type == Type.CLASS) {
    			this.name = ctx.getText();
    		}
    	}

    	@Override
    	public String toString() {
    		return type == Type.CLASS ? name : type.toString();
    	}
    }

    static class Variable {
    	private DataType type;
    	private String name;
    	
    	public Variable(DataType type, String name) {
    		this.type = type;
    		this.name = name;
    	}

    	@Override
    	public String toString() {
    		return type + " " + name;
    	}
    }

    static class Function {
    	private Type type;
    	private String name;
    	private List<Variable> parameters;
    	private List<GrammarParser.StatementContext> commands;

    	public Function(GrammarParser.FunctionDefinitionContext ctx) {
    		this.type = Type.recognize(ctx.type());
    		this.name = ctx.getChild(1).getText();
    		this.parameters = new ArrayList<>();
    		ctx.paramList().formalParameter().forEach(par -> 
    			Function.this.parameters.add(new Variable(new DataType(par.type()), par.name().getText()))
    		);
    		this.commands = new ArrayList<>();
    		ctx.blockStatement().statement().forEach(st -> Function.this.commands.add(st));
    	}

    	@Override
    	public String toString() {
    		StringBuilder sb = new StringBuilder();
    		sb.append(type + " " + name + " (");
    		parameters.forEach(p -> sb.append(p + ", "));
    		sb.append(") { " + commands.size() + " commands }");
    		return sb.toString();
    	}
    }

    static class Class {
    	String name;
    	String base;
    	List<Variable> attributes;
    	List<Function> methods;

    	public Class(GrammarParser.ClassDefinitionContext ctx) {
    		this.name = ctx.getChild(1).getText();
    		this.base = ctx.getChild(3).getText();

    		this.attributes = new ArrayList<>();
    		ctx.variableDefinition().forEach(attributeDefinition -> {
    			DataType type = new DataType(attributeDefinition.type());
    			attributeDefinition.name().forEach(name -> {
    				this.attributes.add(new Variable(type, name.getText()));
    			});
    		});
    		
    		this.methods = new ArrayList<>();
    		ctx.functionDefinition().forEach(functionDefinition -> {
    			this.methods.add(new Function(functionDefinition));
    		});
    	}

    	@Override
    	public String toString() {
    		StringBuilder sb = new StringBuilder();
    		sb.append(name + " : " + base);

    		sb.append("Attributes: ");
    		attributes.forEach(p -> sb.append(p + ", "));

    		sb.append("Methods: ");
    		methods.forEach(m -> sb.append(m + ", "));

    		return sb.toString();
    	}
    }

    public static void main(String[] args) {
        assert args.length == 2;

        String inputPath = args[0];
        String outputPath = args[1];
        
        // Load input
        CharStream input = loadFile(inputPath);
        if(input == null) {
            
        }

        // Lexical analysis
        GrammarLexer lexer = new GrammarLexer(input);
        lexer.removeErrorListener(ConsoleErrorListener.INSTANCE);   // mute
        checkLexicalErrors(lexer);

        // Syntactic analysis
        GrammarParser parser = new GrammarParser(new CommonTokenStream(lexer));
        parser.removeErrorListener(ConsoleErrorListener.INSTANCE);   // mute
        checkSyntacticErrors(parser);

        // Collect function & classes 
    	List<Function> functions = new ArrayList<>();
        List<Class> classes = new ArrayList<>();
    	parser.program().functionDefinition().forEach(ctx -> functions.add(new Function(ctx)));
    	parser.reset();
    	parser.program().classDefinition().forEach(ctx -> classes.add(new Class(ctx)));
    	parser.reset();

    	// Show
        System.out.println("Classes:");
        for(Class c: classes) {
        	System.out.println(c);
        }

        System.out.println("Functions:");
        for(Function f: functions) {
        	System.out.println(f);
        }
        
        // Write to output
        
        System.out.println("Finished");

        exit();
    }
}