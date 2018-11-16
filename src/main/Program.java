package main;

import parser.*;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;

import java.util.List;
import java.util.ArrayList;

public class Program {
	private List<Function> functions;

    private List<Class> classes;

    /**************************************************************************/
    
    public static class DataType {
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

    static class Statement {
    	static enum Type {
    		DEFINITION, ASSIGNMENT, CONDITIONAL, ITERATION, CALL, RETURN;
    	}

    	private Type type;
    	private RuleContext context;

    	public Statement(GrammarParser.StatementContext ctx) {
    		this.context = (RuleContext) ctx.getChild(0);
    		switch(context.getRuleIndex()) {
    			case GrammarParser.RULE_variableDeclaration: type = Type.DEFINITION; break;
    			case GrammarParser.RULE_assignment: type = Type.ASSIGNMENT; break;
    			case GrammarParser.RULE_conditional: type = Type.CONDITIONAL; break;
    			case GrammarParser.RULE_iteration: type = Type.ITERATION; break;
    			case GrammarParser.RULE_functionCall: type = Type.CALL; break;
    			default: this.type = Type.RETURN;
    		}
    	}

    	@Override
    	public String toString() {
    		return type + "";
    	}
    }

    static class StatementList {
    	private List<Statement> list;

    	public StatementList(GrammarParser.BlockStatementContext ctx) {
    		list = new ArrayList<>();
    		ctx.statement().forEach(st -> list.add(new Statement(st)));
    	}

    	@Override
    	public String toString() {
    		StringBuilder sb = new StringBuilder();
    		list.forEach(st -> sb.append(st + "; "));
    		return sb.toString();
    	}
    }

    static class Function {
    	private DataType type;
    	private String name;
    	private List<Variable> parameters;
    	private StatementList body;

    	public Function(GrammarParser.FunctionDefinitionContext ctx) {
    		this.type = new DataType(ctx.type());
    		this.name = ctx.getChild(1).getText();
    		this.parameters = new ArrayList<>();
    		ctx.paramList().formalParameter().forEach(par -> 
    			Function.this.parameters.add(new Variable(new DataType(par.type()), par.name().getText()))
    		);
    		this.body = new StatementList(ctx.blockStatement());
    	}

    	@Override
    	public String toString() {
    		StringBuilder sb = new StringBuilder();
    		sb.append(type + " " + name + " (");
    		parameters.forEach(p -> sb.append(p + ", "));
    		sb.append(") { " + body.toString() + "}");
    		return sb.toString();
    	}
    }

    private class Class {
    	String name;
    	String baseClass;
    	List<Variable> attributes;
    	List<Function> methods;

    	public Class(GrammarParser.ClassDefinitionContext ctx) {
    		this.name = ctx.getChild(1).getText();
    		this.base = ctx.getChild(3).getText();

    		this.attributes = new ArrayList<>();
    		ctx.variableDeclaration().forEach(attributeDefinition -> {
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

    /**************************************************************************/

	public Program(GrammarParser parser) {
        // Collect function & classes
    	functions = new ArrayList<>();
        classes = new ArrayList<>();
        RuleContext p = parser.program();
        for(int i = 0; i < p.getChildCount(); i++) {
        	RuleContext definition = (RuleContext) p.getChild(i);
        	if(definition.getRuleIndex() == GrammarParser.RULE_classDefinition) {
        		classes.add(new Class((GrammarParser.ClassDefinitionContext) definition));
        	} else {
        		functions.add(new Function((GrammarParser.FunctionDefinitionContext) definition));
        	}
        }
    	
    	// Show
        System.out.println("Classes:");
        for(Class c: classes) {
        	System.out.println(c);
        }

        System.out.println("Functions:");
        for(Function f: functions) {
        	System.out.println(f);
        }
	}

	public String translated() {
		return "TODO";
	}
}
