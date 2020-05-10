package ru.kpfu.itis.rodsher.jlmq.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.kpfu.itis.rodsher.jlmq.models.Queue;

import java.util.Optional;

@Repository
public interface QueuesRepository extends JpaRepository<Queue, Long> {
    Optional<Queue> findByName(String name);
}
