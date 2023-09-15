package util;

public class Game {
    static public int[][] neighbors = {{-1, -1}, {0, -1}, {1, 0}, {1, 1}, {0, 1}, {-1, 0}};
    private char[][] map;
    private char[][] view;
    private int[][] safeCells;
    private int yetProbedCells;
    private int bombs;
    private boolean isWon, isLost;

    public Game(char[][] map) {
        this.map = map;
        init();
    }

    public void probe(int x, int y) {
        if(x >= 0 && x < view[0].length && y >= 0 && y < view.length && view[y][x] == '?') {
            if(map[y][x] == 't') {
                view[y][x] = '-';
                isLost = true;
                return;
            }
            view[y][x] = map[y][x];
            yetProbedCells--;
            if(map[y][x] == '0') {
                for(int i = 0; i < neighbors.length; i++) {
                    probe(x + neighbors[i][0], y + neighbors[i][1]);
                }
            }
        }
        if(yetProbedCells == bombs && isLost == false) {
            isWon = true;
        }
    }

    public void flag(int x, int y) {
        if(x >= 0 && x < view[0].length && y >= 0 && y < view.length && view[y][x] == '?') {
            view[y][x] = '*';
        }
    }

    public void printFinalMap() {
        System.out.println("Final map");
        printBoard(view);
        if( isWon ) {
            System.out.println("Result: Agent alive: all solved");
        } else if (isLost) {
            System.out.println("Result: Agent dead: found mine");
        } else {
            System.out.println("Result: Agent not terminated");
        }
    }

    public char[][] getView() {
        return view;
    }

    public int[][] getSafeCells() {
        return safeCells;
    }

    public int[] getSafeCells(int i) {
        if(i >= 0 && i < safeCells.length) {
            return safeCells[i];
        }
        return null;
    }

    public boolean isWon() {
        return isWon;
    }

    public boolean isLost() {
        return isLost;
    }

    private void init() {
        isWon = false;
        isLost = false;
        bombs = 0;
        view = new char[map.length][map[0].length];
        safeCells = new int[][] {{0, 0}, {map[0].length / 2, map.length / 2}};
        for(int i = 0; i < map.length; i++) {
            for(int j = 0; j < map[0].length; j++) {
                view[i][j] = '?';
                yetProbedCells++;
                if(map[i][j] == 't') {
                    bombs++;
                }
            }
        }
    }

    public void printBoard(char[][] board) {
        System.out.println();
        // first line
        for (int l = 0; l < board.length + 5; l++) {
            System.out.print(" ");// shift to start
        }
        for (int j = 0; j < board[0].length; j++) {
            System.out.print(j);// x indexes
            if (j < 10) {
                System.out.print(" ");
            }
        }
        System.out.println();
        // second line
        for (int l = 0; l < board.length + 3; l++) {
            System.out.print(" ");
        }
        for (int j = 0; j < board[0].length; j++) {
            System.out.print(" -");// separator
        }
        System.out.println();
        // the board
        for (int i = 0; i < board.length; i++) {
            for (int l = i; l < board.length - 1; l++) {
                System.out.print(" ");// fill with left-hand spaces
            }
            if (i < 10) {
                System.out.print(" ");
            }

            System.out.print(i + "/ ");// index+separator
            for (int j = 0; j < board[0].length; j++) {
                System.out.print(board[i][j] + " ");// value in the board
            }
            System.out.println();
        }
        System.out.println();
    }
}
