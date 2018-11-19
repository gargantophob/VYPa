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
        ClassTable.collectDefinitions();

        // Collect global functions
        Scope scope = new Scope(null);
        scope.functionAllow();
        parsed.functions.forEach(f -> scope.functionRegister(new Function(f, scope)));

        // Implicit functions

        // Collect declarations of functions and classes
        /*for(Function f: functionList) {
            if(isDefinedSymbol(f.name)) {
                Recover.exit(3, "redefinition of symbol " + f.name);
            }
            functions.put(f.name, f);
        }*/
        // TODO classes

        // Forbid returning classes
        /*for(Function f: functions.values()) {
            f.assertObject();
        }*/

        // Collect declarations inside functions
        /*for(Function f: functions.values()) {
            f.assertDefineteness(this);
        }*/
	}
}

