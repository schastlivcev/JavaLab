package ru.kpfu.itis.rodsher.guessbot.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;
import ru.kpfu.itis.rodsher.guessbot.logic.GameState;

import javax.persistence.*;
import java.sql.Timestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

@Entity
@Table(name = "games")
public class Game {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    @Enumerated(EnumType.STRING)
    private GameState state;

    @Column
    private String word;

    @Column
    private String description;

    @ManyToOne
    @JoinColumn(name = "leader_id")
    private User leader;

    @ManyToOne
    @JoinColumn(name = "winner_id")
    private User winner;

    @Column(name = "created_at")
    @ColumnDefault("CURRENT_TIMESTAMP")
    @CreationTimestamp
    private Timestamp createdAt;
}