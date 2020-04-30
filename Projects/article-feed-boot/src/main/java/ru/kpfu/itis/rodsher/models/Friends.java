package ru.kpfu.itis.rodsher.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

@Entity
@Table(name = "friends")
@IdClass(FriendsId.class)
public class Friends {
    @Id
    @ManyToOne
    @JoinColumn(name = "user_sender_id")
    private User userSender;
    @Id
    @ManyToOne
    @JoinColumn(name = "user_recipient_id")
    private User userRecipient;
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private FriendsStatus status;
    @Column(name = "created_at")
    @ColumnDefault("CURRENT_TIMESTAMP")
    @UpdateTimestamp
    private Timestamp createdAt;
}