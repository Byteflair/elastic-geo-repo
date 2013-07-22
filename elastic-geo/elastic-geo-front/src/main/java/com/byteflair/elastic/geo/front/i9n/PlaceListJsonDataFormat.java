package com.byteflair.elastic.geo.front.i9n;

import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.camel.Exchange;
import org.apache.camel.spi.DataFormat;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;

import com.byteflair.elastic.geo.front.model.ElasticsearchResult;
import com.byteflair.elastic.geo.front.model.Place;
import com.byteflair.elastic.geo.front.model.TermsFacet;

public class PlaceListJsonDataFormat implements DataFormat {

	@Autowired
	private ObjectMapper om;
	
	@Override
	public void marshal(Exchange exchange, Object graph, OutputStream stream) throws Exception {
	}

	@Override
	public Object unmarshal(Exchange exchange, InputStream stream) throws Exception {
		String response = exchange.getOut().getBody(String.class);
		
		ElasticsearchResult<Place> result = new ElasticsearchResult<Place>();
		
		JsonNode rootNode = om.readTree(response);
		
		Iterator<JsonNode> hits = rootNode.get("hits").get("hits").getElements();
		
		while (hits.hasNext()) {
			JsonNode hit = hits.next();
			JsonNode hitSource = hit.get("_source");
			
			Place place = new Place();
			
			place.setId(hitSource.get("id").asLong());
			place.setName(hitSource.get("name").asText());
			place.setType(hitSource.get("type").asText());
			place.setLocality(hitSource.get("locality").asText());
			
			place.setLat(new BigDecimal(hitSource.get("location").get(0).asText()));
			place.setLng(new BigDecimal(hitSource.get("location").get(1).asText()));
			
			JsonNode hitFields = hit.get("fields");
			
			if (hitFields != null) {
				place.setDistance(new BigDecimal(hitFields.get("distance").asText()));
			}
			
			result.addElement(place);
		}
		
		JsonNode facets = rootNode.get("facets");
		
		if (facets != null) {
			//Type facet
			JsonNode typeFacet = facets.get("type");
			
			TermsFacet typeTermsFacet = new TermsFacet();
			typeTermsFacet.setField("Tipo");
			
			Iterator<JsonNode> terms = typeFacet.get("terms").getElements();
			
			Map<String, Integer> typeTerms = new HashMap<String, Integer>();
			
			while (terms.hasNext()) {
				JsonNode term = terms.next();
				
				typeTerms.put(term.get("term").asText(), term.get("count").asInt());
			}
			
			typeTermsFacet.setTerms(typeTerms);
			
			result.addFacet(typeTermsFacet);
			
			//Locality facet
			JsonNode localityFacet = facets.get("locality");
			
			TermsFacet localityTermsFacet = new TermsFacet();
			localityTermsFacet.setField("Localidad");
			
			terms = localityFacet.get("terms").getElements();
			
			typeTerms = new HashMap<String, Integer>();
			
			while (terms.hasNext()) {
				JsonNode term = terms.next();
				
				typeTerms.put(term.get("term").asText(), term.get("count").asInt());
			}
			
			localityTermsFacet.setTerms(typeTerms);
			
			result.addFacet(localityTermsFacet);
		}
		
		return result;
	}
}