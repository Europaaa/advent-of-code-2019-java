package com.adventofcode.y2015;

import javax.xml.bind.DatatypeConverter;
import java.io.File;
import java.io.FileNotFoundException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Scanner;

public class Day4Part2 {

    private static final String INPUT_FILE = "y2015/day4";

    private static final String PREFIX = "000000";

    public static long evaluate(String secret) throws NoSuchAlgorithmException {
        for (long i = 1; true; i++) {
            if (getHash(secret, i).startsWith(PREFIX)) {
                return i;
            }
        }
    }

    private static String getHash(String secret, long number) throws NoSuchAlgorithmException {
        MessageDigest messageDigest = MessageDigest.getInstance("MD5");
        messageDigest.update((secret + String.valueOf(number)).getBytes());

        byte[] digest = messageDigest.digest();
        return DatatypeConverter.printHexBinary(digest).toUpperCase();
    }

    public static void main(String[] args) throws FileNotFoundException, NoSuchAlgorithmException {
        String secret = getInput(CommonUtils.getInputFile(INPUT_FILE));

        System.out.println(evaluate(secret));
    }

    private static String getInput(String path) throws FileNotFoundException {
        try (Scanner scanner = new Scanner(new File(path))) {
            return scanner.nextLine();
        }
    }
}
