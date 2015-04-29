grammar Glacier;

tokens {
	STARTBLOCK,
	ENDBLOCK
}

@header {
package antlr4;
}

shaderProg
:
	'shader' IDENTIFIER NL+ vertexShader fragmentShader
;

functionBlock
:
	IDENTIFIER IDENTIFIER '(' arguments ')' NL statementsBlock
;

vertexShader
:
	'vert' NL+ STARTBLOCK? shaderBlock ENDBLOCK NL*
;

fragmentShader
:
	'frag' NL+ STARTBLOCK? shaderBlock ENDBLOCK NL*
;

shaderBlock
:
	(
		(
			'inout'
			| 'mout'
		) NL+ STARTBLOCK arguments NL+ ENDBLOCK
	)?
	(
		'matrices' NL+ STARTBLOCK arguments NL+ ENDBLOCK
	)?
	(
		'uniforms' NL+ STARTBLOCK uniformBlock NL+ ENDBLOCK
	)?
	(
		'main()' NL+ statementsBlock
	)? 
	functionBlock*
;

uniformBlock
:
	(
		IDENTIFIER IDENTIFIER NL
	)+
;

statementsBlock
:
	(
		STARTBLOCK statement* ENDBLOCK
	)?
;

statement
:
	(
		localVarDef
		| stmtSet
		| stmtReturn
		| expr
	) NL
	| stmtIf
	| stmtWhile
	| stmtForLoop
;

stmtIf
:
	'if' cond = expr NL thenStatements = statementsBlock
	(
		'else' elseStatements
	)?
;

stmtForLoop
:
	forRangeLoop
	| forIteratorLoop
;

localVarDefInline
:
	name = IDENTIFIER
;

forRangeLoop
:
	'for' loopVar = localVarDefInline '=' start = expr direction =
	(
		'to'
		| 'downto'
	) end = expr
	(
		'step' step = expr
	)? NL statementsBlock
;

forIteratorLoop
:
	'for' loopVar = localVarDefInline iterStyle =
	(
		IN
		| 'from'
	) iteratorExpr = expr NL statementsBlock
;

elseStatements
:
	stmtIf
	| NL statementsBlock
;

stmtWhile
:
	'while' cond = expr NL statementsBlock
;

stmtReturn
:
	'return' expr
;

stmtSet
:
	left = exprAssignable
	(
		assignOp =
		(
			'='
			| '+='
			| '-='
			| '*='
			| '/='
		) right = expr
		| incOp = '++'
		| decOp = '--'
	)
;

localVarDef
:
	(
		var = 'var'
		| let = 'let'
		| typeName = IDENTIFIER
	) name = IDENTIFIER
	(
		'=' initial = expr
	)?
;

arguments
:
	(
		IDENTIFIER? IDENTIFIER
		(
			',' IDENTIFIER? IDENTIFIER
		)*
	)?
;

expr
:
	exprPrimary
	| receiver = expr dotsCall =
	(
		'.'
		| '..'
	) funcName = IDENTIFIER? '(' exprList ')'
	| receiver = expr dotsVar =
	(
		'.'
		| '..'
	) varName = IDENTIFIER? indexes?
	| op = '-' right = expr
	| left = expr op =
	(
		'*'
		| '/'
		| '%'
	) right = expr
	| left = expr op =
	(
		'+'
		| '-'
	) right = expr
	| left = expr op =
	(
		'<='
		| '<'
		| '>'
		| '>='
	) right = expr
	| left = expr op =
	(
		'=='
		| '!='
	) right = expr
	| op = 'not' right = expr
	| left = expr op = 'and' right = expr
	| left = expr op = 'or' right = expr
	|
;

exprPrimary
:
	exprFunctionCall
	| varname = IDENTIFIER indexes?
	| atom =
	(
		INT
		| FLOAT
		| STRING
		| 'null'
		| 'true'
		| 'false'
	)
	| '(' expr ')'
;

exprAssignable
:
	exprMemberVar
	| exprVarAccess
;

exprMemberVar
:
	expr dots =
	(
		'.'
		| '..'
	) varname = IDENTIFIER indexes?
;

exprVarAccess
:
	varname = IDENTIFIER indexes?
;

indexes
:
	'[' expr ']'
;

exprFunctionCall
:
	funcName = IDENTIFIER '(' exprList ')'
;

exprList
:
	exprs += expr
	(
		',' exprs += expr
	)*
;

COMMA
:
	','
;

IN
:
	':'
;

INT
:
	(
		'-'
	)?
	(
		'0' .. '9'
	)
	(
		'0' .. '9'
	)*
;

FLOAT
:
	(
		'-'
	)?
	(
		'0' .. '9'
	)
	(
		'0' .. '9'
	)*
	(
		'.'
		(
			'0' .. '9'
		)
		(
			'0' .. '9'
		)*
	)?
;

IDENTIFIER
:
	[a-zA-Z_] [a-zA-Z0-9_]*
;

STRING
:
	'"'
	(
		~'"'
	)* '"'
;

NL
:
	[\r\n]+
;

TAB
:
	[\t]
;

WS
:
	[ ]+ -> skip
;

COMMENT
:
	'/*' .*? '*/' -> skip
;

LINE_COMMENT
:
	'//' ~[\r\n]* -> skip
;