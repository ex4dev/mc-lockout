package dev.tswanson.lockout;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class BoardGenerator {

    private Challenge[][] board;

    public Challenge[][] generateBoard(int size) {
        List<Challenge> pool = new ArrayList<>(Lockout.getInstance().getChallenges());
        Challenge[][] board = new Challenge[size][size];
        Random random = new Random();
        for (int x = 0; x < size; x++) {
            for (int y = 0; y < size; y++) {
                board[x][y] = pool.remove(random.nextInt(pool.size()));
            }
        }
        return board;
    }

    public Challenge[][] getBoard() {
        if (board == null) {
            board = generateBoard(5);
        }
        return board;
    }

    public void resetBoard() {
        this.board = generateBoard(5);
    }

    public boolean containsChallenge(Challenge challenge) {
        for (Challenge[] row : board) {
            for (Challenge c : row) {
                if (c.equals(challenge)) return true;
            }
        }
        return false;
    }
}
