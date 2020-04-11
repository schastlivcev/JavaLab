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
import ru.kpfu.itis.rodsher.models.Wall;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;

@Repository
@Profile("template")
public class WallsRepositoryTemplateImpl implements WallsRepository {
    private static final String SQL_INSERT = "INSERT INTO public.\"walls\"(user_id, article_id, reply, bookmark) values (?, ?, ?, ?)";
    private static final String SQL_SELECT_BY_ID = "SELECT * FROM public.\"walls\" WHERE id = (?)";
    private static final String SQL_SELECT_BY_USER_ID = "SELECT * FROM public.\"walls\" WHERE user_id = (?) ORDER BY created_at DESC";
    private static final String SQL_DELETE = "DELETE FROM public.\"walls\" WHERE id = (?)";

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private EntityLoader entityLoader;

    private RowMapper<Wall> articleRowMapper = (row, rowNumber) -> {
        Article article = entityLoader.getArticleById(row.getLong("article_id"));
        return Wall.builder()
                .id(row.getLong("id"))
                .user(article.getUser().getId() == row.getLong("user_id") ? article.getUser() : entityLoader.getUserById(row.getLong("user_id")))
                .article(article)
                .reply(row.getBoolean("reply"))
                .bookmark(row.getBoolean("bookmark"))
                .createdAt(row.getTimestamp("created_at"))
                .build();
    };

    @Override
    public Long save(Wall wall) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement statement = connection
                    .prepareStatement(SQL_INSERT, Statement.RETURN_GENERATED_KEYS);
            statement.setLong(1, wall.getUser().getId());
            statement.setLong(2, wall.getArticle().getId());
            statement.setBoolean(3, wall.isReply());
            statement.setBoolean(4, wall.isBookmark());

            return statement;
        }, keyHolder);

        return (Long) keyHolder.getKeys().get("id");
    }

    @Override
    public List<Wall> findByUserId(Long userId) {
        return jdbcTemplate.query(SQL_SELECT_BY_USER_ID, new Object[]{userId}, articleRowMapper);
    }

    @Override
    public Optional<Wall> findById(Long id) {
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
