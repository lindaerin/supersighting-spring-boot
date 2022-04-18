package com.supersighting.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import com.supersighting.dao.OrganizationDaoDB.OrganizationMapper;
import com.supersighting.dao.SuperpowerDaoDB.SuperpowerMapper;
import com.supersighting.model.Hero;
import com.supersighting.model.Location;
import com.supersighting.model.Organization;
import com.supersighting.model.Superpower;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class HeroDaoDB implements HeroDao {

    @Autowired
    JdbcTemplate jdbc;

    @Override
    public Hero getHeroById(int id) {

        try {
            final String SELECT_HERO_BY_ID = "SELECT * FROM hero WHERE heroId = ?";
            return jdbc.queryForObject(SELECT_HERO_BY_ID, new HeroMapper(), id);
        } catch (DataAccessException e) {
            return null;
        }
    }

    @Override
    public List<Hero> getAllHero() {
        final String SELECT_ALL_HERO = "SELECT * FROM hero";

        List<Hero> heroes = jdbc.query(SELECT_ALL_HERO, new HeroMapper());
        for (Hero hero : heroes) {
            hero.setSuperpower(getSuperpowerForHero(hero.getId()));
            hero.setOrganizations(getOrganizationsForHero(hero.getId()));
        }
        return heroes;
    }

    private List<Organization> getOrganizationsForHero(int heroId) {
        final String SELECT_ORGANIZATION_FOR_HERO = "SELECT o.* FROM hero h JOIN hero_organization ho " +
                "ON h.heroId = ho.heroId JOIN organization o ON ho.organizationId = o.organizationId " +
                "WHERE h.heroId = ?";
        return jdbc.query(SELECT_ORGANIZATION_FOR_HERO, new OrganizationMapper(), heroId);
    }

    private Superpower getSuperpowerForHero(int heroId) {
        final String SELECT_SUPERPOWER_FOR_HERO = "SELECT s.* FROM hero h JOIN superpower s ON " + 
        "h.superpowerId = s.superpowerId WHERE h.heroId = ?";
        return jdbc.queryForObject(SELECT_SUPERPOWER_FOR_HERO, new SuperpowerMapper(), heroId);
    }

    @Override
    @Transactional
    public Hero addHero(Hero hero) {
        final String INSERT_HERO = "INSERT INTO hero(heroName, heroDescription, superpowerId) VALUES(?,?,?)";
        jdbc.update(INSERT_HERO,
                hero.getName(),
                hero.getDescription(),
                hero.getSuperpower().getId());

        int newId = jdbc.queryForObject("SELECT LAST_INSERT_ID()", Integer.class);
        hero.setId(newId);
        insertHeroOrganization(hero);       
        return hero;
    }

    private void insertHeroOrganization(Hero hero) {
        final String INSERT_HERO_ORGANIZATION = "INSERT into hero_organization(heroId, organizationId) VALUES(?, ?)";
        for (Organization organization : hero.getOrganizations()) {
            jdbc.update(INSERT_HERO_ORGANIZATION, hero.getId(), organization.getId());
        }
    }

    @Override
    public void updateHero(Hero hero) {
        final String UPDATE_HERO = "UPDATE hero SET heroName = ?, heroDescription = ?, " +
                "superpowerId = ? WHERE heroId = ?";
        jdbc.update(UPDATE_HERO,
                hero.getName(),
                hero.getDescription(),
                hero.getSuperpower().getId(),
                hero.getId());
        final String DELETE_HERO_ORGANIZATION = "DELETE FROM hero_organization WHERE heroId = ?";
        jdbc.update(DELETE_HERO_ORGANIZATION, hero.getId());
        insertHeroOrganization(hero);
    }

    @Override
    @Transactional
    public void deleteHeroById(int id) {

        final String DELETE_HERO_ORGANIZATION = "DELETE FROM hero_organization WHERE heroId = ?";
        jdbc.update(DELETE_HERO_ORGANIZATION, id);

        final String DELETE_SIGHTING = "DELETE FROM sighting WHERE heroId = ?";
        jdbc.update(DELETE_SIGHTING, id);

        final String DELETE_HERO = "DELETE FROM hero WHERE heroId = ?";
        jdbc.update(DELETE_HERO, id);
    }

    @Override
    public List<Hero> getAllMembersForOrganization(Organization organization) {
        final String SELECT_ALL_MEMBERS_FOR_ORG = "SELECT DISTINCT h.* FROM hero h JOIN hero_organization ho ON h.heroId = ho.heroId WHERE ho.organizationId = ?";
        return jdbc.query(SELECT_ALL_MEMBERS_FOR_ORG, new HeroMapper(), organization.getId());
    }

    @Override
    public List<Hero> getAllHeroAtSightedLocation(Location location) {
        final String GET_ALL_HEROES_SIGHTED_AT_LOCATION = "SELECT DISTINCT h.* " +
                "FROM hero h " +
                "JOIN sighting st " +
                "ON h.heroId = st.heroId " +
                "WHERE st.locationId = ?";
        List<Hero> heroes = jdbc.query(GET_ALL_HEROES_SIGHTED_AT_LOCATION, new HeroMapper(), location.getId());
        for (Hero hero : heroes) {
            hero.setSuperpower(getSuperpowerForHero(hero.getId()));
            hero.setOrganizations(getOrganizationsForHero(hero.getId()));
        }
        return heroes;
    }

    public static final class HeroMapper implements RowMapper<Hero> {

        @Override
        public Hero mapRow(ResultSet rs, int index) throws SQLException {
            Hero hero = new Hero();
            hero.setId(rs.getInt("heroId"));
            hero.setName(rs.getString("heroName"));
            hero.setDescription(rs.getString("heroDescription"));
            return hero;
        }

    }

}
