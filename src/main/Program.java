package main;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

public class Program {
    public Program(parsed.Program parsed) {
    	// System.out.println(parsed.toString());

        // Collect classes
        ClassTable.initialize();
        parsed.classes.forEach(c -> ClassTable.register(new Class(c)));
        ClassTable.lookUpBase();

        // Collect global functions
        Scope scope = new Scope(null);
        scope.functionAllow();
        parsed.functions.forEach(f -> scope.functionRegister(new Function(f, scope)));

        // Process classes & functions
        ClassTable.processBody();
        scope.functionProcessBody();
	}
}

