package com.supersighting.dao;

import java.util.List;

import com.supersighting.model.Hero;
import com.supersighting.model.Organization;

public interface OrganizationDao {
    Organization getOrganizationById(int id);
    List<Organization> getAllOrganizations();
    Organization addOrganization(Organization organization);
    void updateOrganization(Organization organization);
    void deleteOrganizationById(int id);

    List<Organization> getOrganizationByHero(Hero hero);
}

