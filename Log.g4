grammar Log;

/*
 * Parser rules
 */

log	:	entry+	;
entry:	timestamp	' ' level ' ' message CRLF	;
timestamp	:	DATE ' ' TIME	;
level	:	'ERROR' | 'INFO' | 'DEBUG'	;
message	:	(STRING | ' ')+	;

/*
 * Lexer rules.
 */

fragment DIGIT	:	[0-9]	;
fragment TWODIGIT	:	DIGIT DIGIT	;
fragment LETTER		:	[A-Za-z]	;

DATE	:	TWODIGIT TWODIGIT '-' LETTER LETTER LETTER '-' TWODIGIT	;
TIME	:	TWODIGIT ':' TWODIGIT ':' TWODIGIT	;
STRING	:	LETTER+	;
CRLF	:	'\r' ? '\n' | '\r'	;

WHITESPACE	:	'\n' -> skip;