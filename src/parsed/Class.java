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

    public Class(GrammarParser.ClassDefinitionContext ctx) {
        name = ctx.name().get(0).getText();
        baseName = ctx.name().get(1).getText();

        attributes = new ArrayList<>();
        ctx.variableDeclaration().forEach(def -> attributes.addAll(Variable.recognize(def)));
        
        methods = new ArrayList<>();
        ctx.functionDefinition().forEach(def -> methods.add(new Function(def)));
    }

    /*@Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(name + " : " + baseName + ". ");

        sb.append("Attributes: ");
        attributes.forEach(p -> sb.append(p + ", "));

        sb.append("Methods: ");
        methods.forEach(m -> sb.append(m + ", "));

        return sb.toString();
    }*/
}