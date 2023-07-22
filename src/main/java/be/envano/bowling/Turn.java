package be.envano.bowling;

public record Turn(int pinsToppled) {

    public Turn {
        if (pinsToppled > 10)
            throw new IllegalArgumentException("can't topple more than 10 pins");
        if (pinsToppled < 0)
            throw new IllegalArgumentException("can't topple negative pins");
    }

}
