package main;

import parser.*;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;

import java.util.List;
import java.util.ArrayList;

public class Function implements Scope {
    public Type type;
    public String name;
    public List<Variable> parameters;
    public StatementBlock body;

    public Scope parentScope;
    public List<String> parameterNames;

    public Function(
        Type type, String name, List<Variable> parameters, StatementBlock body
    ) {
        this.type = type;
        this.name = name;
        this.parameters = parameters;
        this.body = body;
    }

    public static Function recognize(GrammarParser.FunctionDefinitionContext ctx) {
        Type type = Type.recognize(ctx.type());
        String name = ctx.name().getText();
        List parameters = new ArrayList<>();
        ctx.paramList().formalParameter().forEach(par -> 
            parameters.add(
                new Variable(Type.recognize(par.type()), par.name().getText())
        ));
        StatementBlock body = StatementBlock.recognize(ctx.blockStatement());
        return new Function(type, name, parameters, body);
    }

    public void assertObject() {
        if(type.type == Type.TypeType.OBJECT) {
            Recover.notImplemented();
        }
    }

    public void collectDeclarations(Scope parentScope) {
        this.parentScope = parentScope;
        parameterNames = new ArrayList<>();
        for(Variable v: parameters) {
            v.assertVoid();
            v.assertObject();
            v.assertDefineteness(this);
            parameterNames.add(v.name);
        }
        body.collectDeclarations(this);
    }

    @Override
    public boolean isDefinedVariableHere(String name) {
        return parameterNames.contains(name);
    }

    @Override
    public boolean isDefinedVariable(String name) {
        return isDefinedVariableHere(name);
    }

    @Override
    public boolean isDefinedFunction(String name) {
        return parentScope.isDefinedFunction(name);
    }

    @Override
    public boolean isDefinedClass(String name) {
        return parentScope.isDefinedClass(name);
    }

    @Override
    public boolean isDefinedSymbol(String name) {
        return parentScope.isDefinedSymbol(name);
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