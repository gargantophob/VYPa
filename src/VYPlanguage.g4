grammar VYPlanguage;

/*
 * Parser Rules
 */

prim_type	:	INT | STRING;
object_type	:	IDENTIFIER;
data_type	:	prim_type | object_type;
type	: data_type | VOID;

definition	:	variable_definition | function_definition;

class_definition	:	CLASS object_type ':' object_type '{' definition* '}' ;

function_definition	:	type IDENTIFIER '(' param_list ')' '{' statement* '}';
param_list	:	VOID | param (',' param)*;
param	:	data_type IDENTIFIER;

statement	:	block_statement | variable_definition | assignment
				| conditional_statement | iteration_statement | function_call
				| method_call | return_statement ;

variable_definition	:	data_type IDENTIFIER (',' IDENTIFIER)* ';' ;
block_statement	:	'{' statement* '}';
assignment	:	IDENTIFIER '=' expression ';' ;
conditional_statement	:	IF '(' expression ')' block_statement ELSE block_statement;
iteration_statement	:	WHILE '(' expression ')' block_statement;
function_call	:	IDENTIFIER '(' expression_list ')' ';';
method_call	:	context_object '.' IDENTIFIER '(' expression_list ')' ';';
return_statement	:	RETURN expression? ';';

expression_list :	(expression (',' expression)*)?;
expression	:	INTEGER_LITERAL | STRING_LITERAL | IDENTIFIER | '(' expression ')'
				| function_call | method_call | object_creation | context_object;

context_object	:	THIS | SUPER | IDENTIFIER;

object_creation	:	NEW object_type ;



/* Lexer rules. */

fragment LETTER	:	[a-zA-Z] | '_';
fragment DIGIT	:	[0-9];
fragment ESCAPE_SEQUENCE	:	'\\' [nt\\"];
fragment STRING_CHARACTER	:	[\u0020-\u0022\u0024-\uFFFF] | ESCAPE_SEQUENCE;

WS	:	[ \t\r\n\u000C]+ -> skip;
LINE_COMMENT	:	'//' ~[\r\n]* -> skip;
BLOCK_COMMENT	:   '/*' .*? '*/' -> skip;

CLASS	:	'class';
ELSE	:	'else';
IF	:	'if';
INT	:	'int';
NEW	:	'new';
RETURN	:	'return';
STRING	:	'string';
SUPER	:	'super';
THIS	:	'this';
VOID	:	'void';
WHILE	:	'while';

IDENTIFIER	:	LETTER	(LETTER | DIGIT)+;

INTEGER_LITERAL	: DIGIT+;
STRING_LITERAL	:	'"' (STRING_CHARACTER)* '"';