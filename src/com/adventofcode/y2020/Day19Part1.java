package com.adventofcode.y2020;

import com.adventofcode.utils.CommonUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Day19Part1 {

    private static final String INPUT_FILE = "y2020/day19";

    private static final Pattern PATTERN_RULE = Pattern.compile("(\\d+):\\s(.+)");
    private static final Pattern PATTERN_SIMPLE_RULE = Pattern.compile("\"(\\w+)\"");

    private static final Pattern PATTERN_COMPOSITE_RULE = Pattern.compile("((?:\\d+\\s)*\\d+)");

    private Map<Integer, MatchRule> matchRules;
    private List<String> messages;

    public Day19Part1(Map<Integer, MatchRule> matchRules, List<String> messages) {
        this.matchRules = matchRules;
        this.messages = messages;
    }

    public long calculate() {
        Pattern pattern = Pattern.compile("^" + calculate(0) + "$");
        return messages.stream().map(pattern::matcher).filter(Matcher::matches).count();
    }

    private String calculate(int ruleId) {
        MatchRule matchRule = matchRules.get(ruleId);
        if (matchRule.type == MatchRule.Type.SIMPLE) {
            return matchRule.pattern;
        }

        List<String> patterns = new ArrayList<>();
        for (List<Integer> child : matchRule.children) {
            StringBuilder builder = new StringBuilder();
            for (Integer childRuleId : child) {
                builder.append(calculate(childRuleId));
            }
            patterns.add(builder.toString());
        }

        String pattern = patterns.stream().collect(Collectors.joining("|", "(", ")"));
        matchRule.setPattern(pattern);
        return pattern;
    }

    static class MatchRule {
        private Type type;

        // Type = Simple Rule
        private String pattern;

        // Type = Composite Rule
        private List<List<Integer>> children;

        enum Type {
            SIMPLE, COMPOSITE
        }

        public MatchRule() {
            this.children = new ArrayList<>();
        }

        public MatchRule setPattern(String pattern) {
            this.pattern = pattern;
            this.type = Type.SIMPLE;
            return this;
        }

        public MatchRule addChild(List<Integer> child) {
            this.children.add(child);
            this.type = Type.COMPOSITE;
            return this;
        }
    }

    public static void main(String[] args) throws FileNotFoundException {
        Day19Part1 day19Part1 = getInput(CommonUtils.getInputFile(INPUT_FILE));

        long count = day19Part1.calculate();
        System.out.println(count);
    }

    private static Day19Part1 getInput(String path) throws FileNotFoundException {
        try (Scanner scanner = new Scanner(new File(path))) {
            Map<Integer, MatchRule> rules = new HashMap<>();
            List<String> messages = new ArrayList<>();

            while (scanner.hasNext()) {
                String line = scanner.nextLine();

                if (line.isEmpty()) {
                    continue;
                }

                Matcher matcher = PATTERN_RULE.matcher(line);
                if (!matcher.matches()) {
                    messages.add(line);
                    continue;
                }

                int ruleId = Integer.parseInt(matcher.group(1));
                String rule = matcher.group(2);

                matcher = PATTERN_SIMPLE_RULE.matcher(rule);
                if (matcher.matches()) {
                    rules.put(ruleId, new MatchRule().setPattern(matcher.group(1)));
                    continue;
                }

                matcher = PATTERN_COMPOSITE_RULE.matcher(rule);

                MatchRule matchRule = new MatchRule();
                while (matcher.find()) {
                    matchRule.addChild(Arrays.stream(matcher.group(1).trim().split(" "))
                            .map(Integer::parseInt)
                            .collect(Collectors.toList()));
                }
                rules.put(ruleId, matchRule);
            }

            return new Day19Part1(rules, messages);
        }
    }
}
