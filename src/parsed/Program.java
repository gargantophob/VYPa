package parsed;

import parser.*;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;

import java.util.List;
import java.util.ArrayList;

public class Program {
    
    public List<Function> functions;
    public List<Class> classes;

    public Program(GrammarParser parser) {
        functions = new ArrayList<>();
    	classes = new ArrayList<>();

        RuleContext program = parser.program();
        for(int i = 0; i < program.getChildCount(); i++) {
        	RuleContext def = (RuleContext) program.getChild(i);
        	if(def.getRuleIndex() == GrammarParser.RULE_classDefinition) {
        		classes.add(new Class((GrammarParser.ClassDefinitionContext) def));
        	} else {
        		functions.add(new Function((GrammarParser.FunctionDefinitionContext) def));
        	}
        }
	}

    /*@Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Functions:");
        for(Function f: functions) {
            sb.append(f + "\n");
        }

        sb.append("Classes:");
        for(Class c: classes) {
            sb.append(c + "\n");
        }

        return sb.toString();
    }*/
}

