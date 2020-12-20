package com.adventofcode.y2020;

import com.adventofcode.utils.CommonUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Scanner;
import java.util.Set;
import java.util.stream.Collectors;

public class Day22Part2 {

    private static final String INPUT_FILE = "y2020/day22";

    private static final int PLAYER_1 = 1;
    private static final int PLAYER_2 = 2;

    public static class Game {
        private Queue<Integer> player1;
        private Queue<Integer> player2;

        private Set<String> states;

        public Game(Queue<Integer> player1, Queue<Integer> player2) {
            this.player1 = player1;
            this.player2 = player2;

            this.states = new HashSet<>();
        }

        public int simulate() {
            while (!player1.isEmpty() && !player2.isEmpty()) {
                // If there was a previous round in this game that had exactly the same cards in the same order
                // in the same players' decks, the game instantly ends in a win for player 1
                if (!states.add(generateHash())) {
                    return PLAYER_1;
                }

                int card1 = player1.poll();
                int card2 = player2.poll();

                // If both players have at least as many cards remaining in their deck as the value of the card
                // they just drew, the winner of the round is determined by playing a new game of
                // Recursive Combat.
                int winner = -1;

                if (card1 <= player1.size() && card2 <= player2.size()) {
                    Game subGame = new Game(copyDeck(player1, card1), copyDeck(player2, card2));
                    winner = subGame.simulate();
                }
                // Otherwise, the winner of the round is the player with the higher-value card.
                else {
                    if (card1 > card2) {
                        winner = PLAYER_1;
                    } else {
                        winner = PLAYER_2;
                    }
                }

                switch (winner) {
                    case PLAYER_1:
                        player1.add(card1);
                        player1.add(card2);
                        break;
                    case PLAYER_2:
                        player2.add(card2);
                        player2.add(card1);
                        break;
                }
            }
            return player1.isEmpty() ? PLAYER_2 : PLAYER_1;
        }

        private Queue<Integer> copyDeck(Queue<Integer> deck, int size) {
            return new LinkedList<>(new ArrayList<>(deck).subList(0, size));
        }

        private String generateHash() {
            String deck1 = player1.stream().map(String::valueOf).collect(Collectors.joining(","));
            String deck2 = player2.stream().map(String::valueOf).collect(Collectors.joining(","));
            return deck1 + "|" + deck2;
        }
    }

    public static void main(String[] args) throws FileNotFoundException {
        Game game = getInput(CommonUtils.getInputFile(INPUT_FILE));

        int winner = game.simulate();
        List<Integer> cards = new ArrayList<>(winner == PLAYER_1 ? game.player1 : game.player2);

        long score = 0L;
        long size = cards.size();
        for (int i = 0; i < cards.size(); i++) {
            score += (size - i) * cards.get(i);
        }
        System.out.println(score);
    }

    private static Game getInput(String path) throws FileNotFoundException {
        try (Scanner scanner = new Scanner(new File(path))) {
            Queue<Integer> player1 = new LinkedList<>();
            Queue<Integer> player2 = new LinkedList<>();

            scanner.nextLine();

            String line = scanner.nextLine();
            while (!line.isEmpty()) {
                player1.add(Integer.parseInt(line));
                line = scanner.nextLine();
            }

            scanner.nextLine();

            line = scanner.nextLine();
            while (!line.isEmpty()) {
                player2.add(Integer.parseInt(line));

                if (scanner.hasNext()) {
                    line = scanner.nextLine();
                } else {
                    line = "";
                }
            }
            return new Game(player1, player2);
        }
    }
}
