package ru.kpfu.itis.rodsher.repositories;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.kpfu.itis.rodsher.models.Article;
import ru.kpfu.itis.rodsher.models.User;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.Types;
import java.util.List;
import java.util.Optional;

@Repository
@Profile("template")
public class ArticlesRepositoryTemplateImpl implements ArticlesRepository {
    private static final String SQL_INSERT = "INSERT INTO public.\"articles\"(user_id, group_id, heading, content) values (?, ?, ?, ?)";
    private static final String SQL_SELECT_BY_ID = "SELECT * FROM public.\"articles\" WHERE id = (?)";
    private static final String SQL_SELECT_BY_USER_ID = "SELECT * FROM public.\"articles\" WHERE user_id = (?)";
    private static final String SQL_DELETE = "DELETE FROM public.\"walls\" WHERE id = (?)";

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private EntityLoader entityLoader;

    private RowMapper<Article> articleRowMapper = (row, rowNumber) -> {
        Integer groupId = row.getInt("group_id");
        if(groupId == 0) {
            groupId = null;
        }
        return Article.builder()
                .id(row.getLong("id"))
                .user(entityLoader.getUserById(row.getLong("user_id")))
                .groupId(groupId)
                .heading(row.getString("heading"))
                .content(row.getString("content"))
                .createdAt(row.getTimestamp("created_at"))
                .build();
    };

    @Override
    public Long save(Article article) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement statement = connection
                    .prepareStatement(SQL_INSERT, Statement.RETURN_GENERATED_KEYS);
            statement.setLong(1, article.getUser().getId());
            if (article.getGroupId() == null) {
                statement.setNull(2, Types.INTEGER);
            } else {
                statement.setInt(2, article.getGroupId());
            }
            statement.setString(3, article.getHeading());
            statement.setString(4, article.getContent());

            return statement;
        }, keyHolder);

        return (Long) keyHolder.getKeys().get("id");
    }

    @Override
    public List<Article> findByUserId(Long userId) {
        return jdbcTemplate.query(SQL_SELECT_BY_USER_ID, new Object[]{userId}, articleRowMapper);
    }

    @Override
    public Optional<Article> findById(Long id) {
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(SQL_SELECT_BY_ID, new Object[]{id}, articleRowMapper));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public boolean remove(Long id) {
        if(jdbcTemplate.update(SQL_DELETE, id) > 0) {
            return true;
        }
        return false;
    }
}
