package ru.kpfu.itis.rodsher.guessbot.logic;

import org.springframework.context.MessageSource;
import ru.kpfu.itis.rodsher.guessbot.models.ClientType;
import ru.kpfu.itis.rodsher.guessbot.models.Game;
import ru.kpfu.itis.rodsher.guessbot.models.User;
import ru.kpfu.itis.rodsher.guessbot.repositories.GamesRepository;
import ru.kpfu.itis.rodsher.guessbot.repositories.UsersRepository;
import ru.kpfu.itis.rodsher.guessbot.services.JoinService;
import ru.kpfu.itis.rodsher.guessbot.services.SenderService;

import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.concurrent.CountDownLatch;

public class GameLogic extends Thread {
    private static final int GAME_START_DELAY_MS = 20000;
    private static final int GAME_WORD_DELAY_MS = 45000;
    private static final int GAME_GUESS_DELAY_MS = 60000;

    private final SenderService senderService;
    private final JoinService joinService;
    private final UsersRepository usersRepository;
    private final GamesRepository gamesRepository;
    private final MessageSource messageSource;

//    private final Game gameStateWrappe;
    private final GameStateWrapper gameStateWrapper;
    private final Object timer;

    private boolean selfChanged = false;

    public GameLogic(GamesRepository gamesRepository, SenderService senderService, UsersRepository usersRepository, JoinService joinService, MessageSource messageSource) {
        super();
        this.gamesRepository = gamesRepository;
//        Optional<Game> game = this.gamesRepository.findById(1L);
//        gameStateWrappe = game.orElseGet(() -> gamesRepository.save(Game.builder().state(GameState.GAME_NOT_YET_STARTED).build()));
        this.gameStateWrapper = new GameStateWrapper(GameState.GAME_NOT_YET_STARTED);
        gameStateWrapper.setLeader(null);
        gameStateWrapper.setWord(null);
        gameStateWrapper.setDescription(null);
        gameStateWrapper.setWinner(null);

        this.senderService = senderService;
        this.messageSource = messageSource;
        this.usersRepository = usersRepository;
        this.joinService = joinService;
        this.timer = new Object();
    }

    public GameState getGameState() {
        return gameStateWrapper.getState();
    }

    public void setGameState(GameState gameState) {
        setState(gameState);
        synchronized (timer) {
            timer.notify();
        }
        synchronized (gameStateWrapper) {
            gameStateWrapper.notify();
        }
    }

    public void removeTimerDelay() {
        synchronized (timer) {
            timer.notify();
        }
    }

    private void setState(GameState gameState) {
        gameStateWrapper.setState(gameState);
    }

    public void setWord(String word) {
        gameStateWrapper.setWord(word);
    }

    public void setDescription(String description) {
        gameStateWrapper.setDescription(description);
    }

    public void setLeader(User leader) {
        gameStateWrapper.setLeader(leader);
    }

    public void setWinner(User winner) {
        gameStateWrapper.setWinner(winner);
    }

    public String getWord() {
        return gameStateWrapper.getWord();
    }

    public String getDescription() {
        return gameStateWrapper.getDescription();
    }

    public User getLeader() {
        return gameStateWrapper.getLeader();
    }

    public User getWinner() {
        return gameStateWrapper.getWinner();
    }

    private void setLeaderAndWordAndDescNull() {
        gameStateWrapper.setWord(null);
        gameStateWrapper.setDescription(null);
        gameStateWrapper.setLeader(null);
        gameStateWrapper.setWinner(null);
    }

