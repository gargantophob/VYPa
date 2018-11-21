package main;

import parser.*;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;

// import java.util.List;
// import java.util.ArrayList;

public class Program {
    
    public Program(GrammarParser parser) {
        
        // Collect class names
        SymbolTable.initialize();
        GrammarParser.ProgramContext program = parser.program();
        SymbolTable.collectClassNames(parsed.classes);

        classes.add(new Class((GrammarParser.ClassDefinitionContext) def));
        // functions.add(new Function((GrammarParser.FunctionDefinitionContext) def));

        /*SymbolTable.collectClassBases();
        SymbolTable.collectClassMembers();
        SymbolTable.collectFunctionHeaders(parsed.functions);
        SymbolTable.collectBodies();
        SymbolTable.inferType();
*/
    }
}

