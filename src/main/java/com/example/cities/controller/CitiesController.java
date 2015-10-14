package com.example.cities.controller;


import com.example.cities.client.model.FormInput;
import com.example.cities.client.model.PagedCities;
import com.example.cities.client.repositories.CityRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class CitiesController {
    private CityRepository repository;

    @Autowired
    public CitiesController(CityRepository repository) {
        this.repository = repository;
    }

    @RequestMapping(value="/", method = RequestMethod.GET)
    public String index(Model uiModel,Pageable pageable) {
    	PagedCities cities = list(uiModel,pageable);
    	uiModel.addAttribute("pagedCities",cities);
    	uiModel.addAttribute("formInput",new FormInput("",cities.getMetadata().getSize()));
        return "index";
    }
    @RequestMapping(value="/", params={"name","size"}, method = RequestMethod.GET)
    public String search(@RequestParam("name") String name, @RequestParam("size") Long size, Model uiModel,Pageable pageable) {
    	PagedCities cities = search(name,pageable);
    	uiModel.addAttribute("pagedCities",cities);
    	FormInput input = new FormInput(name,size);
    	uiModel.addAttribute("formInput",input);
        return "index";
    }
    

    @RequestMapping(value="/cities/", method = RequestMethod.GET)
    public PagedCities list(Model uiModel,Pageable pageable) {
    	PagedCities cities = repository.findAll(pageable.getPageNumber(), pageable.getPageSize());
    	uiModel.addAttribute("pagedCities",cities);
        return cities;
    }

    @RequestMapping(value = "/cities/search", method = RequestMethod.GET)
    public PagedCities search(@RequestParam("name") String name, Pageable pageable) {
        return repository.findByNameContains(name, pageable.getPageNumber(), pageable.getPageSize());
    }
    
}
