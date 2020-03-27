package ru.kpfu.itis.rodsher.repositories;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.kpfu.itis.rodsher.models.Role;
import ru.kpfu.itis.rodsher.models.User;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.Optional;

@Component
public class UsersRepositoryJdbcTemplateImpl implements UsersRepository {
    private static final String SQL_SELECT_BY_ID = "SELECT * FROM public.\"users\" WHERE id = (?)";
    private static final String SQL_SELECT_BY_EMAIL = "SELECT * FROM public.\"users\" WHERE email = (?)";
    private static final String SQL_SELECT_BY_EMAIL_AND_NAME = "SELECT * FROM public.\"users\" WHERE email = (?) AND name = (?)";
    private static final String SQL_INSERT = "INSERT INTO public.\"users\"(email, password, name, is_man, verified, role) values (?, ?, ?, ?, ?, ?)";
    private static final String SQL_UPDATE_VERIFIED = "UPDATE public.\"users\" SET verified = TRUE WHERE id = (?)";
    private static final String SQL_DELETE_BY_ID = "DELETE FROM public.\"users\" WHERE id = (?)";

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private RowMapper<User> userRowMapper = (row, rowNumber) ->
            User.builder()
                    .id(row.getInt("id"))
                    .email(row.getString("email"))
                    .password(row.getString("password"))
                    .name(row.getString("name"))
                    .isMan(row.getBoolean("is_man"))
                    .verified(row.getBoolean("verified"))
                    .role(Role.valueOf(row.getString("role")))
                    .build();

    @Override
    public Integer save(User user) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement statement = connection
                    .prepareStatement(SQL_INSERT, Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, user.getEmail());
            statement.setString(2, user.getPassword());
            statement.setString(3, user.getName());
            statement.setBoolean(4, user.isMan());
            statement.setBoolean(5, user.isVerified());
            statement.setString(6, user.getRole().toString());
            return statement;
        }, keyHolder);

        return (Integer) keyHolder.getKeys().get("id");
    }

    @Override
    public Optional<User> findByEmail(String email) {
        try {
            User user = jdbcTemplate.queryForObject(SQL_SELECT_BY_EMAIL, new Object[]{email}, userRowMapper);
            return Optional.ofNullable(user);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<User> findById(int id) {
        try {
            User user = jdbcTemplate.queryForObject(SQL_SELECT_BY_ID, new Object[]{id}, userRowMapper);
            return Optional.ofNullable(user);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public boolean checkAvailability(String email, String name) {
        return jdbcTemplate.query(SQL_SELECT_BY_EMAIL_AND_NAME, new Object[]{email, name}, userRowMapper).size() <= 0;
    }

    @Override
    public boolean checkVerification(int id) {
        User user = jdbcTemplate.queryForObject(SQL_SELECT_BY_ID, new Object[]{id}, userRowMapper);
        return user != null && user.isVerified();
    }

    @Override
    public boolean setVerified(int id) {
        return jdbcTemplate.update(SQL_UPDATE_VERIFIED, id) > 0;
    }

    @Override
    public boolean removeById(int id) {
        return jdbcTemplate.update(SQL_DELETE_BY_ID, id) > 0;
    }
}
