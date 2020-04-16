package ru.kpfu.itis.rodsher.repositories;

import ru.kpfu.itis.rodsher.models.Friends;
import ru.kpfu.itis.rodsher.models.FriendsStatus;

import java.util.List;
import java.util.Optional;

public interface FriendsRepository {
    void saveStatus(Friends friends);
    boolean remove(Friends friends);
    Optional<Friends> find(Long id1, Long id2);
    List<Friends> findFriendsForId(Long userId);
}