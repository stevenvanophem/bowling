package be.envano.bowling;

import java.util.ArrayList;
import java.util.Objects;

public class Player {

    private final String name;

    public Player(String name) {
        Objects.requireNonNull(name, "player name missing");
        if (name.isBlank())
            throw new IllegalArgumentException("player name cannot be blank");
        this.name = name;
    }

    public static Player named(String name) {
        return new Player(name);
    }

    public String name() {
        return this.name;
    }

}
