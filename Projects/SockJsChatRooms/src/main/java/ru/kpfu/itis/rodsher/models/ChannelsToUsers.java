package ru.kpfu.itis.rodsher.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

@Entity
@Table(name = "channels_to_users")
@IdClass(ChannelsToUsersId.class)
public class ChannelsToUsers {
    @Id
    @ManyToOne
    @JoinColumn(name = "channel_id")
    private Channel channel;

    @Id
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "created_at")
    @ColumnDefault("CURRENT_TIMESTAMP")
    @CreationTimestamp
    private Timestamp createdAt;
}
