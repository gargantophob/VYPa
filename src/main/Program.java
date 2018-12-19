/*
 * VYPa 2018 - VYPcode compiler.
 * Roman Andriushchenko (xandri03)
 */

package main;

import parser.GrammarParser;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.Set;
import java.util.HashSet;
import java.util.stream.Collectors;

/**
 * Syntax-directed parser.
 */
public class Program {
	
    public Program(GrammarParser parser) {
        // Initiate AST construction
        GrammarParser.ProgramContext program = parser.program();

        // Allocate table of classes
        Class.table = new SymbolTable<>();

        // Register built-in Object class
        Class classObject = new Class();
        classObject.name = "Object";
        Class.table.register(classObject);
        
        // Collect definitions of classes
        program.classDefinition().forEach(
        	ctx -> Class.table.register(Class.recognize(ctx))
        );

        // Establish class hierarchy
        Class.table.values().forEach(c -> c.collectBase());
        Class.table.values().forEach(c -> c.checkBase());

        // Allocate table of global functions
        Function.table = new SymbolTable<>();

        // Register built-in functions
        Function function;

        function = new Function();
        function.type = Type.VOID;
        function.name = "print";
        function.parameters = new ArrayList<>();
        Function.table.register(function);

        function = new Function();
        function.type = Type.INT;
        function.name = "readInt";
        function.parameters = new ArrayList<>();
        Function.table.register(function);

        function = new Function();
        function.type = Type.STRING;
        function.name = "readString";
        function.parameters = new ArrayList<>();
        Function.table.register(function);

        function = new Function();
        function.type = Type.INT;
        function.name = "length";
        function.parameters = new ArrayList<>();
        function.parameters.add(new Variable(Type.STRING, "s"));
        Function.table.register(function);

        function = new Function();
        function.type = Type.STRING;
        function.name = "subStr";
        function.parameters = new ArrayList<>();
        function.parameters.add(new Variable(Type.STRING, "s"));
        function.parameters.add(new Variable(Type.INT, "i"));
        function.parameters.add(new Variable(Type.INT, "n"));
        Function.table.register(function);

        // Collect global function headers
        program.functionDefinition().forEach(ctx -> {
        	Function.table.register(Function.recognize(ctx));
		});

        // Process all global functions definitions
        Function.table.values().forEach(f -> f.initialize());

        // Look up "main" and check its signature
        function = Function.table.lookUp("main");
        if(function.type != Type.VOID || !function.signatureMatchExact(new ArrayList<>())) {
            Recover.type("bad signature of function main");
        }

        // Collect class members
        Class.table.values().forEach(c -> c.collectMembers());

        // Collect procedure bodies
        Function.table.values().forEach(f -> f.collectBody());
        Class.table.values().forEach(c -> c.collectBody());

        // Indexate functions
        int index = 0;
        for(Function f: Function.table.values()) {
            f.indexate(index++);
        }
        
        // Indexate classes
        index = 0;
        List<Class> list = new ArrayList<>();
        Class.table.values().forEach(c -> c.order(list));
        for(Class c: list) {
            c.indexate(index++);
        }
    }

    /* *** Code generation ***  */
    public String code() {
        
    }
}

