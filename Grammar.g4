grammar Grammar;

/* Parser Rules */

primType	:	Int | String;
objectType	:	Identifier;
dataType	:	primType | objectType;
voidType	:	Void;
type		: dataType | voidType;

program	:	globalDefinition*;	/* whether main() exists will be a semantic check */
globalDefinition	:	functionDefinition | classDefinition;

functionDefinition	:	type Identifier '(' paramList ')' blockStatement;
paramList	:	voidType | formalParameter (',' formalParameter)*;
formalParameter	:	dataType Identifier;

classDefinition	:	Class objectType ':' objectType '{' fieldDefinition* '}' ;
fieldDefinition	:	variableDefinition | functionDefinition;
variableDefinition	:	dataType Identifier (',' Identifier)* ';' ;

statement	:	variableDefinition | assignment | conditional
				| iteration | functionCall | methodCall | returnStatement ;

assignment	:	Identifier '=' ex ';' ;
blockStatement	:	'{' statement* '}';
conditional	:	If '(' ex ')' blockStatement Else blockStatement;
iteration	:	While '(' ex ')' blockStatement;
returnStatement	:	Return ex? ';';

functionCall	:	Identifier '(' exList ')' ';';
methodCall	:	contextObject '.' Identifier '(' exList ')' ';';
exList :	(ex (',' ex)*)?;
contextObject	:	This | Super | Identifier;

ex	:	value | objectCreation | '(' ex ')' | casting
				| functionCall | methodCall | '!' ex | ex '*' ex | ex '/' ex
				| ex '+' ex | ex '-' ex
				| ex '<' ex | ex '>' ex | ex '<=' ex | ex '>=' ex
				| ex '==' ex | ex '!=' ex | ex '&&' ex | ex '||' ex;

value	:	literal | variable;
literal	:	IntegerLiteral | StringLiteral;
variable	:	localVariable | instanceVariable;
localVariable	:	Identifier | This;
instanceVariable	:	localVariable '.' Identifier;

objectCreation	:	New objectType ;
casting	:	'(' objectType ')' ex;

/*
not	:	'!' ex;
mul	:	ex '*' ex;
div	:	ex '/' ex;
add	:	ex '+' ex;
sub	:	ex '-' ex;
less	:	ex '<' ex;
greater	:	ex '>' ex;
lessOrEqual	:	ex '<=' ex;
greaterOrEqual	:	ex '>=' ex;
equal	:	ex '==' ex;
notEqual	:	ex '!=' ex;
and	:	ex '&&' ex;
or	:	ex '||' ex;
*/

/* Lexer rules. */

fragment Letter	:	[a-zA-Z] | '_';
fragment Zero	:	'0';
fragment Nonzero	:	[1-9];
fragment Digit	:	Zero | Nonzero;
fragment EscapeSequence	:	'\\' [nt\\"];
fragment StringCharacter	:	EscapeSequence | ~[\u0000-\u001F\u0022];

Whitespace	:	[ \t\r\n]+ -> skip;
LineComment	:	'//' ~[\n]* -> skip;
BlockComment	:   '/*' .*? '*/' -> skip;

Class	:	'class';
Else	:	'else';
If		:	'if';
Int		:	'int';
New		:	'new';
Return	:	'return';
String	:	'string';
Super	:	'super';
This	:	'this';
Void	:	'void';
While	:	'while';

Identifier	:	Letter	(Letter | Digit)*;
IntegerLiteral	: Zero | Nonzero Digit*;
StringLiteral	:	'"' (StringCharacter)* '"';
