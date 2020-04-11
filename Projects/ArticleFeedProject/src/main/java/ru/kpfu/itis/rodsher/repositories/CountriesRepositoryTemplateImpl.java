package ru.kpfu.itis.rodsher.repositories;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.kpfu.itis.rodsher.models.Country;

import java.util.List;
import java.util.Optional;

@Repository
@Profile("template")
public class CountriesRepositoryTemplateImpl implements CountriesRepository {
    private static final String SQL_SELECT_ALL = "SELECT * FROM public.\"countries\" ORDER BY name_ru DESC";
    private static final String SQL_SELECT_BY_ID = "SELECT * FROM public.\"countries\" WHERE id = (?)";
    private static final String SQL_SELECT_BY_NAME_RU = "SELECT * FROM public.\"countries\" WHERE name_ru = (?)";

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private RowMapper<Country> countryRowMapper = (row, rowNumber) ->
            Country.builder()
                    .id(row.getInt("id"))
                    .nameRu(row.getString("name_ru"))
                    .build();


    @Override
    public Optional<Country> findById(int id) {
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(SQL_SELECT_BY_ID, new Object[]{id}, countryRowMapper));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<Country> findAll() {
        return jdbcTemplate.query(SQL_SELECT_ALL, countryRowMapper);
    }

    @Override
    public Optional<Country> findByNameRu(String nameRu) {
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(SQL_SELECT_BY_NAME_RU, new Object[]{nameRu}, countryRowMapper));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }
}