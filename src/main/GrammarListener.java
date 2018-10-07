package main;

import parser.*;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;

import java.util.List;
import java.util.ArrayList;


public class GrammarListener extends GrammarBaseListener {
	@Override
	public void enterClassDefinition(GrammarParser.ClassDefinitionContext ctx) {
		System.out.println("Class definition.");
	}
	/*
	public List<LogEntry> entries = new ArrayList<>();
    public LogEntry current;

	@Override
	public void enterEntry(LogParser.EntryContext ctx) {
		current = new LogEntry();

		
		TerminalNode node = ctx.Identifier();
		String methodName = node.getText();

		if (Character.isUpperCase(methodName.charAt(0))) {
            String error = String.format("Method %s is uppercased!", methodName);
            errors.add(error);
        }
        
	}

	@Override
	public void exitEntry(LogParser.EntryContext ctx) {
		entries.add(current);
	}
	*/

}