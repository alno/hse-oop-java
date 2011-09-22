package ru.hse.example;

import java.util.ArrayList;
import java.util.List;

public class Queens {

    public static void main(String[] args) {
        int size = 5;

        List<Queen> queens = new ArrayList<Queen>();

        if (!tryPlaceQueens(0, size, queens))
            System.out.println("Not possible!");

        for (Queen q : queens)
            System.out.println(q.x + " : " + q.y);
    }

    static boolean tryPlaceQueens(int x, int size, List<Queen> queens) {
        if (x >= size)
            return true;

        for (int i = 0; i < size; ++i) {
            Queen q = new Queen(x, i);

            if (!q.beats(queens)) {
                queens.add(q);

                if (tryPlaceQueens(x + 1, size, queens))
                    return true;

                queens.remove(q);
            }
        }

        return false;
    }
}
