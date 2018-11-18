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
        /*FunctionTable.initialize();
        parsed.functions.forEach(f -> FunctionTable.global.register(new Function(f, null)));*/

        // Collect members of classes
        // ClassTable.collectDefinitions();

        // Function functionReference;
        /*functionReference = toStringCreate();
        functions.put(functionReference.name, functionReference);
        functionReference = getClassCreate();
        functions.put(functionReference.name, functionReference);*/

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

