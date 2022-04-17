package com.supersighting.dao;

import java.util.List;

import com.supersighting.model.Hero;
import com.supersighting.model.Location;

public interface LocationDao {
    Location getLocationById(int id);
    List<Location> getAllLocations();
    Location addLocation(Location location);
    void updateLocation(Location location);
    void deleteLocationById(int id);

    List<Location> getAllLocationsForHero(Hero hero);
}
