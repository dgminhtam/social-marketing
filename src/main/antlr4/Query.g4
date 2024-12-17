/*
Copyright (c) 2019, Michael Mollard
*/

grammar Query;

input
    : query EOF
    ;

query
    : left=query logicalOp=(AND | OR) right=query #opQuery
    | LPAREN query RPAREN #priorityQuery
    | criteria #atomQuery
    ;

criteria
    : key OP value
    | key MULTIPLE_VALUE_OP LPAREN value (COMMA value)* RPAREN
    | NO_VALUE_FUNC LPAREN key RPAREN
    | ONE_VALUE_FUNC LPAREN key COMMA value RPAREN
    ;

key
    : IDENTIFIER
    ;

value
    : IDENTIFIER
    | STRING
    | NUMBER
    | BOOL
    ;

OP
    : EQ
    | NE
    | GT
    | GE
    | LT
    | LE
    ;

ONE_VALUE_FUNC
    : CONTAINS
    | CONTAINS_IGNORE_CASE
    | STARTS_WITH
    | STARTS_WITH_IGNORE_CASE
    | ENDS_WITH
    | ENDS_WITH_IGNORE_CASE
    ;

NO_VALUE_FUNC
    : NULL
    | NOT_NULL
    | EMPTY
    | NOT_EMPTY
    ;

MULTIPLE_VALUE_OP
    : IN
    | NIN
    ;

BOOL
    : 'true'
    | 'false'
    ;

STRING
    : '"' DoubleStringCharacter* '"'
    | '\'' SingleStringCharacter* '\''
    ;

fragment DoubleStringCharacter
    : ~["\\\r\n]
    | '\\' EscapeSequence
    | LineContinuation
    ;
fragment SingleStringCharacter
    : ~['\\\r\n]
    | '\\' EscapeSequence
    | LineContinuation
    ;
fragment EscapeSequence
    : CharacterEscapeSequence
    | HexEscapeSequence
    | UnicodeEscapeSequence
    ;
fragment CharacterEscapeSequence
    : SingleEscapeCharacter
    | NonEscapeCharacter
    ;
fragment HexEscapeSequence
    : 'x' HexDigit HexDigit
    ;

fragment UnicodeEscapeSequence
    : 'u' HexDigit HexDigit HexDigit HexDigit
    ;
fragment SingleEscapeCharacter
    : ['"\\bfnrtv]
    ;

fragment NonEscapeCharacter
    : ~['"\\bfnrtv0-9xu\r\n]
    ;
fragment EscapeCharacter
    : SingleEscapeCharacter
    | DecimalDigit
    | [xu]
    ;
fragment LineContinuation
    : '\\' LineTerminatorSequence
    ;
fragment LineTerminatorSequence
    : '\r\n'
    | LineTerminator
    ;
fragment DecimalDigit
    : [0-9]
    ;
fragment HexDigit
    : [0-9a-fA-F]
    ;
fragment OctalDigit
    : [0-7]
    ;
AND
    : 'and'
    ;
OR
    : 'or'
    ;
NUMBER
    : ('0' .. '9') ('0' .. '9')* POINT? ('0' .. '9')*
    ;
LPAREN
    : '('
    ;
RPAREN
    : ')'
    ;

EQ
    : ' eq '
    ;
NE
    : ' ne '
    ;
GT
    : ' gt '
    ;
GE
    : ' ge '
    ;
LT
    : ' lt '
    ;
LE
    : ' le '
    ;
CONTAINS
    : 'contains'
    ;
CONTAINS_IGNORE_CASE
    : 'containsIgnoreCase'
    ;
STARTS_WITH
    : 'startsWith'
    ;
STARTS_WITH_IGNORE_CASE
    : 'startsWithIgnoreCase'
    ;
ENDS_WITH
    : 'endsWith'
    ;
ENDS_WITH_IGNORE_CASE
    : 'endsWithIgnoreCase'
    ;
NULL
    : 'null'
    ;
NOT_NULL
    : 'notNull'
    ;
EMPTY
    : 'empty'
    ;
NOT_EMPTY
    : 'notEmpty'
    ;
IN
    : ' in '
    ;
NIN
    : ' nin '
    ;

fragment POINT
    : '.'
    ;
COMMA
    : ','
    ;

IDENTIFIER
    : [A-Za-z0-9.]+
    ;
LineTerminator
    : [\r\n\u2028\u2029] -> channel(HIDDEN)
    ;
WS
    : [ \r\t\f\n]+ -> skip
    ;
