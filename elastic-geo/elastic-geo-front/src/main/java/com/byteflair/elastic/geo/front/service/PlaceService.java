package com.byteflair.elastic.geo.front.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.camel.ProducerTemplate;
import org.apache.commons.lang.StringUtils;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.common.unit.DistanceUnit;
import org.elasticsearch.index.query.FilterBuilders;
import org.elasticsearch.index.query.GeoDistanceFilterBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.facet.FacetBuilder;
import org.elasticsearch.search.facet.FacetBuilders;
import org.elasticsearch.search.facet.terms.TermsFacetBuilder;
import org.elasticsearch.search.sort.GeoDistanceSortBuilder;
import org.elasticsearch.search.sort.SortBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.byteflair.elastic.geo.front.model.ElasticsearchResult;
import com.byteflair.elastic.geo.front.model.Place;
import com.byteflair.elastic.geo.front.util.SequenceGenerator;

@Service
public class PlaceService {
	@Autowired
	ProducerTemplate webProducerTemplate;
	
	public Place createPlace(Place place) {
		place.setId(SequenceGenerator.next());
		
		webProducerTemplate.sendBody("direct:elastic.index.place", place);
		
		return place;
	}
	
	public void loadPlaces() {
		Place puertaDelSol = new Place();
		
		puertaDelSol.setId(SequenceGenerator.next());
		puertaDelSol.setName("Puerta del Sol");
		puertaDelSol.setLocality("Madrid");
		puertaDelSol.setType("Monumento");
		puertaDelSol.setLat(new BigDecimal(40.416812));
		puertaDelSol.setLng(new BigDecimal(-3.703498));
		
		webProducerTemplate.sendBody("direct:elastic.index.place", puertaDelSol);
		
		Place lasBravas = new Place();
		
		lasBravas.setId(SequenceGenerator.next());
		lasBravas.setName("Las Bravas");
		lasBravas.setLocality("Madrid");
		lasBravas.setType("Bar");
		lasBravas.setLat(new BigDecimal(40.415111));
		lasBravas.setLng(new BigDecimal(-3.702317));
		
		webProducerTemplate.sendBody("direct:elastic.index.place", lasBravas);
		
		Place santiagoBernabeu = new Place();
		
		santiagoBernabeu.setId(SequenceGenerator.next());
		santiagoBernabeu.setName("Estadio Santiago Bernabeu");
		santiagoBernabeu.setLocality("Madrid");
		santiagoBernabeu.setType("Campo de fútbol");
		santiagoBernabeu.setLat(new BigDecimal(40.452662));
		santiagoBernabeu.setLng(new BigDecimal(-3.689561));
		
		webProducerTemplate.sendBody("direct:elastic.index.place", santiagoBernabeu);
		
		Place vicenteCalderon = new Place();
		
		vicenteCalderon.setId(SequenceGenerator.next());
		vicenteCalderon.setName("Estadio Vicente Calderón");
		vicenteCalderon.setLocality("Madrid");
		vicenteCalderon.setType("Campo de fútbol");
		vicenteCalderon.setLat(new BigDecimal(40.402042));
		vicenteCalderon.setLng(new BigDecimal(-3.719505));
		
		webProducerTemplate.sendBody("direct:elastic.index.place", vicenteCalderon);
	}
	
	@SuppressWarnings("unchecked")
	public ElasticsearchResult<Place> searchPlaces(String type, Double lat, Double lng, Double radius) {
		ElasticsearchResult<Place> foundPlaces = (ElasticsearchResult<Place>)webProducerTemplate.requestBody("direct:elastic.search.place", buildQuery(type, lat, lng, radius, getFacets(), 0, 25, getSort(lat, lng)));
		
		return foundPlaces;
	}
	
	private String buildQuery(String type, Double lat, Double lng, Double radius, List<FacetBuilder> facets, int start, int rows, SortBuilder sort){
		SearchRequestBuilder searchRequest = new SearchRequestBuilder(null).setSearchType(SearchType.DFS_QUERY_THEN_FETCH).setFrom(start).setSize(rows).setExplain(false);
		
		searchRequest.setQuery(getQuery(type, lat, lng, radius));

		searchRequest.addField("_source");
		
		if (lat != null && lat != 0.0 && lng != null && lng != 0.0) {
			Map<String, Object> scriptFieldParams = new HashMap<String, Object>();
			scriptFieldParams.put("lat", lat);
			scriptFieldParams.put("lng", lng);
			
			searchRequest.addScriptField("distance", "doc['location'].arcDistanceInKm(lat,lng)", scriptFieldParams);
		}
		
		if (facets != null) {
			for (FacetBuilder facet : facets) {
				searchRequest.addFacet(facet);
			}
		}
		
		if (sort != null) {
			searchRequest.addSort(sort);
		}
		
		return searchRequest.toString();
	}
	
	private QueryBuilder getQuery(String type, Double lat, Double lng, Double radius) {
		QueryBuilder query = null;
		
		if (StringUtils.isNotBlank(type)) {
			query = QueryBuilders.termQuery("type", type);
		} else {
			query = QueryBuilders.matchAllQuery();
		}
		
		if (lat != null && lat != 0.0 && lng != null && lng != 0.0) {
			GeoDistanceFilterBuilder filter = FilterBuilders.geoDistanceFilter("location");
			filter.point(lat, lng);
			
			if (radius != null) {
				filter.distance(radius, DistanceUnit.KILOMETERS);
			} else {
				filter.distance(5.0, DistanceUnit.KILOMETERS);
			}
			
			return QueryBuilders.filteredQuery(query, filter);
		} else {
			return query;
		}
	}
	
	private List<FacetBuilder> getFacets(){
		List<FacetBuilder> facets = new ArrayList<FacetBuilder>();
		
		FacetBuilder facet = null;

		facet = FacetBuilders.termsFacet("type");
		((TermsFacetBuilder)facet).field("type");
		
		facets.add(facet);
		
		facet = FacetBuilders.termsFacet("locality");
		((TermsFacetBuilder)facet).scriptField("_source['locality']");
		facets.add(facet);
		
		return facets;
	}
	
	private SortBuilder getSort(Double lat, Double lng) {
		if (lat != null && lat != 0.0 && lng != null && lng != 0.0) {
			GeoDistanceSortBuilder sort = SortBuilders.geoDistanceSort("location");
			sort.unit(DistanceUnit.KILOMETERS);
			sort.order(SortOrder.ASC);
			sort.point(lat, lng);
			
			return sort;
		} else {
			return null;
		}
	}
}