package be.envano.bowling;

import java.util.Objects;

public class Frame {

    private Turn firstTurn;
    private Turn secondTurn;
    private boolean strike;
    private boolean spare;

    private Frame() {
    }

    public static Frame create() {
        return new Frame();
    }

    public Frame takeTurn(Turn turn) {
        Objects.requireNonNull(turn);
        if (!this.open()) {
            throw new IllegalArgumentException("Can't take turns on a closed frame");
        }
        if (this.firstTurn == null) {
            return this.registerFirstTurn(turn);
        }
        if (this.secondTurn == null) {
            return this.registerSecondTurn(turn);
        }
        throw new IllegalStateException();
    }

    public Frame registerFirstTurn(Turn turn) {
        Objects.requireNonNull(turn);

        if (this.firstTurn != null)
            throw new IllegalStateException("Can't register first turn twice");
        if (this.secondTurn != null)
            throw new IllegalStateException("Can't registered first turn after the second");

        if (turn.pinsToppled() == 10) {
            this.strike = true;
            return this;
        }

        this.firstTurn = turn;
        return this;

    }

    public Frame registerSecondTurn(Turn turn) {
        Objects.requireNonNull(turn);

        if (this.firstTurn == null)
            throw new IllegalStateException("Can't register second turn without a first turn");
        if (this.secondTurn != null)
            throw new IllegalStateException("Can't register second turn twice");

        if (this.firstTurn.pinsToppled() + turn.pinsToppled() >= 10) {
            this.spare = true;
            return this;
        }

        this.secondTurn = turn;
        return this;
    }

    public Turn firstTurn() {
        return Objects.requireNonNullElseGet(this.firstTurn, () -> new Turn(0));
    }

    public Turn secondTurn() {
        return Objects.requireNonNullElseGet(this.secondTurn, () -> new Turn(0));
    }

    public boolean open() {
        boolean noTurnsTaken = this.firstTurn == null && this.secondTurn == null;
        boolean secondTurnNotTaken = this.firstTurn != null && this.secondTurn == null;
        return noTurnsTaken || secondTurnNotTaken;
    }

    public String print() {
        if (strike) {
            return "  X";
        }
        if (spare) {
            return this.firstTurn.pinsToppled() + " " + "/";
        }
        if (this.firstTurn == null) {
            return "-" + " " + "-";
        }
        if (this.secondTurn == null) {
            return this.firstTurn.pinsToppled() + " " + "-";
        }
        return this.firstTurn.pinsToppled() + " " + this.secondTurn().pinsToppled();
    }

}
