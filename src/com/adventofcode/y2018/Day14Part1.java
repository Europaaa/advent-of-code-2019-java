package com.adventofcode.y2018;

import com.adventofcode.utils.CommonUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Day14Part1 {
    private static final String INPUT_FILE = "y2018/day14";

    private String scores;

    private int player1;
    private int player2;

    public Day14Part1() {
        this.scores = "37";

        this.player1 = 0;
        this.player2 = 1;
    }

    public void simulate() {
        int recipe1 = this.getScore(this.player1);
        int recipe2 = this.getScore(this.player2);

        int[] recipes = combine(recipe1, recipe2);

        StringBuilder builder = new StringBuilder();
        builder.append(scores);

        for (int i = 0; i < recipes.length; i++) {
            builder.append(recipes[i]);
        }
        this.scores = builder.toString();

        this.player1 = (this.player1 + this.getScore(this.player1) + 1) % this.scores.length();
        this.player2 = (this.player2 + this.getScore(this.player2) + 1) % this.scores.length();
    }

    private int[] combine(int recipe1, int recipe2) {
        char[] chars = String.valueOf(recipe1 + recipe2).toCharArray();

        int[] recipes = new int[chars.length];
        for (int i = 0; i < chars.length; i++) {
            recipes[i] = chars[i] - '0';
        }
        return recipes;
    }

    private int getScore(int recipe) {
        return scores.charAt(recipe) - '0';
    }

    public static void main(String[] args) throws FileNotFoundException {
        int rounds = getInput(CommonUtils.getInputFile(INPUT_FILE));

        Day14Part1 day14Part1 = new Day14Part1();
        while (day14Part1.scores.length() < rounds + 10) {
            day14Part1.simulate();
            System.out.println(day14Part1.scores.length());
        }
        System.out.println(day14Part1.scores.substring(rounds, rounds + 10));
    }

    private static int getInput(String path) throws FileNotFoundException {
        try (Scanner scanner = new Scanner(new File(path))) {
            return scanner.nextInt();
        }
    }
}
