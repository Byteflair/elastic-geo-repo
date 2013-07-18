package com.byteflair.elastic.geo.front.i9n;

import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.camel.Exchange;
import org.apache.camel.spi.DataFormat;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;

import com.byteflair.elastic.geo.front.model.Place;

public class PlaceListJsonDataFormat implements DataFormat {

	@Autowired
	private ObjectMapper om;
	
	@Override
	public void marshal(Exchange exchange, Object graph, OutputStream stream) throws Exception {
	}

	@Override
	public Object unmarshal(Exchange exchange, InputStream stream) throws Exception {
		String response = exchange.getOut().getBody(String.class);
		
		List<Place> places = new ArrayList<Place>();
		
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
			
			places.add(place);
		}
		
		return places;
	}
}