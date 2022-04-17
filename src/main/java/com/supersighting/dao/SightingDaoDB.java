package com.supersighting.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

import com.supersighting.model.Hero;
import com.supersighting.model.Location;
import com.supersighting.model.Organization;
import com.supersighting.model.Sighting;
import com.supersighting.model.Superpower;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

public class SightingDaoDB implements SightingDao{
    @Autowired
    JdbcTemplate jdbc;


    @Override
    public Sighting getSightingById(int id) {
        try {
            final String GET_SIGHTING_BY_ID = "SELECT * FROM sighting WHERE sightingId = ?";
            Sighting sighting =  jdbc.queryForObject(GET_SIGHTING_BY_ID, new SightingMapper(), id);
            sighting.setLocation(getLocationForSighting(id));
            sighting.setHero(getHeroForSighting(id));
            return sighting;

        } catch(DataAccessException e) {
            return null;
        }
    }


    private Location getLocationForSighting(int sightingId){
        final String SELECT_LOCATION_FOR_SIGHTING = "SELECT l.* from sighting st join location l " +
                "ON st.locationId = l.locationId " +
                "WHERE st.sightingId = ?";
        return jdbc.queryForObject(SELECT_LOCATION_FOR_SIGHTING, new LocationDaoDB.LocationMapper(), sightingId);
    }

    private Hero getHeroForSighting(int sightingId){
        final String SELECT_HERO_FOR_SIGHTING = "SELECT h.* from sighting st join hero h " +
                "ON st.heroId = h.heroId " +
                "WHERE st.sightingId = ?";
        Hero hero = jdbc.queryForObject(SELECT_HERO_FOR_SIGHTING, new HeroDaoDB.HeroMapper(), sightingId);
        hero.setSuperpower(getSuperpowerForHero(hero.getId()));
        hero.setOrganizations(getOrganizationsForHero(hero.getId()));
        return hero;
    }


    private Superpower getSuperpowerForHero(int heroId){
        final String SELECT_SUPERPOWER_FOR_HERO = "SELECT s.* from hero h join superpower s " +
                "ON h.superpowerId = s.superpowerId " +
                "WHERE h.heroId = ?";
        return jdbc.queryForObject(SELECT_SUPERPOWER_FOR_HERO, new SuperpowerDaoDB.SuperpowerMapper(), heroId);
    }

    private List<Organization> getOrganizationsForHero(int heroId){
        final String SELECT_ORGANIZATION_FOR_HERO = "SELECT o.* from hero h " +
                "join hero_organization ho " +
                "on h.heroId = ho.heroId " +
                "join organization o " +
                "on ho.organizationId = o.organizationId " +
                "where h.heroId = ?";
        return jdbc.query(SELECT_ORGANIZATION_FOR_HERO, new OrganizationDaoDB.OrganizationMapper(), heroId);
    }

    @Override
    public List<Sighting> getAllSightings() {
        final String GET_ALL_SIGHTINGS = "SELECT * FROM sighting";
        List<Sighting> sightings = jdbc.query(GET_ALL_SIGHTINGS, new SightingMapper());
        for (Sighting sighting : sightings){
            sighting.setLocation(getLocationForSighting(sighting.getId()));
            sighting.setHero(getHeroForSighting(sighting.getId()));
        }
        return sightings;
    }

    @Override
    public Sighting addSighting(Sighting sighting) {
        final String INSERT_SIGHTING = "INSERT INTO sighting(date, locationId, heroId) " +
                "VALUES(?,?,?)";
        jdbc.update(INSERT_SIGHTING,
                sighting.getDate(),
                sighting.getLocation().getId(),
                sighting.getHero().getId()
        );

        int newId = jdbc.queryForObject("SELECT LAST_INSERT_ID()", Integer.class);
        sighting.setId(newId);
        return sighting;
    }

    @Override
    public void updateSighting(Sighting sighting) {
        final String UPDATE_SIGHTING = "UPDATE sighting SET date = ?, locationId = ?, " +
                "heroId = ? WHERE sightingId = ?";
        jdbc.update(UPDATE_SIGHTING,
                sighting.getDate(),
                sighting.getLocation().getId(),
                sighting.getHero().getId(),
                sighting.getId());
    }

    @Override
    public void deleteSightingById(int id) {
        final String DELETE_SIGHTING = "DELETE FROM sighting WHERE sightingId = ?";
        jdbc.update(DELETE_SIGHTING, id);
    }

    @Override
    public List<Sighting> getSightingsByDate(LocalDate date) {
        final String GET_SIGHTINGS_BY_DATE = "select * from sighting where date = ?";
        List<Sighting> sightings = jdbc.query(GET_SIGHTINGS_BY_DATE, new SightingMapper(), date);
        for (Sighting sighting : sightings){
            sighting.setLocation(getLocationForSighting(sighting.getId()));
            sighting.setHero(getHeroForSighting(sighting.getId()));
        }

        return sightings;
    }

    @Override
    public List<Sighting> getLatestSightings() {
        final String GET_SIGHTINGS_BY_DATE = "select * from sighting order by date desc limit 10";
        List<Sighting> sightings = jdbc.query(GET_SIGHTINGS_BY_DATE, new SightingMapper());
        for (Sighting sighting : sightings){
            sighting.setLocation(getLocationForSighting(sighting.getId()));
            sighting.setHero(getHeroForSighting(sighting.getId()));
        }
        return sightings;
    }

    public static final class SightingMapper implements RowMapper<Sighting> {

        @Override
        public Sighting mapRow(ResultSet rs, int index) throws SQLException {
            Sighting sighting = new Sighting();
            sighting.setId(rs.getInt("sightingId"));
            if (rs.getString("date") != null){
                sighting.setDate(rs.getDate("date").toLocalDate());
            }

            return sighting;
        }
    }
}
