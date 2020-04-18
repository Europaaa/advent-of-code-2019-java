package com.adventofcode.y2015;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Scanner;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Day7Part2 {

    private static final String INPUT_FILE = "y2015/day7";

    private static final String REGEX_NUMERIC = "\\d+";

    private static long evaluate(List<Command> commands) {
        Computer computer = new Computer();
        commands.forEach(computer::connect);
        computer.analyze();

        int signal = computer.signals.get("a");
        computer.connect(new Command(CommandType.INPUT, new String[] { String.valueOf(signal) }, "b"));
        computer.analyze();

        return computer.signals.get("a");
    }

    private static class Computer {

        private Map<String, Command> wires = new HashMap<>();

        private Map<String, Set<String>> dependencies;
        private Map<String, Integer> signals;

        public void connect(Command command) {
            wires.put(command.output, command);
        }

        private void analyze() {
            dependencies = new HashMap<>();
            signals = new HashMap<>();

            wires.keySet().forEach(wire -> {
                Command command = wires.get(wire);
                dependencies.put(wire, getDependencies(command));
            });

            Set<String> sources = dependencies.entrySet().stream()
                    .filter(entry -> entry.getValue().isEmpty())
                    .map(Map.Entry::getKey)
                    .collect(Collectors.toSet());
            sources.stream().map(wires::get).forEach(this::evaluate);

            Set<String> targets = new HashSet<>(wires.keySet());
            targets.removeAll(sources);

            while (!targets.isEmpty()) {
                Set<String> resolvable = targets.stream()
                        .filter(target -> dependencies.get(target).stream().allMatch(signals::containsKey))
                        .collect(Collectors.toSet());
                resolvable.stream().map(wires::get).forEach(this::evaluate);

                targets.removeAll(resolvable);
            }
        }

        private Set<String> getDependencies(Command command) {
            CommandType type = command.type;

            Set<String> dependencies = new HashSet<>();

            if (!command.inputs[0].matches(REGEX_NUMERIC)) {
                dependencies.add(command.inputs[0]);
            }

            if (type.operands == 2) {
                if (!command.inputs[1].matches(REGEX_NUMERIC)) {
                    dependencies.add(command.inputs[1]);
                }
            }
            return dependencies;
        }

        private void evaluate(Command command) {
            switch (command.type) {
                case INPUT:
                    signals.put(command.output, getValue(command.inputs[0]));
                    break;
                case AND:
                    signals.put(command.output, getValue(command.inputs[0]) & getValue(command.inputs[1]));
                    break;
                case OR:
                    signals.put(command.output, getValue(command.inputs[0]) | getValue(command.inputs[1]));
                    break;
                case LSHIFT:
                    signals.put(command.output, getValue(command.inputs[0]) << getValue(command.inputs[1]));
                    break;
                case RSHIFT:
                    signals.put(command.output, getValue(command.inputs[0]) >> getValue(command.inputs[1]));
                    break;
                case NOT:
                    signals.put(command.output, ~getValue(command.inputs[0]));
                    break;
            }
        }

        private int getValue(String input) {
            if (input.matches(REGEX_NUMERIC)) {
                return Integer.valueOf(input);
            }
            return signals.get(input);
        }
    }

    public static void main(String[] args) throws FileNotFoundException {
        List<Command> commands = getInput(CommonUtils.getInputFile(INPUT_FILE));

        System.out.println(evaluate(commands));
    }

    private static List<Command> getInput(String path) throws FileNotFoundException {
        try (Scanner scanner = new Scanner(new File(path))) {
            List<Command> codes = new ArrayList<>();

            while (scanner.hasNext()) {
                String code = scanner.nextLine();

                Command command = Arrays.stream(CommandType.values())
                        .map(op -> {
                            Matcher matcher = op.regex.matcher(code);
                            if (matcher.matches()) {
                                String[] inputs = new String[op.operands];
                                int i = 1;
                                for (; i <= inputs.length; i++) {
                                    inputs[i - 1] = matcher.group(i);
                                }
                                String output = matcher.group(i);
                                return new Command(op, inputs, output);
                            }
                            return null;
                        })
                        .filter(Objects::nonNull)
                        .findFirst()
                        .get();

                codes.add(command);
            }
            return codes;
        }
    }

    private static final String SIGNAL = "(\\d+)";
    private static final String WIRE = "(\\w+)";
    private static final String TOKEN = "([\\w\\d]+)";

    private enum CommandType {
        INPUT(TOKEN + " -> " + WIRE, 1),
        AND(TOKEN + " AND " + TOKEN + " -> " + WIRE, 2),
        OR(TOKEN + " OR " + TOKEN + " -> " + WIRE, 2),
        LSHIFT(TOKEN + " LSHIFT " + SIGNAL + " -> " + WIRE, 2),
        RSHIFT(TOKEN + " RSHIFT " + SIGNAL + " -> " + WIRE, 2),
        NOT("NOT " + TOKEN + " -> " + WIRE, 1);

        private Pattern regex;
        private int operands;

        CommandType(String regex, int operands) {
            this.regex = Pattern.compile(regex);
            this.operands = operands;
        }
    }

    private static class Command {
        private CommandType type;
        private String[] inputs;
        private String output;

        public Command(CommandType type, String[] inputs, String output) {
            this.type = type;
            this.inputs = inputs;
            this.output = output;
        }
    }
}
