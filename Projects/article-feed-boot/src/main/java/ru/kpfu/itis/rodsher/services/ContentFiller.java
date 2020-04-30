package ru.kpfu.itis.rodsher.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.kpfu.itis.rodsher.models.Country;
import ru.kpfu.itis.rodsher.repositories.CountriesRepository;

import java.util.List;

@Service
public class ContentFiller {
    @Autowired
    private CountriesRepository countriesRepository;

    public static final String EMPTY_COUNTRY_NAME = "-выберите-";

    public List<Country> getCountries() {
        final List<Country> countries = countriesRepository.findAll();
        countries.add(new Country(0, EMPTY_COUNTRY_NAME));
        return countries;
    }
}
