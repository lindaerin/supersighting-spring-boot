package com.supersighting.dao;

import java.util.List;

import com.supersighting.model.Superpower;

public interface SuperpowerDao {
    Superpower getSuperpowerById(int id);
    List<Superpower> getAllSuperpower();
    Superpower addSuperpower(Superpower superpower);
    void updateSuperpower(Superpower superpower);
    void deleteSuperpowerById(int id);
}
