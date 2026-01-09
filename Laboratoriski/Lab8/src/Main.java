import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Map;
import java.util.concurrent.*;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;


enum ActionType {
    JOIN_GAME,
    LEAVE_GAME,
    ATTACK
}


class Player {
    private final String id;
    private int score;

    public Player(String id) {
        this.id = id;
        this.score = 0;
    }

    // TODO: Implement addScore function

    public synchronized void addScore(int delta) {
        score += delta;
    }

    @Override
    public String toString() {
        return "Player{" +
                "id='" + id + '\'' +
                ", score=" + score +
                '}';
    }
}


class PlayerAction {
    private final String playerId;
    private final ActionType action;

    public PlayerAction(String playerId, ActionType action) {
        this.playerId = playerId;
        this.action = action;
    }

    public String getPlayerId() {
        return playerId;
    }

    public ActionType getActionType() {
        return action;
    }

    public int getProcessingTime() {
        switch (action) {
            case JOIN_GAME:
                return 20;
            case LEAVE_GAME:
                return 30;
            case ATTACK:
                return 5;
            default:
                return 0;
        }
    }

    @Override
    public String toString() {
        return "PlayerAction{" +
                "playerId='" + playerId + '\'' +
                ", action=" + action +
                '}';
    }
}

class RoomAction {
    final String roomId;
    final PlayerAction action;

    RoomAction(String roomId, PlayerAction action) {
        this.roomId = roomId;
        this.action = action;
    }
}


class GameRoom {

    public final String roomId;
    public final Map<String, Player> players = new ConcurrentHashMap<>();

    private final BlockingQueue<PlayerAction> actionQueue =
            new LinkedBlockingQueue<>();

    private final ExecutorService executor =
            Executors.newSingleThreadExecutor();

    public volatile boolean running = true;

    public GameRoom(String roomId) {
        this.roomId = roomId;
        startProcessor();
    }

    // TODO: Implement startProcessor
    private void startProcessor() {
        executor.execute(
                () -> {
                    while (running || !actionQueue.isEmpty()) {
                        try {
                            PlayerAction p = actionQueue.poll(100, TimeUnit.MILLISECONDS);
                            if (p != null) {
                                this.processAction(p);
                            }
                        } catch (InterruptedException e) {

                        }
                    }
                }
        );
    }

    public void submitAction(PlayerAction action) {
        System.out.println("[" + roomId + "] RECEIVED: " + action);
        actionQueue.offer(action);
    }

    private void processAction(PlayerAction action) {
        try {
            Thread.sleep(action.getProcessingTime());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        switch (action.getActionType()) {
            case JOIN_GAME:
                players.putIfAbsent(
                        action.getPlayerId(),
                        new Player(action.getPlayerId())
                );
                System.out.println("[" + roomId + "] JOIN: "
                        + action.getPlayerId());
                break;

            case LEAVE_GAME:
                if (players.remove(action.getPlayerId()) != null) {
                    System.out.println("[" + roomId + "] LEAVE: "
                            + action.getPlayerId());
                } else {
                    System.out.println("[" + roomId
                            + "] LEAVE IGNORED (not in room): "
                            + action.getPlayerId());
                }
                break;

            case ATTACK:
                Player p = players.get(action.getPlayerId());
                if (p == null) {
                    System.out.println("[" + roomId
                            + "] ATTACK IGNORED (not in room): "
                            + action.getPlayerId());
                } else {
                    p.addScore(10);
                    System.out.println("[" + roomId + "] ATTACK: " + p);
                }
                break;
        }
    }

    public void shutdown() {
        // TODO: Add missing logic

        this.running = false;
        executor.shutdown();
        try {
            boolean finished = executor.awaitTermination(5, TimeUnit.SECONDS);
            if (!finished) executor.shutdownNow();
        } catch (InterruptedException e) {executor.shutdownNow();}
        System.out.println("[" + roomId + "] FINAL PLAYERS:");
        players.values().forEach(p ->
                System.out.println("  " + p));
    }
}


class GameServer {

    private final BlockingQueue<RoomAction> inputQueue =
            new LinkedBlockingQueue<>();

    private final ConcurrentHashMap<String, GameRoom> rooms =
            new ConcurrentHashMap<>();

    private final ExecutorService dispatcher =
            Executors.newSingleThreadExecutor();

    private volatile boolean running = true;

    public GameServer() {
        startDispatcher();
    }

    // TODO: Implement startDispatcher()
    private void startDispatcher() {
        dispatcher.execute(() -> {
            while (running || !inputQueue.isEmpty()) {
                RoomAction ra = inputQueue.poll();
                if(ra != null) {
                    GameRoom gr = rooms.computeIfAbsent(ra.roomId, k -> new GameRoom(ra.roomId));
                    gr.submitAction(ra.action);
                }
            }
        });
    }

    public void submit(String roomId, PlayerAction action) {
        inputQueue.offer(new RoomAction(roomId, action));
    }

    // TODO: Implement GameServer shutdown() method
    public void shutdown() throws InterruptedException {
        running = false;
        dispatcher.shutdown();
        if(!dispatcher.awaitTermination(5, TimeUnit.SECONDS)) dispatcher.shutdownNow();
        rooms.forEach((s, gameRoom) -> {
            gameRoom.shutdown();
        });
    }
}


public class Main {

    public static void main(String[] args) throws IOException, InterruptedException {

        GameServer server = new GameServer();

        BufferedReader reader =
                new BufferedReader(new InputStreamReader(System.in));

        String line;
        while ((line = reader.readLine()) != null && !line.isBlank()) {

            final String input = line.trim();

            try {
                String[] parts = input.split(",");
                if (parts.length != 3) {
                    System.err.println("Invalid input: " + input);
                    return;
                }

                String roomId = parts[0].trim();
                String playerId = parts[1].trim();
                ActionType actionType =
                        ActionType.valueOf(parts[2].trim());

                PlayerAction action =
                        new PlayerAction(playerId, actionType);

                server.submit(roomId, action);

            } catch (Exception e) {
                System.err.println(
                        "Failed to process line: " + input
                );
                e.printStackTrace();
            }
        }

        reader.close();

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        server.shutdown();

        System.out.println("Game server stopped.");
    }
}
