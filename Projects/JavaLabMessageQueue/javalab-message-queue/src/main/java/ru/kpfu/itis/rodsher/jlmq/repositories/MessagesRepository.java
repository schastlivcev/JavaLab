package ru.kpfu.itis.rodsher.jlmq.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.kpfu.itis.rodsher.jlmq.models.Message;
import ru.kpfu.itis.rodsher.jlmq.models.MessageStatus;
import ru.kpfu.itis.rodsher.jlmq.models.Queue;

import java.util.Optional;
import java.util.Set;

@Repository
public interface MessagesRepository extends JpaRepository<Message, Long> {
    Optional<Message> findFirstByQueueAndStatusInOrderByCreatedAtAsc(Queue queue, Set<MessageStatus> statuses);
    @Modifying
    @Transactional
    @Query("UPDATE Message m SET m.status = ?2 WHERE m.id = ?1")
    int updateStatusById(Long messageId, MessageStatus status);
}