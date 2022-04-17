package com.supersighting.controller;

import java.util.List;

import com.supersighting.dao.SightingDao;
import com.supersighting.model.Sighting;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @Autowired
    SightingDao sightingDao;

    @GetMapping("/")
    public String displayHomepage(Model model) {
        List<Sighting> lastTenSightings = sightingDao.getLatestSightings();
        model.addAttribute("recentSightings", lastTenSightings);
        return "home.html";
    }
}