    @Override
    public void run() {
        try {
            while(true) {
                synchronized (gameStateWrapper) {
                    if(!selfChanged) {
                        gameStateWrapper.wait();
                    }
                    switch(gameStateWrapper.getState()) {
                        case GAME_CREATED:
                            selfChanged = false;
                            setLeaderAndWordAndDescNull();
                            System.out.println("Game created.");
                            synchronized (timer) {
                                timer.wait(GAME_START_DELAY_MS);
                            }
                            if(!gameStateWrapper.getState().equals(GameState.GAME_CREATED)) {
                                break;
                            }
                            List<User> users = usersRepository.findAll();
                            if(users.size() > 1) {
                                StringBuilder playerNames = new StringBuilder();
                                for(int i = 0; i < users.size(); i++) {
                                    playerNames.append(users.get(i).getName()).append(" (").append(users.get(i).getClientType().equals(ClientType.DISCORD) ? "d" : "t").append(")");
                                    if(i != users.size() - 1) {
                                        playerNames.append(", ");
                                    }
                                }
                                System.out.println("Game started. Players: " + playerNames);
                                senderService.sendMessageToAll(messageSource.getMessage("game.start.success", new Object[]{playerNames}, Locale.getDefault()));
                                gameStateWrapper.setState(GameState.GAME_CHOOSING);
                                selfChanged = true;
                            } else if(users.size() == 1) {
                                String name = users.get(0).getName();
                                System.out.println("Game didn't start due to only one player: " + name);
                                senderService.sendMessageToAll(messageSource.getMessage("game.start.alone", new Object[]{name, name}, Locale.getDefault()));
                                joinService.disconnectAll();
                                setLeaderAndWordAndDescNull();
                                gameStateWrapper.setState(GameState.GAME_NOT_YET_STARTED);
                            }
                            break;
                        case GAME_CHOOSING:
                            selfChanged = false;
                            List<User> players = usersRepository.findAll();
                            if(players.size() > 1) {
                                int random = (int) (Math.random() * players.size());
                                User leader = players.get(random);
                                senderService.sendMessageToAll(messageSource.getMessage("game.choose.success", new Object[]{leader.getName()}, Locale.getDefault()));
                                setLeader(leader);
                                System.out.println("Chose leader player: " + leader.getName());
                                selfChanged = true;
                                if(gameStateWrapper.getLeader() != null) {
                                    gameStateWrapper.setState(GameState.GAME_WORDING);
                                } else {
                                    break;
                                }
                            } else if(players.size() == 1) {
                                senderService.sendMessageToAll(messageSource.getMessage("game.choose.alone", new Object[]{players.get(0).getName()}, Locale.getDefault()));
                                joinService.disconnectAll();
                                setLeaderAndWordAndDescNull();
                                gameStateWrapper.setState(GameState.GAME_NOT_YET_STARTED);
                            }
                            break;
                        case GAME_WORDING:
                            selfChanged = false;
                            System.out.println("WORDING: " + Thread.currentThread().getName());
                            if(gameStateWrapper.getState().equals(GameState.GAME_WORDING)) {
                                synchronized (timer) {
                                    timer.wait(GAME_WORD_DELAY_MS);
                                }
                                List<User> playersToWord = usersRepository.findAll();
                                if(playersToWord.size() > 1) {
                                    if(getLeader() == null) {
                                        gameStateWrapper.setState(GameState.GAME_CHOOSING);
                                        selfChanged = true;
                                        break;
                                    } else {
                                        if(gameStateWrapper.getState().equals(GameState.GAME_WORDING)) {
                                            if(gameStateWrapper.getWord() == null || gameStateWrapper.getDescription() == null) {
                                                System.out.println("WORD NULL");
                                                if(gameStateWrapper.getLeader() != null) {
                                                    senderService.sendMessageToAll(messageSource.getMessage("game.word.error", new Object[]{gameStateWrapper.getLeader().getName()}, Locale.getDefault()));
                                                }
                                                joinService.disconnectUser(gameStateWrapper.getLeader());
                                                setLeaderAndWordAndDescNull();
                                                setGameState(GameState.GAME_CHOOSING);
                                            } else {
                                                System.out.println("CHOSE: word=" + getWord() + ", desc=" + getDescription());
                                                StringBuilder closedWord = new StringBuilder();
                                                for(int i = 0; i < getWord().length(); i++) {
                                                    if(getWord().charAt(i) == ' ') {
                                                        closedWord.append(" ");
                                                    } else {
                                                        closedWord.append("-");
                                                    }
                                                }
                                                senderService.sendMessageToAll(messageSource.getMessage("game.word.chose", new Object[]{getLeader().getName(), getWord().length(), closedWord.toString(), getDescription()}, Locale.getDefault()));
                                                gameStateWrapper.setState(GameState.GAME_GUESSING);
                                                selfChanged = true;
                                                break;
                                            }
                                        }
                                    }
                                } else {
                                    senderService.sendMessageToAll(messageSource.getMessage("game.choose.alone", new Object[]{playersToWord.get(0).getName()}, Locale.getDefault()));
                                    joinService.disconnectAll();
                                    setLeaderAndWordAndDescNull();
                                    setGameState(GameState.GAME_NOT_YET_STARTED);
                                    break;
                                }
                            }
                            selfChanged = true;
                            break;
                        case GAME_GUESSING:
                            selfChanged = false;
                            System.out.println("GUESSING: " + Thread.currentThread().getName());
                            if(gameStateWrapper.getState().equals(GameState.GAME_GUESSING)) {
                                synchronized (timer) {
                                    timer.wait(GAME_GUESS_DELAY_MS);
                                }
                                List<User> endPlayers = usersRepository.findAll();
                                if(gameStateWrapper.getState().equals(GameState.GAME_GUESSING)) {
                                    if(endPlayers.size() == 1) {
                                        senderService.sendMessageToAll(messageSource.getMessage("game.choose.alone", new Object[]{endPlayers.get(0).getName()}, Locale.getDefault()));
                                    } else {
                                        StringBuilder playerNames = new StringBuilder();
                                        for(int i = 0; i < endPlayers.size(); i++) {
                                            playerNames.append(endPlayers.get(i).getName()).append(" (").append(endPlayers.get(i).getClientType().equals(ClientType.DISCORD) ? "d" : "t").append(")");
                                            if(i != endPlayers.size() - 1) {
                                                playerNames.append(", ");
                                            }
                                        }
                                        senderService.sendMessageToAll(messageSource.getMessage("game.end.timeout", new Object[]{getLeader().getName(), getWord(), getLeader().getName(), playerNames}, Locale.getDefault()));
                                    }
                                    joinService.disconnectAll();
                                    setLeaderAndWordAndDescNull();
                                    setGameState(GameState.GAME_NOT_YET_STARTED);
                                    break;
                                } else {
                                    if(endPlayers.size() == 1) {
                                        senderService.sendMessageToAll(messageSource.getMessage("game.choose.alone", new Object[]{endPlayers.get(0).getName()}, Locale.getDefault()));
                                        joinService.disconnectAll();
                                        setLeaderAndWordAndDescNull();
                                        setGameState(GameState.GAME_NOT_YET_STARTED);
                                        break;
                                    }
                                }
                            }
                            selfChanged = true;
                            break;
                        case GAME_ENDED:
                            selfChanged = false;
                            List<User> endgamePlayers = usersRepository.findAll();
                            StringBuilder endNames = new StringBuilder();
                            boolean spoiled = false;
                            if(getLeader().getId().equals(getWinner().getId())) {
                                spoiled = true;
                            }
                            for(int i = 0; i < endgamePlayers.size(); i++) {
                                if(spoiled) {
                                   if(endgamePlayers.get(i).getId().equals(getWinner().getId())) {
                                        continue;
                                   }
                                }
                                endNames.append(endgamePlayers.get(i).getName()).append(" (").append(endgamePlayers.get(i).getClientType().equals(ClientType.DISCORD) ? "d" : "t").append(")");
                                if(i != endgamePlayers.size() - 1) {
                                    endNames.append(", ");
                                }
                            }
                            if(spoiled) {
                                senderService.sendMessageToAll(messageSource.getMessage("game.end.spoil", new Object[]{gameStateWrapper.getWinner().getName(), endNames}, Locale.getDefault()));
                            } else {
                                senderService.sendMessageToAll(messageSource.getMessage("game.end.win", new Object[]{gameStateWrapper.getWinner().getName(), endNames}, Locale.getDefault()));
                            }
                            setLeaderAndWordAndDescNull();
                            joinService.disconnectAll();
                            setGameState(GameState.GAME_NOT_YET_STARTED);
                            break;
                    }
                }
            }
        } catch (InterruptedException e) {
            System.out.println("Game cycle was interrupted.");
        }
    }
}