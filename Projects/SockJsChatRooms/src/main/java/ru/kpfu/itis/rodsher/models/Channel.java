package ru.kpfu.itis.rodsher.models;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

@Entity
@Table(name = "channels")
public class Channel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String name;

    @Column
    @ColumnDefault("CURRENT_TIMESTAMP")
    @CreationTimestamp
    private Timestamp created_at;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "channel")
    private List<ChannelsToUsers> users;
}