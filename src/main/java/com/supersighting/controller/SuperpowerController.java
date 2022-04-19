package com.supersighting.controller;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;

import com.supersighting.dao.SuperpowerDao;
import com.supersighting.model.Superpower;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class SuperpowerController {

    @Autowired
    SuperpowerDao superpowerDao;

    Set<ConstraintViolation<Superpower>> violations = new HashSet<>();
    Validator validate = Validation.buildDefaultValidatorFactory().getValidator();

    @GetMapping("superpowers")
    public String displayAllSuperpower(Model model) {
        List<Superpower> superpowers = superpowerDao.getAllSuperpower();
        model.addAttribute("errors", violations);
        model.addAttribute("superpowers", superpowers);
        return "superpowers";
    }

    @PostMapping("addSuperpower")
    public String addSuperpower(HttpServletRequest request) {
        String name = request.getParameter("name");

        Superpower superpower = new Superpower();
        superpower.setName(name);

        violations = validate.validate(superpower);

        if (violations.isEmpty()) {
            superpowerDao.addSuperpower(superpower);
        }
        
        return "redirect:/superpowers";
    }

    @GetMapping("deleteSuperpower")
    public String deleteSuperpower(int id) {
        superpowerDao.deleteSuperpowerById(id);
        return "redirect:/superpowers";
    }

    @GetMapping("editSuperpower")
    public String editSuperpower(HttpServletRequest request, Model model) {
        int id = Integer.parseInt(request.getParameter("id"));
        Superpower superpower = superpowerDao.getSuperpowerById(id);
        model.addAttribute("superpower", superpower);

        return "editSuperpower";
    }

    @PostMapping("editSuperpower")
    public String performEditSuperpower(HttpServletRequest request) {
        int id = Integer.parseInt(request.getParameter("id"));
        Superpower superpower = superpowerDao.getSuperpowerById(id);
        superpower.setName(request.getParameter("name"));
        superpowerDao.updateSuperpower(superpower);
        return "redirect:/superpowers";
    }
}
