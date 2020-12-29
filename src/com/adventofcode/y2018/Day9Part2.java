package com.adventofcode.y2018;

import com.adventofcode.utils.CommonUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day9Part2 {
    private static final String INPUT_FILE = "y2018/day9";

    private static final Pattern PATTERN = Pattern.compile("(\\d+) players; last marble is worth (\\d+) points");

    private int numPlayer;
    private long maxMarbleId;

    private Marble pointer;
    private long[] scores;

    public Day9Part2(int numPlayer, long maxMarbleId) {
        this.numPlayer = numPlayer;
        this.maxMarbleId = maxMarbleId;

        this.pointer = new Marble(0);
        this.pointer.prev = this.pointer;
        this.pointer.next = this.pointer;

        this.scores = new long[numPlayer];
        Arrays.fill(scores, 0);
    }

    public long calculate() {
        for (int i = 1; i <= this.maxMarbleId; i++) {
            int player = i % numPlayer;
            this.scores[player] += simulate(i);

//            StringBuilder builder = new StringBuilder();
//            builder.append("[").append(player).append("] ");
//
//            Marble m = m0;
//            for (int j = 0; j <= i; j++) {
//                builder.append(m.id).append(" ");
//                m = m.next;
//            }
//
//            System.out.println(builder.toString());
        }
        return Arrays.stream(this.scores).max().getAsLong();
    }

    public long simulate(int id) {
        if (id % 23 == 0) {
            // Remove the marble 7 marbles counter-clockwise from the current marble
            Marble m = this.pointer;
            for (int i = 0; i < 7; i++) {
                m = m.prev;
            }

            Marble m1 = m.prev;
            Marble m2 = m.next;

            m1.next = m2;
            m2.prev = m1;

            // The marble located immediately clockwise of the marble that was removed
            // becomes the new current marble.
            pointer = m2;
            return id + m.id;
        }

        Marble m1 = this.pointer.next;
        Marble m2 = m1.next;

        Marble marble = new Marble(id);
        marble.prev = m1;
        marble.next = m2;
        m1.next = marble;
        m2.prev = marble;

        this.pointer = marble;
        return 0;
    }

    public Day9Part2 setMaxMarbleId(long maxMarbleId) {
        this.maxMarbleId = maxMarbleId;
        return this;
    }

    private static class Marble {
        private int id;
        private Marble prev;
        private Marble next;

        public Marble(int id) {
            this.id = id;
        }
    }

    public static void main(String[] args) throws FileNotFoundException {
        Day9Part2 day9Part1 = getInput(CommonUtils.getInputFile(INPUT_FILE));
        day9Part1.setMaxMarbleId(day9Part1.maxMarbleId * 100);

        long score = day9Part1.calculate();
        System.out.println(score);
    }

    private static Day9Part2 getInput(String path) throws FileNotFoundException {
        try (Scanner scanner = new Scanner(new File(path))) {
            Matcher matcher = PATTERN.matcher(scanner.nextLine());
            if (!matcher.matches()) {
                throw new IllegalStateException();
            }

            return new Day9Part2(Integer.parseInt(matcher.group(1)), Long.parseLong(matcher.group(2)));
        }
    }
}
