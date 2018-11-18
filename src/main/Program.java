package main;

import parser.*;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

public class Program implements Scope {
    Map<String, Function> functions;
    Map<String, Class> classes;

    public Program(GrammarParser parser) {
        // Collect function & classes
        List<Function> functionList = new ArrayList<>();
    	List<Class> classList = new ArrayList<>();

        RuleContext program = parser.program();
        for(int i = 0; i < program.getChildCount(); i++) {
        	RuleContext definition = (RuleContext) program.getChild(i);
        	if(definition.getRuleIndex() == GrammarParser.RULE_classDefinition) {
        		classList.add(new Class((GrammarParser.ClassDefinitionContext) definition));
        	} else {
        		functionList.add(Function.recognize((GrammarParser.FunctionDefinitionContext) definition));
        	}
        }

        /*System.out.println("Functions:");
        for(Function f: functionList) {
            System.out.println(f);
        }*/

        // Implicit 'Object' class

        // Implicit functions

        // Collect declarations of functions and classes

        // Create functions table
        functions = new HashMap<>();
        classes = new HashMap<>();
        for(Function f: functionList) {
            if(isDefinedSymbol(f.name)) {
                Recover.exit(3, "redefinition of symbol " + f.name);
            }
            functions.put(f.name, f);
        }
        // TODO classes

        // Forbid returning classes
        for(Function f: functions.values()) {
            f.assertObject();
        }

        // Collect declarations inside functions
        for(Function f: functions.values()) {
            f.collectDeclarations(this);
        }
	}

    @Override
    public boolean isDefinedVariableHere(String name) {
        return false;
    }

    @Override
    public boolean isDefinedVariable(String name) {
        return false;
    }

    @Override
    public boolean isDefinedFunction(String name) {
        return functions.containsKey(name);
    }

    @Override
    public boolean isDefinedClass(String name) {
        return classes.containsKey(name);
    }

    @Override
    public boolean isDefinedSymbol(String name) {
        return isDefinedFunction(name) || isDefinedClass(name);
    }
    
}

