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
	'shader' shaderName = IDENTIFIER NL+ vertexShader fragmentShader
;

functionBlock
:
	returnType=IDENTIFIER funcName=IDENTIFIER '(' arguements=arguments ')' NL body=statementsBlock
;

vertexShader
:
	'vert' NL+ STARTBLOCK? vshaderBlock ENDBLOCK NL*
;

fragmentShader
:
	'frag' NL+ STARTBLOCK? vshaderBlock ENDBLOCK NL*
;

varDef
:
	varType = IDENTIFIER? varName = IDENTIFIER
;

arguments
:
	(
		vardefs += varDef
		(
			',' vardefs += varDef
		)*
	)?
;

vshaderBlock
:
	(
		'inout' NL+ STARTBLOCK inoutArgs = arguments NL+ ENDBLOCK
	)?
	(
		'in' NL+ STARTBLOCK inArgs = arguments NL+ ENDBLOCK
	)?
	(
		'out' NL+ STARTBLOCK outArgs = arguments NL+ ENDBLOCK
	)?
	(
		'matrices' NL+ STARTBLOCK matsArgs = arguments NL+ ENDBLOCK
	)?
	(
		'uniforms' NL+ STARTBLOCK uniformArgs = uniformBlock NL* ENDBLOCK
	)?
	(
		'main()' NL+ mainFunc = statementsBlock
	)? functions += functionBlock*
;

uniformBlock
:
	uniforms += uniformDef+
;

uniformDef
:
	uniformType = IDENTIFIER uniformName = IDENTIFIER NL
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
	| ieDirective '.' IDENTIFIER
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

ieDirective
:
	(
		'in'
		| 'out'
	)
;

exprMemberVar
:
	(
		expr
		| ieDirective
	) dots =
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