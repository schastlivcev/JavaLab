package ru.kpfu.itis.rodsher.repositories;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.kpfu.itis.rodsher.models.VerificationToken;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.Optional;

@Component
public class VerificationTokenRepositoryImpl implements VerificationTokenRepository {
    private static final String SQL_SELECT_BY_TOKEN = "SELECT * FROM public.\"tokens\" WHERE token = (?)";
    private static final String SQL_INSERT = "INSERT INTO public.\"tokens\"(user_id, token, expiry_date) values (?, ?, ?)";
    private static final String SQL_DELETE_BY_ID = "DELETE FROM public.\"tokens\" WHERE id = (?)";

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private RowMapper<VerificationToken> tokenRowMapper = (row, rowNumber) ->
            VerificationToken.builder()
                    .id(row.getInt("id"))
                    .userId(row.getInt("user_id"))
                    .token(row.getString("token"))
                    .expiryDate(row.getDate("expiry_date"))
                    .build();

    @Override
    public Integer save(int userId, String token, Date expiryDate) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement statement = connection
                    .prepareStatement(SQL_INSERT, Statement.RETURN_GENERATED_KEYS);
            statement.setInt(1, userId);
            statement.setString(2, token);
            statement.setDate(3, expiryDate);
            return statement;
        }, keyHolder);

        return (Integer) keyHolder.getKeys().get("id");
    }

    @Override
    public Optional<VerificationToken> findByToken(String token) {
        try {
            VerificationToken verificationToken = jdbcTemplate.queryForObject(SQL_SELECT_BY_TOKEN, new Object[]{token}, tokenRowMapper);
            return Optional.ofNullable(verificationToken);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public boolean removeById(int id) {
        return jdbcTemplate.update(SQL_DELETE_BY_ID, id) > 0;
    }
}
