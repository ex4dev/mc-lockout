package dev.tswanson.lockout;

import dev.tswanson.lockout.challenge.ChallengeDifficulty;

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
                board[x][y] = pool.remove(getRandomChallenge(random, pool, ChallengeDifficulty.fromRow(y)));
            }
        }
        return board;
    }

    private static int getRandomChallenge(Random random, List<Challenge> pool, ChallengeDifficulty difficulty) {
        int index = random.nextInt(pool.size());
        Challenge challenge = pool.get(index);
        if (challenge.metadata().difficulty() != difficulty) return getRandomChallenge(random, pool, difficulty);
        return index;
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
