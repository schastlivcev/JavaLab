package ru.kpfu.itis.rodsher.repositories;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.kpfu.itis.rodsher.models.Country;
import ru.kpfu.itis.rodsher.models.Role;
import ru.kpfu.itis.rodsher.models.User;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.Types;
import java.util.List;
import java.util.Optional;

@Repository
@Profile("template")
public class UsersRepositoryTemplateImpl implements UsersRepository {
    private static final String SQL_INSERT = "INSERT INTO public.\"users\"(email, password, name, surname, is_man, birthday, country_id, status, image, role, verified) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
    private static final String SQL_SELECT_BY_ID = "SELECT * FROM public.\"users\" WHERE id = (?)";
    private static final String SQL_SELECT_BY_EMAIL = "SELECT * FROM public.\"users\" WHERE email = (?)";

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private EntityLoader entityLoader;

    private RowMapper<User> userRowMapper = (row, rowNumber) -> {
        return User.builder()
                .id(row.getLong("id"))
                .email(row.getString("email"))
                .password(row.getString("password"))
                .name(row.getString("name"))
                .surname(row.getString("surname"))
                .isMan(row.getBoolean("is_man"))
                .birthday(row.getDate("birthday"))
                .country(entityLoader.getCountryById(row.getInt("country_id")))
                .status(row.getString("status"))
                .image(row.getString("image"))
                .role(Role.valueOf(row.getString("role")))
                .verified(row.getBoolean("verified"))
                .createdAt(row.getTimestamp("created_at"))
                .build();
    };

    @Override
    public Long save(User user) {
        System.out.println("####### Templates!");
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement statement = connection
                    .prepareStatement(SQL_INSERT, Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, user.getEmail());
            statement.setString(2, user.getPassword());
            statement.setString(3, user.getName());
            statement.setString(4, user.getSurname());
            statement.setBoolean(5, user.getIsMan());
            statement.setDate(6, user.getBirthday());
            statement.setInt(7, user.getCountry().getId());
            statement.setString(8, user.getStatus());
            if (user.getImage() == null) {
                statement.setNull(9, Types.BIGINT);
            } else {
                statement.setString(9, user.getImage());
            }
            statement.setString(10, user.getRole().toString());
            statement.setBoolean(11, user.getVerified());

            return statement;
        }, keyHolder);

        return (Long) keyHolder.getKeys().get("id");
    }

    @Override
    public Optional<User> findByEmail(String email) {
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(SQL_SELECT_BY_EMAIL, new Object[]{email}, userRowMapper));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<User> findById(long id) {
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(SQL_SELECT_BY_ID, new Object[]{id}, userRowMapper));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<User> findByNameAndSurname(String name, String surname) {
        return null;
    }

    @Override
    public boolean updateInfo(User user) {
        return false;
    }
}