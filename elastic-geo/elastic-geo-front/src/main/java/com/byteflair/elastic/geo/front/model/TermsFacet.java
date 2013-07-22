package com.byteflair.elastic.geo.front.model;

import java.io.Serializable;
import java.util.Map;

public class TermsFacet implements Serializable {
	private static final long serialVersionUID = -1146660391350442009L;

	private String field;
	private Map<String, Integer> terms;

	public String getField() {
		return field;
	}

	public void setField(String field) {
		this.field = field;
	}

	public Map<String, Integer> getTerms() {
		return terms;
	}

	public void setTerms(Map<String, Integer> terms) {
		this.terms = terms;
	}
}