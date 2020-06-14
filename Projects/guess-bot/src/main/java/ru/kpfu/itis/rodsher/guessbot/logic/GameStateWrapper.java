package ru.kpfu.itis.rodsher.guessbot.logic;

import ru.kpfu.itis.rodsher.guessbot.models.User;

public class GameStateWrapper {
    private GameState state;
    private User leader;
    private User winner;
    private String word;
    private String description;

    public GameStateWrapper(GameState gameState) {
        this.state = gameState;
    }

    public GameState getState() {
        return state;
    }

    public void setState(GameState state) {
        this.state = state;
    }

    public User getLeader() {
        return leader;
    }

    public void setLeader(User leader) {
        this.leader = leader;
    }

    public User getWinner() {
        return winner;
    }

    public void setWinner(User winner) {
        this.winner = winner;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
