package com.supersighting.controller;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.Valid;
import javax.validation.Validation;
import javax.validation.Validator;

import com.supersighting.dao.HeroDao;
import com.supersighting.dao.OrganizationDao;
import com.supersighting.dao.SuperpowerDao;
import com.supersighting.model.Hero;
import com.supersighting.model.Organization;
import com.supersighting.model.Superpower;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class HeroController {
    @Autowired
    HeroDao heroDao;

    @Autowired
    SuperpowerDao superpowerDao;

    @Autowired
    OrganizationDao organizationDao;

    Set<ConstraintViolation<Hero>> violations = new HashSet<>();
    Validator validate = Validation.buildDefaultValidatorFactory().getValidator();

    @GetMapping("heroes")
    public String displayAllSuperpower(Model model) {

        List<Hero> heroes = heroDao.getAllHero();
        List<Organization> organizations = organizationDao.getAllOrganizations();
        List<Superpower> superpowers = superpowerDao.getAllSuperpower();
        model.addAttribute("heroes", heroes);
        model.addAttribute("organizations", organizations);
        model.addAttribute("superpowers", superpowers);

        return "heroes";
    }

    @PostMapping("addHero")
    public String addHero(Hero hero, BindingResult result, HttpServletRequest request) {
        String superpowerId = request.getParameter("superpowerId");
        String[] organizationIds = request.getParameterValues("organizationId");

        hero.setSuperpower(superpowerDao.getSuperpowerById(Integer.parseInt(superpowerId)));

        List<Organization> organizations = new ArrayList<>();

        for (String organizationId : organizationIds) {
            organizations.add(organizationDao.getOrganizationById(Integer.parseInt(organizationId)));
        }

        hero.setOrganizations(organizations);

        violations = validate.validate(hero);

        if (violations.isEmpty()) {
            heroDao.addHero(hero);
        }

        return "redirect:/heroes";
    }

    @GetMapping("heroDetail")
    public String heroDetail(Integer id, Model model) {
        Hero hero = heroDao.getHeroById(id);
        model.addAttribute("hero", hero);
        return "heroDetail";
    }

    @GetMapping("deleteHero")
    public String deleteHero(Integer id) {
        heroDao.deleteHeroById(id);
        return "redirect:/heroes";
    }

    @GetMapping("editHero")
    public String editHero(Integer id, Model model) {
        Hero hero = heroDao.getHeroById(id);
        List<Organization> organizations = organizationDao.getAllOrganizations();
        List<Superpower> superpowers = superpowerDao.getAllSuperpower();
        model.addAttribute("hero", hero);
        model.addAttribute("organizations", organizations);
        model.addAttribute("superpowers", superpowers);
        return "editHero";
    }

    @PostMapping("editHero")
    public String performEditHero(@Valid Hero hero, BindingResult result, HttpServletRequest request, Model model) {
        String superpowerId = request.getParameter("superpowerId");
        String[] organizationIds = request.getParameterValues("organizationId");

        hero.setSuperpower(superpowerDao.getSuperpowerById(Integer.parseInt(superpowerId)));

        List<Organization> organizations = new ArrayList<>();
        if (organizationIds != null) {
            for (String organizationId : organizationIds) {
                organizations.add(organizationDao.getOrganizationById(Integer.parseInt(organizationId)));
            }
        } else {

            FieldError error = new FieldError("hero", "organizations", "Must include one organization");
            result.addError(error);
        }
        hero.setOrganizations(organizations);

        if (result.hasErrors()) {
            model.addAttribute("superpowers", superpowerDao.getAllSuperpower());
            model.addAttribute("organizations", organizationDao.getAllOrganizations());
            model.addAttribute("hero", hero);
            return "editHero";
        }

        heroDao.updateHero(hero);

        return "redirect:/heroes";
    }

}
