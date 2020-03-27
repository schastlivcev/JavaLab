package ru.kpfu.itis.rodsher.repositories;


import ru.kpfu.itis.rodsher.models.FileInfo;

import java.util.Optional;

public interface FilesRepository extends CrudRepository<Integer, FileInfo> {

    Optional<FileInfo> findByUrl(String url);
    Optional<FileInfo> findByStorageFileName(String originalFileName);
}
