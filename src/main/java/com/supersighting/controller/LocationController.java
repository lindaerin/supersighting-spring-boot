package com.supersighting.controller;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;

import com.supersighting.dao.LocationDao;
import com.supersighting.model.Location;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class LocationController {

    @Autowired
    LocationDao locationDao;

    Set<ConstraintViolation<Location>> violations = new HashSet<>();
    Validator validate = Validation.buildDefaultValidatorFactory().getValidator();


    @GetMapping("locations")
    public String displayLocations(Model model) {
        List<Location> locations = locationDao.getAllLocations();
        model.addAttribute("locations", locations);
        return "locations";
    }

    @PostMapping("addLocation")
    public String addLocation(Location location, HttpServletRequest request) {

        violations = validate.validate(location);

        if(violations.isEmpty()){
            locationDao.addLocation(location);
        }

        return "redirect:/locations";
    }

    @GetMapping("deleteLocation")
    public String deleteLocation(Integer id) {
        locationDao.deleteLocationById(id);
        return "redirect:/locations";
    }

    @GetMapping("locationDetail")
    public String locationDetail(Integer id, Model model) { 
        Location location = locationDao.getLocationById(id);
        model.addAttribute("location", location);
        return "locationDetail";
    }


    @GetMapping("editLocation")
    public String editHero(Integer id, Model model) {
        Location location = locationDao.getLocationById(id);
        model.addAttribute("location", location);

        return "editLocation";
    }

    

}
