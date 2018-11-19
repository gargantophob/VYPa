grammar Grammar;

/* Parser Rules */

type	:	Void | Int | String | name;
name	:	Id;
atomicPath	:	This | name;
path	:	atomicPath ('.' atomicPath)*;

program	:	(functionDefinition | classDefinition)*;

functionDefinition	:	type name '(' paramList ')' block;
paramList		:	Void | formalParameter (',' formalParameter)*;
formalParameter	:	type name;

classDefinition	:	Class name ':' name '{' (variableDeclaration | functionDefinition)* '}';
variableDeclaration	:	type name (',' name)* ';';

statement	:	variableDeclaration | assignment | conditional | iteration | call | returnStatement ;

assignment	:	path '=' ex ';' ;
block	:	'{' statement* '}';
conditional	:	If '(' ex ')' block Else block;
iteration	:	While '(' ex ')' block;
returnStatement	:	Return ex? ';';

call	:	((Super | path ) '.')? name '(' (ex (',' ex)*)? ')' ';';

ex	:	value | New name | '(' ex ')' | '(' type ')' ex
			| call | '!' ex | ex '*' ex | ex '/' ex
			| ex '+' ex | ex '-' ex
			| ex '<' ex | ex '>' ex | ex '<=' ex | ex '>=' ex
			| ex '==' ex | ex '!=' ex | ex '&&' ex | ex '||' ex;

value	:	literal | path;
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

