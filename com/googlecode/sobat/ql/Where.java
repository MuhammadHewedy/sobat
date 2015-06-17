package com.googlecode.sobat.ql;

import com.googlecode.sobat.ql.condition.Conditions;;

public class Where extends Clause {
	
	private Clause clause; 
	private Conditions conditions;
	
	public Where(Clause clause, Conditions conditions) {
		if (clause instanceof OrderBy)
			throw new RuntimeException("WHERE should be specified before ORDER BY Cluase");
		this.clause = clause;
		this.conditions = conditions;
	}
	public String getValue() {
		return clause.getValue() + (conditions == null ? "" : QueryConstants.WHERE + conditions.getCondition()) ;
	}

	public From getFrom() {
		return clause.getFrom();
	}

}
