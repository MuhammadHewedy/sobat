package com.googlecode.sobat.ql.condition;

/**
<search_condition> ::=
{ [ NOT ] <predicate> | ( <search_condition> ) }
[ {AND | OR} [NOT] {<predicate> | ( <search_condition> ) } ]
} [,...n]
 
 <predicate> ::=
{
expression { = | <> | > | >= | < | <= } expression
| string_expression [NOT] LIKE string_expression
[ESCAPE ’escape_character’]
| expression [NOT] BETWEEN expression AND expression
| expression IS [NOT] NULL
| CONTAINS
( {column | * }, ’<contains_search_condition>’ )
| FREETEXT ( {column | * }, ’freetext_string’ )
| expression [NOT] IN (subquery | expression [,...n])
| expression { = | <> | > | >= | < | <= }
{ALL | SOME | ANY} (subquery)
| EXISTS (subquery)
}
 */
abstract class Condition{

	protected String condition;
	
	Condition(String property, Object value) {
		if (value instanceof String)
			value = "'" + value + "'";
		condition = buildCondition(property, value);
	}
		
	String getCondition() {
		return condition;
	}
	
	protected abstract String buildCondition(String columnName, Object value);
}
