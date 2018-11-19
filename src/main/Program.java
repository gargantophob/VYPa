package main;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

public class Program {
    public Program(parsed.Program parsed) {
    	SymbolTable.initialize();
        SymbolTable.collectClassNames(parsed.classes);
        SymbolTable.collectClassBases();
        SymbolTable.collectClassMembers();
        SymbolTable.collectFunctionHeaders(parsed.functions);
        SymbolTable.collectBodies();
	}
}

