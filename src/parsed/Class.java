package parsed;

import parser.*;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;

import java.util.List;
import java.util.ArrayList;

public class Class {
    public String name;
    public String baseName;
    public List<Variable> attributes;
    public List<Function> methods;

    public Class(String name, String baseName, List<Variable> attributes, List<Function> methods) {
        this.name = name;
        this.baseName = baseName;
        this.attributes = attributes;
        this.methods = methods;
    }

    public static Class recognize(GrammarParser.ClassDefinitionContext ctx) {
        String name = ctx.name().get(0).getText();
        String baseName = ctx.name().get(1).getText();

        List<Variable> attributes = new ArrayList<>();
        ctx.variableDeclaration().forEach(attributeDefinition ->
            attributes.addAll(Variable.recognize(attributeDefinition))
        );
        
        List<Function> methods = new ArrayList<>();
        ctx.functionDefinition().forEach(functionDefinition ->
            methods.add(Function.recognize(functionDefinition))
        );

        return new Class(name, baseName, attributes, methods);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(name + " : " + baseName + ". ");

        sb.append("Attributes: ");
        attributes.forEach(p -> sb.append(p + ", "));

        sb.append("Methods: ");
        methods.forEach(m -> sb.append(m + ", "));

        return sb.toString();
    }
}