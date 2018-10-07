// Generated from VYPlanguage.g4 by ANTLR 4.7.1
package parser;
import org.antlr.v4.runtime.tree.ParseTreeVisitor;

/**
 * This interface defines a complete generic visitor for a parse tree produced
 * by {@link VYPlanguageParser}.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for
 * operations with no return type.
 */
public interface VYPlanguageVisitor<T> extends ParseTreeVisitor<T> {
	/**
	 * Visit a parse tree produced by {@link VYPlanguageParser#prim_type}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPrim_type(VYPlanguageParser.Prim_typeContext ctx);
	/**
	 * Visit a parse tree produced by {@link VYPlanguageParser#object_type}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitObject_type(VYPlanguageParser.Object_typeContext ctx);
	/**
	 * Visit a parse tree produced by {@link VYPlanguageParser#data_type}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitData_type(VYPlanguageParser.Data_typeContext ctx);
	/**
	 * Visit a parse tree produced by {@link VYPlanguageParser#type}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitType(VYPlanguageParser.TypeContext ctx);
	/**
	 * Visit a parse tree produced by {@link VYPlanguageParser#definition}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDefinition(VYPlanguageParser.DefinitionContext ctx);
	/**
	 * Visit a parse tree produced by {@link VYPlanguageParser#class_definition}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitClass_definition(VYPlanguageParser.Class_definitionContext ctx);
	/**
	 * Visit a parse tree produced by {@link VYPlanguageParser#function_definition}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFunction_definition(VYPlanguageParser.Function_definitionContext ctx);
	/**
	 * Visit a parse tree produced by {@link VYPlanguageParser#param_list}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitParam_list(VYPlanguageParser.Param_listContext ctx);
	/**
	 * Visit a parse tree produced by {@link VYPlanguageParser#param}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitParam(VYPlanguageParser.ParamContext ctx);
	/**
	 * Visit a parse tree produced by {@link VYPlanguageParser#statement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStatement(VYPlanguageParser.StatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link VYPlanguageParser#variable_definition}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitVariable_definition(VYPlanguageParser.Variable_definitionContext ctx);
	/**
	 * Visit a parse tree produced by {@link VYPlanguageParser#block_statement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBlock_statement(VYPlanguageParser.Block_statementContext ctx);
	/**
	 * Visit a parse tree produced by {@link VYPlanguageParser#assignment}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAssignment(VYPlanguageParser.AssignmentContext ctx);
	/**
	 * Visit a parse tree produced by {@link VYPlanguageParser#conditional_statement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitConditional_statement(VYPlanguageParser.Conditional_statementContext ctx);
	/**
	 * Visit a parse tree produced by {@link VYPlanguageParser#iteration_statement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIteration_statement(VYPlanguageParser.Iteration_statementContext ctx);
	/**
	 * Visit a parse tree produced by {@link VYPlanguageParser#function_call}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFunction_call(VYPlanguageParser.Function_callContext ctx);
	/**
	 * Visit a parse tree produced by {@link VYPlanguageParser#method_call}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMethod_call(VYPlanguageParser.Method_callContext ctx);
	/**
	 * Visit a parse tree produced by {@link VYPlanguageParser#return_statement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitReturn_statement(VYPlanguageParser.Return_statementContext ctx);
	/**
	 * Visit a parse tree produced by {@link VYPlanguageParser#expression_list}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExpression_list(VYPlanguageParser.Expression_listContext ctx);
	/**
	 * Visit a parse tree produced by {@link VYPlanguageParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExpression(VYPlanguageParser.ExpressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link VYPlanguageParser#context_object}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitContext_object(VYPlanguageParser.Context_objectContext ctx);
	/**
	 * Visit a parse tree produced by {@link VYPlanguageParser#object_creation}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitObject_creation(VYPlanguageParser.Object_creationContext ctx);
}