package com.adventofcode.y2020;

import com.adventofcode.utils.CommonUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Day25Part1 {

    private static final String INPUT_FILE = "y2020/day25";

    private static final long SUBJECT = 7L;
    private static final long TRANSFORMER = 20201227;

    private long publicKey1;
    private long publicKey2;

    private long loopSize1;
    private long loopSize2;

    public Day25Part1(long publicKey1, long publicKey2) {
        this.publicKey1 = publicKey1;
        this.publicKey2 = publicKey2;
    }

    public long getEncryptionKey() {
        this.loopSize1 = this.getLoopSize(SUBJECT, publicKey1);
        this.loopSize2 = this.getLoopSize(SUBJECT, publicKey2);

        long value = 1L;
        for (int i = 0; i < loopSize1; i++) {
            value = (value * publicKey2) % TRANSFORMER;
        }
        return value;
    }

    private long getLoopSize(long subject, long target) {
        int i = 0;
        for (long value = 1L; value != target; value = (value * subject) % TRANSFORMER) {
            i += 1;
        }
        return i;
    }

    public static void main(String[] args) throws FileNotFoundException {
        Day25Part1 day25Part1 = getInput(CommonUtils.getInputFile(INPUT_FILE));

        long encryptionKey = day25Part1.getEncryptionKey();
        System.out.println(encryptionKey);
    }

    private static Day25Part1 getInput(String path) throws FileNotFoundException {
        try (Scanner scanner = new Scanner(new File(path))) {
            return new Day25Part1(Long.parseLong(scanner.nextLine()), Long.parseLong(scanner.nextLine()));
        }
    }
}
