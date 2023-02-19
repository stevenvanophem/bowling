package be.envano.bowling;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.stream.Stream;

class GameTest {

    private final Game game = new Game();

    @ParameterizedTest(name = "Pins toppled: {0} resulted in a score of {1}")
    @MethodSource
    void testScore(List<Integer> rolls, Integer score) {
        rolls.forEach(game::roll);
        int result = game.score();
        Assertions.assertThat(result).isEqualTo(score);
    }

    static Stream<Arguments> testScore() {
        return Stream.of(
                Arguments.of(List.of(1), 1),
                Arguments.of(List.of(0), 0)
        );
    }

}