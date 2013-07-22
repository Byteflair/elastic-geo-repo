package com.byteflair.elastic.geo.front.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ElasticsearchResult<T> implements Serializable {
	private static final long serialVersionUID = -1587178505643688661L;

	private List<T> list;
	private List<TermsFacet> facets;

	public List<T> getList() {
		return list;
	}

	public void setList(List<T> list) {
		this.list = list;
	}

	public List<TermsFacet> getFacets() {
		return facets;
	}

	public void setFacets(List<TermsFacet> facets) {
		this.facets = facets;
	}
	
	public void addElement(T element) {
		if (list == null) {
			list = new ArrayList<T>();
		}
		
		list.add(element);
	}
	
	public void addFacet(TermsFacet facet) {
		if (facets == null) {
			facets = new ArrayList<TermsFacet>();
		}
		
		facets.add(facet);
	}
}