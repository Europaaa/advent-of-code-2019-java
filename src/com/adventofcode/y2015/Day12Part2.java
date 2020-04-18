package com.adventofcode.y2015;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Day12Part2 {

    private static final String INPUT_FILE = "y2015/day12";

    private static final String KEYWORD = "red";

    private static long evaluate(Object json) {
        if (json instanceof String) {
            return 0;
        }
        if (json instanceof Number) {
            return ((Number) json).longValue();
        }
        if (json instanceof List) {
            return ((List) json).stream().mapToLong(Day12Part2::evaluate).sum();
        }
        if (json instanceof Map) {
            boolean valid = ((Map) json).values().stream().noneMatch(KEYWORD::equals);
            if (valid) {
                return ((Map) json).values().stream().mapToLong(Day12Part2::evaluate).sum();
            }
            return 0;
        }
        throw new IllegalStateException("Unexpected data type: " + json);
    }

    public static void main(String[] args) throws FileNotFoundException, JsonProcessingException {
        Object json = getInput(CommonUtils.getInputFile(INPUT_FILE));

        System.out.println(evaluate(json));
    }

    private static Object getInput(String path) throws FileNotFoundException, JsonProcessingException {
        try (Scanner scanner = new Scanner(new File(path))) {
            String jsonText = scanner.nextLine();
            if (jsonText.startsWith("[")) {
                return new ObjectMapper().readValue(jsonText, List.class);
            }
            if (jsonText.startsWith("{")) {
                return new ObjectMapper().readValue(jsonText, LinkedHashMap.class);
            }
            if (jsonText.startsWith("\"")) {
                return jsonText;
            }
            return Long.valueOf(jsonText);
        }
    }
}
