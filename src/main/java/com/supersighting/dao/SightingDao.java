package com.supersighting.dao;

import java.time.LocalDate;
import java.util.List;

import com.supersighting.model.Sighting;

public interface SightingDao {
    Sighting getSightingById(int id);
    List<Sighting> getAllSightings();
    Sighting addSighting(Sighting sighting);
    void updateSighting(Sighting sighting);
    void deleteSightingById(int id);

    List<Sighting> getSightingsByDate(LocalDate date);
    List<Sighting> getLatestSightings();
}
