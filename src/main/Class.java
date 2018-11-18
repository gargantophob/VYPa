package main;

import parser.*;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;

import java.util.List;
import java.util.ArrayList;

public class Class {
    public String name;
    public String baseClass;
    public List<Variable> attributes;
    public List<Function> methods;

    public Class(GrammarParser.ClassDefinitionContext ctx) {
        name = ctx.name().get(0).getText();
        baseClass = ctx.name().get(1).getText();

        attributes = new ArrayList<>();
        ctx.variableDeclaration().forEach(attributeDefinition ->
            Statement.recognize(attributeDefinition).forEach(st -> attributes.add(st.variable))
        );
        
        methods = new ArrayList<>();
        ctx.functionDefinition().forEach(functionDefinition ->
            methods.add(Function.recognize(functionDefinition))
        );
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(name + " : " + baseClass + ". ");

        sb.append("Attributes: ");
        attributes.forEach(p -> sb.append(p + ", "));

        sb.append("Methods: ");
        methods.forEach(m -> sb.append(m + ", "));

        return sb.toString();
    }
}