package com.byteflair.elastic.geo.front.service;

import java.math.BigDecimal;

import org.apache.camel.ProducerTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
}