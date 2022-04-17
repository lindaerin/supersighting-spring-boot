package com.supersighting.dao;

import java.util.List;

import com.supersighting.model.Hero;
import com.supersighting.model.Location;
import com.supersighting.model.Organization;

public interface HeroDao {
    Hero getHeroById(int id);
    List<Hero> getAllHero();
    Hero addHero(Hero hero);
    void updateHero(Hero hero);
    void deleteHeroById(int id);

    List<Hero> getAllHeroAtSightedLocation(Location location);
    List<Hero> getAllMembersForOrganization(Organization organization);
}
