package be.envano.bowling;

public class Game {

    private int pinsToppled;

    public void roll(int pinsToppled) {
        this.pinsToppled = this.pinsToppled + pinsToppled;
    }

    public int score() {
        return this.pinsToppled;
    }

}
