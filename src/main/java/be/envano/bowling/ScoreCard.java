package be.envano.bowling;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class ScoreCard {

    private final Map<Player, List<Frame>> playerFrames = new HashMap<>();

    public ScoreCard addParticipant(Player player) {
        List<Frame> frames = IntStream.range(0, 10)
                .mapToObj(i -> Frame.create())
                .toList();
        this.playerFrames.put(player, frames);
        return this;
    }

    public int amountOfPlayers() {
        return playerFrames.size();
    }

    public List<Player> players() {
        return List.copyOf(this.playerFrames.keySet());
    }

    public ScoreCard takeTurn(TakeTurn command) {
        Objects.requireNonNull(command);

        final Player player = command.player();
        final Turn turn = command.turn();

        this.playerFrames(player).stream()
                .filter(Frame::open)
                .findFirst()
                .orElseThrow()
                .takeTurn(turn);

        return this;
    }

    public List<Frame> playerFrames(Player player) {
        Objects.requireNonNull(player);
        return this.playerFrames.get(player);
    }

    public String print(Player player) {
        return playerFrames.get(player).stream()
                .map(Frame::print)
                .collect(Collectors.joining(" "));
    }

    public record TakeTurn(Player player, Turn turn) {

        public TakeTurn {
            Objects.requireNonNull(player);
            Objects.requireNonNull(turn);
        }

    }

}
