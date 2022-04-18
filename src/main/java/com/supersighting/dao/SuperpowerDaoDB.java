package com.supersighting.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import com.supersighting.model.Superpower;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class SuperpowerDaoDB implements SuperpowerDao {

    @Autowired
    JdbcTemplate jdbc;


    @Override
    public Superpower getSuperpowerById(int id) {

        try {
            
            final String SELECT_SUPERPOWER_BY_ID = "SELECT * FROM superpower WHERE superpowerId = ?";
            return jdbc.queryForObject(SELECT_SUPERPOWER_BY_ID, new SuperpowerMapper(), id);

        } catch (DataAccessException e) {
            return null;
        }
    }

    @Override
    public List<Superpower> getAllSuperpower() {

        final String SELECT_ALL_SUPERPOWERS = "SELECT * FROM superpower";
        return jdbc.query(SELECT_ALL_SUPERPOWERS, new SuperpowerMapper());
    
    }

    @Override
    @Transactional
    public Superpower addSuperpower(Superpower superpower) {
        
        final String INSERT_SUPERPOWER = "INSERT INTO superpower(superpowerName) VALUES(?)";
        jdbc.update(INSERT_SUPERPOWER, superpower.getName());

        int newId = jdbc.queryForObject("SELECT LAST_INSERT_ID()", Integer.class);
        superpower.setId(newId);

        return superpower;
    }

    @Override
    public void updateSuperpower(Superpower superpower) {
        final String UPDATE_SUPERPOWER = "UPDATE superpower SET name = ? WHERE id = ?";
        jdbc.update(UPDATE_SUPERPOWER, superpower.getName(), superpower.getId());
    }

    @Override
    @Transactional
    public void deleteSuperpowerById(int id) {
        // delete hero in hero_organization with superpower = id
        final String DELETE_HERO_ORG = "DELETE ho.* FROM hero_organization ho " + "JOIN hero h on ho.heroId = h.heroId "
                + "JOIN superpower s on h.superpowerId = s.superpowerId WHERE s.superpowerId = ?";
        jdbc.update(DELETE_HERO_ORG, id);

        // delete hero in sighting with superpower = id
        final String DELETE_SIGHTING = "DELETE sg.* FROM sighting sg " + "JOIN hero h ON sg.heroId = h.heroId "
                + "JOIN superpower s on h.superpowerId = s.superpowerId WHERE s.superpowerId = ?";
        jdbc.update(DELETE_SIGHTING, id);

        // delete hero in sighting with superpower = id
        final String DELETE_HERO = "DELETE FROM hero WHERE superpowerId = ?";
        jdbc.update(DELETE_HERO, id);

        // delete superpower
        final String DELETE_SUPERPOWER = "DELETE FROM superpower WHERE superpowerId = ?";
        jdbc.update(DELETE_SUPERPOWER, id);
    }

    public static final class SuperpowerMapper implements RowMapper<Superpower> {

        @Override
        public Superpower mapRow(ResultSet rs, int index) throws SQLException {
            Superpower superpower = new Superpower();
            superpower.setId(rs.getInt("superpowerId"));
            superpower.setName(rs.getString("superpowerName"));
            return superpower;
        }

    }

}
