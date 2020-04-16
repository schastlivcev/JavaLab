package ru.kpfu.itis.rodsher.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;

@Data
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
