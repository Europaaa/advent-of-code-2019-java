package com.adventofcode.y2020;

import com.adventofcode.utils.CommonUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Scanner;

public class Day22Part1 {

    private static final String INPUT_FILE = "y2020/day22";

    private Queue<Integer> player1;
    private Queue<Integer> player2;

    public Day22Part1(Queue<Integer> player1, Queue<Integer> player2) {
        this.player1 = player1;
        this.player2 = player2;
    }

    public List<Integer> simulate() {
        while (!player1.isEmpty() && !player2.isEmpty()) {
            int card1 = player1.poll();
            int card2 = player2.poll();

            if (card1 > card2) {
                player1.add(card1);
                player1.add(card2);
            } else {
                player2.add(card2);
                player2.add(card1);
            }
        }

        return new ArrayList<>(player1.isEmpty() ? player2 : player1);
    }

    public static void main(String[] args) throws FileNotFoundException {
        Day22Part1 day22Part1 = getInput(CommonUtils.getInputFile(INPUT_FILE));

        List<Integer> winner = day22Part1.simulate();

        long score = 0L;
        long size = winner.size();
        for (int i = 0; i < winner.size(); i++) {
            score += (size - i) * winner.get(i);
        }
        System.out.println(score);
    }

    private static Day22Part1 getInput(String path) throws FileNotFoundException {
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
            return new Day22Part1(player1, player2);
        }
    }
}
