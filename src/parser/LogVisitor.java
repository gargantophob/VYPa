// Generated from Log.g4 by ANTLR 4.7.1
package parser;
import org.antlr.v4.runtime.tree.ParseTreeVisitor;

/**
 * This interface defines a complete generic visitor for a parse tree produced
 * by {@link LogParser}.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for
 * operations with no return type.
 */
public interface LogVisitor<T> extends ParseTreeVisitor<T> {
	/**
	 * Visit a parse tree produced by {@link LogParser#log}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLog(LogParser.LogContext ctx);
	/**
	 * Visit a parse tree produced by {@link LogParser#entry}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitEntry(LogParser.EntryContext ctx);
	/**
	 * Visit a parse tree produced by {@link LogParser#timestamp}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTimestamp(LogParser.TimestampContext ctx);
	/**
	 * Visit a parse tree produced by {@link LogParser#level}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLevel(LogParser.LevelContext ctx);
	/**
	 * Visit a parse tree produced by {@link LogParser#message}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMessage(LogParser.MessageContext ctx);
}