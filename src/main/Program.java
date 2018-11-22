package main;

import parser.GrammarParser;

import java.util.List;
import java.util.ArrayList;

public class Program {
    
    public Program(GrammarParser parser) {
        
        // Initiate AST construction
        SymbolTable.initialize();
        GrammarParser.ProgramContext program = parser.program();

        /*****************************/
        
        // Assemble Object class
        SymbolTable.registerClass(new Class());

        // Collect class names
        program.classDefinition().forEach(ctx -> SymbolTable.registerClass(new Class(ctx)));
        
        // Establich class hierarchy
        SymbolTable.classes().forEach(c -> c.collectBase());
        SymbolTable.classes().forEach(c -> c.checkBase());

        /*****************************/

        // Assemble built-in functions
        SymbolTable.registerFunction(new Function(Type.VOID, "print", null, null));
        SymbolTable.registerFunction(new Function(Type.INT, "readInt", null, null));
        SymbolTable.registerFunction(new Function(Type.STRING, "readString", null, null));
        
        List<Variable> lengthArguments = new ArrayList<>();
        lengthArguments.add(new Variable(Type.STRING, "s"));
        SymbolTable.registerFunction(new Function(Type.INT, "length", lengthArguments, null));

        List<Variable> subStrArguments = new ArrayList<>();
        subStrArguments.add(new Variable(Type.STRING, "s"));
        subStrArguments.add(new Variable(Type.INT, "i"));
        subStrArguments.add(new Variable(Type.INT, "n"));
        SymbolTable.registerFunction(new Function(Type.STRING, "subStr", subStrArguments, null));

        // Collect global function headers
        program.functionDefinition().forEach(ctx ->
            SymbolTable.registerFunction(new Function(ctx, null))
        );

        // Look up "main" and check its signature
        if(!SymbolTable.functions.lookUp("main").signatureMatch(Type.VOID, new ArrayList<>())) {
            Recover.type("bad signature of function main");
        }

        /*****************************/

        // Collect class members
        SymbolTable.classes().forEach(c -> c.collectMembers());
        SymbolTable.classes().forEach(c -> c.checkMembers());

        // Process procedure bodies
        SymbolTable.functions().forEach(f -> f.collectBody());
        SymbolTable.classes().forEach(c -> c.collectBody());

        /*****************************/        
    }
}

