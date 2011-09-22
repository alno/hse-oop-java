package ru.hse.example;

import java.util.List;

public class Queen {

    int x, y;

    public Queen(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public boolean beats(Queen queen) {
        if (x == queen.x)
            return true;
        if (y == queen.y)
            return true;

        if (x - y == queen.x - queen.y)
            return true;

        if (x + y == queen.x + queen.y)
            return true;

        return false;
    }

    public boolean beats(List<Queen> queens) {
        for (Queen q : queens)
            if (beats(q))
                return true;

        return false;
    }
}
