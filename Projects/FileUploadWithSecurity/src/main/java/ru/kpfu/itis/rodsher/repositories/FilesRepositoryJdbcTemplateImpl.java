package ru.kpfu.itis.rodsher.repositories;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.kpfu.itis.rodsher.models.FileInfo;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;

@Component
public class FilesRepositoryJdbcTemplateImpl implements FilesRepository {
    private static final String SQL_SELECT_BY_URL = "SELECT * FROM public.\"files\" WHERE url = (?)";
    private static final String SQL_SELECT_BY_STORAGE_FILE_NAME = "SELECT * FROM public.\"files\" WHERE storage_file_name = (?)";
    private static final String SQL_INSERT = "INSERT INTO public.\"files\"(storage_file_name, original_file_name, size, content_type, url, user_id) values (?, ?, ?, ?, ?, ?)";

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private RowMapper<FileInfo> fileInfoRowMapper = (row, rowNumber) ->
            FileInfo.builder()
                    .id(row.getInt("id"))
                    .storageFileName(row.getString("storage_file_name"))
                    .originalFileName(row.getString("original_file_name"))
                    .size(row.getLong("size"))
                    .type(row.getString("content_type"))
                    .url(row.getString("url"))
                    .userId(row.getInt("user_id"))
                    .build();

    @Override
    public void save(FileInfo fileInfo) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement statement = connection
                    .prepareStatement(SQL_INSERT, Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, fileInfo.getStorageFileName());
            statement.setString(2, fileInfo.getOriginalFileName());
            statement.setLong(3, fileInfo.getSize());
            statement.setString(4, fileInfo.getType());
            statement.setString(5, fileInfo.getUrl());
            statement.setInt(6, fileInfo.getUserId());
            return statement;
        }, keyHolder);

        Integer key = (Integer) keyHolder.getKeys().get("id");
        return;
    }

    @Override
    public Optional<FileInfo> findByUrl(String url) {
        try {
            FileInfo fileInfo = jdbcTemplate.queryForObject(SQL_SELECT_BY_URL, new Object[]{url}, fileInfoRowMapper);
            return Optional.ofNullable(fileInfo);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<FileInfo> findByStorageFileName(String storageFileName) {
        try {
            FileInfo fileInfo = jdbcTemplate.queryForObject(SQL_SELECT_BY_STORAGE_FILE_NAME, new Object[]{storageFileName}, fileInfoRowMapper);
            return Optional.ofNullable(fileInfo);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<FileInfo> find(Integer integer) {
        return Optional.empty();
    }

    @Override
    public List<FileInfo> findAll() {
        return null;
    }

    @Override
    public void delete(Integer integer) {

    }
}
