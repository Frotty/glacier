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
	'shader' shaderName = IDENTIFIER NL+ glacierHeader vertexShader fragmentShader
;

glacierHeader
:
	drawDirective NL* contextOptions?
;


drawDirective: 
	'draw' drawDirect=('geometry'|'fullscreen') NL
;

contextOptions: 
	'context' '[' (options+=contextOpt ',')* options+=contextOpt? ']' NL
;

contextOpt:
	optionName= IDENTIFIER ('=' optionValue = IDENTIFIER)?
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
		inBlock
	)?
	(
		outBlock
	)?
	(
		transBlock
	)?
	(
		uniformsBlock
	)?
	(
		materialBlock
	)?
	(
		'main()' NL+ mainFunc = statementsBlock
	)? functions += functionBlock*
;

inBlock
:
	'in' NL+ STARTBLOCK inArgs = arguments NL+ ENDBLOCK
;

outBlock
:
	'out' NL+ STARTBLOCK outArgs = arguments NL+ ENDBLOCK
;

transBlock
:
	'trans' NL+ STARTBLOCK transArgs = arguments NL+ ENDBLOCK
;

uniformsBlock
:
	'uni' NL+ STARTBLOCK uniformArgs = arguments NL* ENDBLOCK
;

materialBlock
:
	'mat' NL+ STARTBLOCK materialArgs = arguments NL* ENDBLOCK
;

functionBlock
:
	returnType = IDENTIFIER funcName = IDENTIFIER '(' arguements = arguments ')'
	NL body = statementsBlock
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
			(
				','
				| NL+
			) vardefs += varDef
		)*
	)?
;

statementsBlock
:
	(
		STARTBLOCK stmts += statement* ENDBLOCK
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
	typeName = IDENTIFIER name = IDENTIFIER
	(
		'=' initial = expr
	)?
;

expr
:
	primary = exprPrimary
	| receiver = expr dotsCall =
	(
		'.'
		| '..'
	) funcName = IDENTIFIER? '(' params = exprList ')'
	| receiver = expr dotsVar =
	(
		'.'
		| '..'
	) varName = IDENTIFIER indexes?
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
	| ieD = globalType '.' varName = IDENTIFIER
;

exprPrimary
:
	funcCall = exprFunctionCall
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

globalType
:
	(
		'in'
		| 'out'
		| 'trans'
		| 'uni'
		| 'mat'
	)
;

exprMemberVar
:
	(
		expr
		| ieDirect = globalType
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
	funcName = IDENTIFIER '(' params = exprList ')'
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

FLOAT: [0-9]+ '.' [0-9]* | '.'[0-9]+;

INT: [0-9]+ ;

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