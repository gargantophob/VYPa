package main;

import parser.*;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;

import java.util.List;
import java.util.ArrayList;


public class LogListener extends LogBaseListener {
	public List<LogEntry> entries = new ArrayList<>();
    public LogEntry current;

	@Override
	public void enterEntry(LogParser.EntryContext ctx) {
		current = new LogEntry();

		/*
		TerminalNode node = ctx.Identifier();
		String methodName = node.getText();

		if (Character.isUpperCase(methodName.charAt(0))) {
            String error = String.format("Method %s is uppercased!", methodName);
            errors.add(error);
        }
        */
	}

	@Override
	public void exitEntry(LogParser.EntryContext ctx) {
		entries.add(current);
	}

	@Override
	public void enterTimestamp(LogParser.TimestampContext ctx) {
		current.timestamp = ctx.getText();
	}

	@Override
	public void enterLevel(LogParser.LevelContext ctx) {
		current.level = ctx.getText();
	}

	@Override
	public void enterMessage(LogParser.MessageContext ctx) {
		current.message = ctx.getText();
	}

}