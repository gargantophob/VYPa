// Generated from VYPlanguage.g4 by ANTLR 4.7.1
package parser;
import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link VYPlanguageParser}.
 */
public interface VYPlanguageListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link VYPlanguageParser#prim_type}.
	 * @param ctx the parse tree
	 */
	void enterPrim_type(VYPlanguageParser.Prim_typeContext ctx);
	/**
	 * Exit a parse tree produced by {@link VYPlanguageParser#prim_type}.
	 * @param ctx the parse tree
	 */
	void exitPrim_type(VYPlanguageParser.Prim_typeContext ctx);
	/**
	 * Enter a parse tree produced by {@link VYPlanguageParser#object_type}.
	 * @param ctx the parse tree
	 */
	void enterObject_type(VYPlanguageParser.Object_typeContext ctx);
	/**
	 * Exit a parse tree produced by {@link VYPlanguageParser#object_type}.
	 * @param ctx the parse tree
	 */
	void exitObject_type(VYPlanguageParser.Object_typeContext ctx);
	/**
	 * Enter a parse tree produced by {@link VYPlanguageParser#data_type}.
	 * @param ctx the parse tree
	 */
	void enterData_type(VYPlanguageParser.Data_typeContext ctx);
	/**
	 * Exit a parse tree produced by {@link VYPlanguageParser#data_type}.
	 * @param ctx the parse tree
	 */
	void exitData_type(VYPlanguageParser.Data_typeContext ctx);
	/**
	 * Enter a parse tree produced by {@link VYPlanguageParser#type}.
	 * @param ctx the parse tree
	 */
	void enterType(VYPlanguageParser.TypeContext ctx);
	/**
	 * Exit a parse tree produced by {@link VYPlanguageParser#type}.
	 * @param ctx the parse tree
	 */
	void exitType(VYPlanguageParser.TypeContext ctx);
	/**
	 * Enter a parse tree produced by {@link VYPlanguageParser#definition}.
	 * @param ctx the parse tree
	 */
	void enterDefinition(VYPlanguageParser.DefinitionContext ctx);
	/**
	 * Exit a parse tree produced by {@link VYPlanguageParser#definition}.
	 * @param ctx the parse tree
	 */
	void exitDefinition(VYPlanguageParser.DefinitionContext ctx);
	/**
	 * Enter a parse tree produced by {@link VYPlanguageParser#class_definition}.
	 * @param ctx the parse tree
	 */
	void enterClass_definition(VYPlanguageParser.Class_definitionContext ctx);
	/**
	 * Exit a parse tree produced by {@link VYPlanguageParser#class_definition}.
	 * @param ctx the parse tree
	 */
	void exitClass_definition(VYPlanguageParser.Class_definitionContext ctx);
	/**
	 * Enter a parse tree produced by {@link VYPlanguageParser#function_definition}.
	 * @param ctx the parse tree
	 */
	void enterFunction_definition(VYPlanguageParser.Function_definitionContext ctx);
	/**
	 * Exit a parse tree produced by {@link VYPlanguageParser#function_definition}.
	 * @param ctx the parse tree
	 */
	void exitFunction_definition(VYPlanguageParser.Function_definitionContext ctx);
	/**
	 * Enter a parse tree produced by {@link VYPlanguageParser#param_list}.
	 * @param ctx the parse tree
	 */
	void enterParam_list(VYPlanguageParser.Param_listContext ctx);
	/**
	 * Exit a parse tree produced by {@link VYPlanguageParser#param_list}.
	 * @param ctx the parse tree
	 */
	void exitParam_list(VYPlanguageParser.Param_listContext ctx);
	/**
	 * Enter a parse tree produced by {@link VYPlanguageParser#param}.
	 * @param ctx the parse tree
	 */
	void enterParam(VYPlanguageParser.ParamContext ctx);
	/**
	 * Exit a parse tree produced by {@link VYPlanguageParser#param}.
	 * @param ctx the parse tree
	 */
	void exitParam(VYPlanguageParser.ParamContext ctx);
	/**
	 * Enter a parse tree produced by {@link VYPlanguageParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterStatement(VYPlanguageParser.StatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link VYPlanguageParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitStatement(VYPlanguageParser.StatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link VYPlanguageParser#variable_definition}.
	 * @param ctx the parse tree
	 */
	void enterVariable_definition(VYPlanguageParser.Variable_definitionContext ctx);
	/**
	 * Exit a parse tree produced by {@link VYPlanguageParser#variable_definition}.
	 * @param ctx the parse tree
	 */
	void exitVariable_definition(VYPlanguageParser.Variable_definitionContext ctx);
	/**
	 * Enter a parse tree produced by {@link VYPlanguageParser#block_statement}.
	 * @param ctx the parse tree
	 */
	void enterBlock_statement(VYPlanguageParser.Block_statementContext ctx);
	/**
	 * Exit a parse tree produced by {@link VYPlanguageParser#block_statement}.
	 * @param ctx the parse tree
	 */
	void exitBlock_statement(VYPlanguageParser.Block_statementContext ctx);
	/**
	 * Enter a parse tree produced by {@link VYPlanguageParser#assignment}.
	 * @param ctx the parse tree
	 */
	void enterAssignment(VYPlanguageParser.AssignmentContext ctx);
	/**
	 * Exit a parse tree produced by {@link VYPlanguageParser#assignment}.
	 * @param ctx the parse tree
	 */
	void exitAssignment(VYPlanguageParser.AssignmentContext ctx);
	/**
	 * Enter a parse tree produced by {@link VYPlanguageParser#conditional_statement}.
	 * @param ctx the parse tree
	 */
	void enterConditional_statement(VYPlanguageParser.Conditional_statementContext ctx);
	/**
	 * Exit a parse tree produced by {@link VYPlanguageParser#conditional_statement}.
	 * @param ctx the parse tree
	 */
	void exitConditional_statement(VYPlanguageParser.Conditional_statementContext ctx);
	/**
	 * Enter a parse tree produced by {@link VYPlanguageParser#iteration_statement}.
	 * @param ctx the parse tree
	 */
	void enterIteration_statement(VYPlanguageParser.Iteration_statementContext ctx);
	/**
	 * Exit a parse tree produced by {@link VYPlanguageParser#iteration_statement}.
	 * @param ctx the parse tree
	 */
	void exitIteration_statement(VYPlanguageParser.Iteration_statementContext ctx);
	/**
	 * Enter a parse tree produced by {@link VYPlanguageParser#function_call}.
	 * @param ctx the parse tree
	 */
	void enterFunction_call(VYPlanguageParser.Function_callContext ctx);
	/**
	 * Exit a parse tree produced by {@link VYPlanguageParser#function_call}.
	 * @param ctx the parse tree
	 */
	void exitFunction_call(VYPlanguageParser.Function_callContext ctx);
	/**
	 * Enter a parse tree produced by {@link VYPlanguageParser#method_call}.
	 * @param ctx the parse tree
	 */
	void enterMethod_call(VYPlanguageParser.Method_callContext ctx);
	/**
	 * Exit a parse tree produced by {@link VYPlanguageParser#method_call}.
	 * @param ctx the parse tree
	 */
	void exitMethod_call(VYPlanguageParser.Method_callContext ctx);
	/**
	 * Enter a parse tree produced by {@link VYPlanguageParser#return_statement}.
	 * @param ctx the parse tree
	 */
	void enterReturn_statement(VYPlanguageParser.Return_statementContext ctx);
	/**
	 * Exit a parse tree produced by {@link VYPlanguageParser#return_statement}.
	 * @param ctx the parse tree
	 */
	void exitReturn_statement(VYPlanguageParser.Return_statementContext ctx);
	/**
	 * Enter a parse tree produced by {@link VYPlanguageParser#expression_list}.
	 * @param ctx the parse tree
	 */
	void enterExpression_list(VYPlanguageParser.Expression_listContext ctx);
	/**
	 * Exit a parse tree produced by {@link VYPlanguageParser#expression_list}.
	 * @param ctx the parse tree
	 */
	void exitExpression_list(VYPlanguageParser.Expression_listContext ctx);
	/**
	 * Enter a parse tree produced by {@link VYPlanguageParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterExpression(VYPlanguageParser.ExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link VYPlanguageParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitExpression(VYPlanguageParser.ExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link VYPlanguageParser#context_object}.
	 * @param ctx the parse tree
	 */
	void enterContext_object(VYPlanguageParser.Context_objectContext ctx);
	/**
	 * Exit a parse tree produced by {@link VYPlanguageParser#context_object}.
	 * @param ctx the parse tree
	 */
	void exitContext_object(VYPlanguageParser.Context_objectContext ctx);
	/**
	 * Enter a parse tree produced by {@link VYPlanguageParser#object_creation}.
	 * @param ctx the parse tree
	 */
	void enterObject_creation(VYPlanguageParser.Object_creationContext ctx);
	/**
	 * Exit a parse tree produced by {@link VYPlanguageParser#object_creation}.
	 * @param ctx the parse tree
	 */
	void exitObject_creation(VYPlanguageParser.Object_creationContext ctx);
}