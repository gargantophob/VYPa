grammar VYPlanguage;

/*
 * Parser Rules
 */

expression	:	expression '+' expression
				| expression '*' expression
				| '(' expression ')'
				| NUMBER;

/*
 * Lexer rules.
 */

NUMBER	: [0-9]+	;

WHITESPACE	:	'\n' -> skip;