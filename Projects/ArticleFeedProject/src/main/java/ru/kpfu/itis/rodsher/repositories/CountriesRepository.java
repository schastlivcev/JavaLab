package ru.kpfu.itis.rodsher.repositories;

import ru.kpfu.itis.rodsher.models.Country;

import java.util.List;
import java.util.Optional;

public interface CountriesRepository {
    Optional<Country> findById(int id);
    List<Country> findAll();
    Optional<Country> findByNameRu(String nameRu);
}
