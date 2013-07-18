package com.byteflair.elastic.geo.front.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.byteflair.elastic.geo.front.model.Place;
import com.byteflair.elastic.geo.front.service.PlaceService;

@Controller
public class PlaceController {
	@Autowired
	PlaceService placeService;
	
	@RequestMapping(method = RequestMethod.GET)
	public String home() {
		return "home";
	}
	
	@RequestMapping(value = "/create-place", method = RequestMethod.GET)
	public String createPlace(Model model) {
		model.addAttribute("newPlace", new Place());
		
		return "createPlace";
	}
	
	@RequestMapping(value = "/create-place", method = RequestMethod.POST)
	public String createPlace(Place place, RedirectAttributes redirectAttributes) {
		Place createdPlace = placeService.createPlace(place);
		
		redirectAttributes.addFlashAttribute("createdPlace", createdPlace);
		
		return "redirect:/created-place";
	}
	
	@RequestMapping(value = "/created-place", method = RequestMethod.GET)
	public String createdPlace(Model model) {
		return "placeCreated";
	}
	
	@RequestMapping(value = "/load-places", method = RequestMethod.GET)
	public String loadPlaces() {
		placeService.loadPlaces();
		
		return "redirect:/";
	}
}