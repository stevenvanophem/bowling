package be.envano.bowling;


import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ScoreCardTest {

    @Test
    @DisplayName("A scorecard can have an undisclosed amount of names on it")
    void testScoreCardNames() {
        ScoreCard scoreCard = new ScoreCard();

        scoreCard.addParticipant(Player.named("Steven"));
        scoreCard.addParticipant(Player.named("Justy"));
        scoreCard.addParticipant(Player.named("Kevin"));
        scoreCard.addParticipant(Player.named("Leo"));
        scoreCard.addParticipant(Player.named("Eddy"));

        assertThat(scoreCard.amountOfPlayers()).isEqualTo(5);
    }

    @Test
    @DisplayName("A player has a name")
    void testPlayerHasName() {
        Player player = new Player("stevo");

        assertThat(player.name()).isEqualTo("stevo");
    }

    @Test
    @DisplayName("A player name cannot be blank")
    void testPlayerNameBlank() {
        assertThatThrownBy(() -> new Player(""))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("player name cannot be blank");
    }

    @Test
    @DisplayName("A player name cannot be null")
    void testPlayerNameNull() {
        assertThatThrownBy(() -> new Player(null))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    @DisplayName("A turn can topple maximum 10 pins")
    void testTurn() {
        Turn turn = new Turn(10);

        assertThat(turn.pinsToppled()).isEqualTo(10);
    }

    @Test
    @DisplayName("A turn can't topple negative pins")
    void testNegativePins() {
        assertThatThrownBy(() -> new Turn(-1))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("can't topple negative pins");
    }

    @Test
    @DisplayName("A turn can't topple more than 10 pins")
    void testTooManyPins() {
        assertThatThrownBy(() -> new Turn(11))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("can't topple more than 10 pins");
    }

    @Test
    @DisplayName("A frame consists out of two turns")
    void testFrameTurns() {
        Frame frame = Frame.create();

        assertThat(frame.firstTurn()).isEqualTo(new Turn(0));
        assertThat(frame.secondTurn()).isEqualTo(new Turn(0));
    }

    @Test
    @DisplayName("I can register the pins toppled of the first turn")
    void testRegisterPinsToppledOfFirstTurn() {
        Turn firstTurn = Frame.create()
                .registerFirstTurn(new Turn(2))
                .firstTurn();

        assertThat(firstTurn.pinsToppled()).isEqualTo(2);
    }

    @Test
    @DisplayName("I can register the pins toppled of the second turn")
    void testRegisteredPinsToppledOfSecondTurn() {
        Frame frame = Frame.create()
                .registerFirstTurn(new Turn(2))
                .registerSecondTurn(new Turn(5));

        assertThat(frame.firstTurn().pinsToppled()).isEqualTo(2);
        assertThat(frame.secondTurn().pinsToppled()).isEqualTo(5);
    }

    @Test
    @DisplayName("Can't register first turn when already registered")
    void testRegisterFirstOops() {
        assertThatExceptionOfType(IllegalStateException.class)
                .isThrownBy(() -> Frame.create()
                        .registerFirstTurn(new Turn(2))
                        .registerFirstTurn(new Turn(1)))
                .withMessage("Can't register first turn twice");
    }

    @Test
    @DisplayName("Can't register second turn when no first registered")
    void testRegisterFirstOops2() {
        assertThatExceptionOfType(IllegalStateException.class)
                .isThrownBy(() -> Frame.create()
                        .registerSecondTurn(new Turn(2)))
                .withMessage("Can't register second turn without a first turn");
    }

    @Test
    @DisplayName("Can't register second turn twice")
    void testRegisterFirstOops3() {
        assertThatExceptionOfType(IllegalStateException.class)
                .isThrownBy(() -> Frame.create()
                        .registerFirstTurn(new Turn(1))
                        .registerSecondTurn(new Turn(2))
                        .registerSecondTurn(new Turn(3)))
                .withMessage("Can't register second turn twice");
    }

    @Test
    @DisplayName("A scoreboard has 10 frames per player")
    void testFrames() {
        ScoreCard scoreCard = new ScoreCard();

        scoreCard.addParticipant(new Player("stevo"))
                .players()
                .forEach(player -> {
                    List<Frame> frames = scoreCard.playerFrames(player);
                    assertThat(frames.size()).isEqualTo(10);
                });
    }

    @Test
    @DisplayName("A player can have two turns")
    void testTwoTurns() {
        Player player = Player.named("steven");

        String playerScore = new ScoreCard()
                .addParticipant(player)
                .takeTurn(new ScoreCard.TakeTurn(player, new Turn(2)))
                .takeTurn(new ScoreCard.TakeTurn(player, new Turn(5)))
                .print(player);

        assertThat(playerScore).isEqualTo("2 5 - - - - - - - - - - - - - - - - - -");
    }

    @Test
    @DisplayName("A player that rolls 10 pins on the first go of the frame marks a strike")
    void testStrike() {
        Player player = Player.named("steven");

        String playerScore = new ScoreCard()
                .addParticipant(player)
                .takeTurn(new ScoreCard.TakeTurn(player, new Turn(10)))
                .print(player);

        assertThat(playerScore).isEqualTo("  X - - - - - - - - - - - - - - - - - -");
    }

    @Test
    @DisplayName("A player that rolls 10 pins on the second go of the frame marks a spare")
    void testSpare() {
        Player player = Player.named("steven");

        String playerScore = new ScoreCard()
                .addParticipant(player)
                .takeTurn(new ScoreCard.TakeTurn(player, new Turn(2)))
                .takeTurn(new ScoreCard.TakeTurn(player, new Turn(9)))
                .print(player);

        assertThat(playerScore).isEqualTo("2 / - - - - - - - - - - - - - - - - - -");
    }

}