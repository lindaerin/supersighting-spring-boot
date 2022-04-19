package com.supersighting.controller;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.supersighting.dao.HeroDao;
import com.supersighting.dao.LocationDao;
import com.supersighting.dao.SightingDao;
import com.supersighting.model.Hero;
import com.supersighting.model.Location;
import com.supersighting.model.Sighting;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class SightingController {
    @Autowired
    HeroDao heroDao;

    @Autowired
    LocationDao locationDao;

    @Autowired
    SightingDao sightingDao;


    @GetMapping("sightings")
    public String displaySightings(Model model) {

        List<Sighting> sightings = sightingDao.getAllSightings();
        List<Location> locations = locationDao.getAllLocations();
        List<Hero> heroes = heroDao.getAllHero();
        model.addAttribute("sightings", sightings);
        model.addAttribute("locations", locations);
        model.addAttribute("heroes", heroes);
        return "sightings";
    }

    @PostMapping("addSighting")
    public String addSighting(Sighting sighting, HttpServletRequest request) {
        String locationId = request.getParameter("locationId");
        String heroId = request.getParameter("heroId");
        String dateString = request.getParameter("dateString");

        sighting.setLocation(locationDao.getLocationById(Integer.parseInt(locationId)));
        sighting.setHero(heroDao.getHeroById(Integer.parseInt(heroId)));
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        sighting.setDate(LocalDate.parse(dateString, formatter));
        sightingDao.addSighting(sighting);

        return "redirect:/sightings";
    }

    @GetMapping("sightingDetail")
    public String sightingDetail(Integer id, Model model) { 
        Sighting sighting = sightingDao.getSightingById(id);
        model.addAttribute("sighting", sighting);
        return "sightingDetail";
    }

    @GetMapping("deleteSighting")
    public String deleteSighting(Integer id) {
        sightingDao.deleteSightingById(id);
        return "redirect:/sightings";
    }

    @GetMapping("editSighting")
    public String editSuperpower(HttpServletRequest request, Model model) {
        int id = Integer.parseInt(request.getParameter("id"));
        Sighting sighting = sightingDao.getSightingById(id);
        model.addAttribute("sighting", sighting);

        return "editSuperpower";
    }

    @PostMapping("editSighting")
    public String performEditSuperpower(HttpServletRequest request) {
        int id = Integer.parseInt(request.getParameter("id"));
        Sighting sighting = sightingDao.getSightingById(id);

        // update (?)
        sightingDao.updateSighting(sighting);
        return "redirect:/sightings";
    }

}
