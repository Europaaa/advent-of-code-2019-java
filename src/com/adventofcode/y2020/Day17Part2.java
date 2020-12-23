package com.adventofcode.y2020;

import com.adventofcode.utils.CommonUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Day17Part2 {

    private static final String INPUT_FILE = "y2020/day17";

    public static class State {

        private char[][][][] state;

        private int dimW;
        private int dimX;
        private int dimY;
        private int dimZ;

        public State(char[][][][] state) {
            this.state = state;

            this.dimW = state.length;
            this.dimZ = state[0].length;
            this.dimY = state[0][0].length;
            this.dimX = state[0][0][0].length;
        }

        public State expand() {
            char[][][][] newState = new char[dimW + 2][][][];

            for (int w = 0; w < dimW + 2; w++) {
                newState[w] = new char[dimZ + 2][][];

                for (int z = 0; z < dimZ + 2; z++) {
                    newState[w][z] = new char[dimY + 2][];

                    for (int y = 0; y < dimY + 2; y++) {
                        newState[w][z][y] = new char[dimX + 2];

                        for (int x = 0; x < dimX + 2; x++) {
                            newState[w][z][y][x] = getNextState(state, x - 1, y - 1, z - 1, w - 1);
                        }
                    }
                }
            }
            return new State(newState);
        }

        private static char getNextState(char[][][][] space, int x, int y, int z, int w) {
            // Count active neighbors
            int count = 0;
            for (int iW = -1; iW <= 1; iW++) {
                if (w + iW < 0 || w + iW >= space.length) {
                    continue;
                }

                for (int iZ = -1; iZ <= 1; iZ++) {
                    if (z + iZ < 0 || z + iZ >= space[w + iW].length) {
                        continue;
                    }

                    for (int iY = -1; iY <= 1; iY++) {
                        if (y + iY < 0 || y + iY >= space[w + iW][z + iZ].length) {
                            continue;
                        }

                        for (int iX = -1; iX <= 1; iX++) {
                            if (x + iX < 0 || x + iX >= space[w + iW][z + iZ][y + iY].length) {
                                continue;
                            }
                            if (iW == 0 && iZ == 0 && iY == 0 && iX == 0) {
                                continue;
                            }
                            if (space[w + iW][z + iZ][y + iY][x + iX] == '#') {
                                count += 1;
                            }
                        }
                    }
                }
            }

            // Calculate the next state
            char current;
            if (0 <= w && w < space.length && 0 <= z && z < space[w].length &&
                    0 <= y && y < space[w][z].length && 0 <= x && x < space[w][z][y].length) {
                current = space[w][z][y][x];
            } else {
                current = '.';
            }

            switch (current) {
                case '#':
                    if (count == 2 || count == 3) {
                        return  '#';
                    }
                    return  '.';
                case '.':
                    if (count == 3) {
                        return  '#';
                    }
                    return '.';
            }
            throw new IllegalStateException();
        }

//        public String toString() {
//            StringBuilder builder = new StringBuilder();
//
//            for (int z = 0; z < dimZ; z++) {
//                builder.append("z=").append((-1 * ((dimZ - 1) / 2) + z)).append("\n");
//
//                for (int y = 0; y < dimY; y++) {
//                    for (int x = 0; x < dimX; x++) {
//                        builder.append(state[z][y][x]);
//                    }
//                    builder.append("\n");
//                }
//
//                builder.append("\n");
//            }
//
//            builder.append("\n");
//            return builder.toString();
//        }
    }

    public static void main(String[] args) throws FileNotFoundException {
        State state = getInput(CommonUtils.getInputFile(INPUT_FILE));

        for (int i = 0; i < 6; i++) {
            state = state.expand();
        }

        long count = 0;
        for (int iW = 0; iW < state.state.length; iW++) {
            for (int iZ = 0; iZ < state.state[iW].length; iZ++) {
                for (int iY = 0; iY < state.state[iW][iZ].length; iY++) {
                    for (int iX = 0; iX < state.state[iW][iZ][iY].length; iX++) {
                        if (state.state[iW][iZ][iY][iX] == '#') {
                            count += 1;
                        }
                    }
                }
            }
        }
        System.out.println(count);
    }

    private static State getInput(String path) throws FileNotFoundException {
        try (Scanner scanner = new Scanner(new File(path))) {
            char[][][][] space = new char[1][][][];

            List<char[]> lines = new ArrayList<>();
            while (scanner.hasNext()) {
                lines.add(scanner.nextLine().toCharArray());
            }

            space[0] = new char[1][][];
            space[0][0] = lines.toArray(new char[0][]);
            return new State(space);
        }
    }
}
