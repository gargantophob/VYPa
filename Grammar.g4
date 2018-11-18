grammar Grammar;

/* Parser Rules */

type	:	Void | Int | String | name;
name	:	Id;

program	:	(functionDefinition | classDefinition)*;

functionDefinition	:	type name '(' paramList ')' blockStatement;
paramList		:	Void | formalParameter (',' formalParameter)*;
formalParameter	:	type name;

classDefinition	:	Class name ':' name '{' (variableDeclaration | functionDefinition)* '}';
variableDeclaration	:	type name (',' name)* ';';

statement	:	variableDeclaration | assignment | conditional
				| iteration | call | returnStatement ;

assignment	:	variable '=' ex ';' ;
blockStatement	:	'{' statement* '}';
conditional	:	If '(' ex ')' blockStatement Else blockStatement;
iteration	:	While '(' ex ')' blockStatement;
returnStatement	:	Return ex? ';';

atomicVariable	:	This | name;
variable	:	atomicVariable ('.' atomicVariable)*;
call	:	((Super | variable) '.')? name '(' (ex (',' ex)*)? ')' ';';

ex	:	value | New name | '(' ex ')' | '(' type ')' ex
			| call | '!' ex | ex '*' ex | ex '/' ex
			| ex '+' ex | ex '-' ex
			| ex '<' ex | ex '>' ex | ex '<=' ex | ex '>=' ex
			| ex '==' ex | ex '!=' ex | ex '&&' ex | ex '||' ex;

value	:	literal | variable;
literal	:	IntegerLiteral | StringLiteral;

/* Lexer rules. */

fragment Letter	:	[a-zA-Z] | '_';
fragment Zero	:	'0';
fragment Nonzero	:	[1-9];
fragment Digit	:	Zero | Nonzero;

fragment EscapeSequence	:	'\\' [nt\\"];
fragment StringCharacter	:	EscapeSequence | ~[\u0000-\u001F\u0022];

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

Id	:	Letter	(Letter | Digit)*;
IntegerLiteral	: Zero | Nonzero Digit*;
StringLiteral	:	'"' (StringCharacter)* '"';

Whitespace	:	[ \t\r\n]+ -> skip;
LineComment	:	'//' ~[\n]* -> skip;
BlockComment	:   '/*' .*? '*/' -> skip;

