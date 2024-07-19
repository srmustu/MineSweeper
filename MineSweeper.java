import java.util.Random;
import java.util.Scanner;

public class MineSweeper {
    private int rows;
    private int cols;
    private String[][] board;
    private String[][] mines;
    private boolean[][] revealed;
    private int mineCount;

    public MineSweeper(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
        this.board = new String[rows][cols];
        this.mines = new String[rows][cols];
        this.revealed = new boolean[rows][cols];
        this.mineCount = (rows * cols) / 4;
        initializeBoard();
        placeMines();
    }

    private void initializeBoard() {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                board[i][j] = "-";
                mines[i][j] = "-";
                revealed[i][j] = false;
            }
        }
    }

    private void placeMines() {
        Random random = new Random();
        int placedMines = 0;

        while (placedMines < mineCount) {
            int row = random.nextInt(rows);
            int col = random.nextInt(cols);

            if (!mines[row][col].equals("*")) {
                mines[row][col] = "*";
                placedMines++;
            }
        }
    }

    private void printBoard(String[][] board) {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                System.out.print(board[i][j] + " ");
            }
            System.out.println();
        }
    }

    private boolean isValidCoordinate(int row, int col) {
        return row >= 0 && row < rows && col >= 0 && col < cols;
    }

    private void reveal(int row, int col) {
        if (!isValidCoordinate(row, col) || revealed[row][col]) {
            return;
        }

        revealed[row][col] = true;

        if (mines[row][col].equals("*")) {
            board[row][col] = "*";
            printBoard(board);
            System.out.println("Game Over!!");
            System.exit(0);
        } else {
            int mineCount = countMines(row, col);
            board[row][col] = String.valueOf(mineCount);

            if (mineCount == 0) {
                // Recursive reveal for surrounding cells
                for (int i = -1; i <= 1; i++) {
                    for (int j = -1; j <= 1; j++) {
                        reveal(row + i, col + j);
                    }
                }
            }
        }
    }

    private int countMines(int row, int col) {
        int count = 0;
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                int newRow = row + i;
                int newCol = col + j;
                if (isValidCoordinate(newRow, newCol) && mines[newRow][newCol].equals("*")) {
                    count++;
                }
            }
        }
        return count;
    }

    public void play() {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("Current Board:");
            printBoard(board);

            System.out.print("Satır Giriniz: ");
            int row = scanner.nextInt();
            System.out.print("Sütun Giriniz: ");
            int col = scanner.nextInt();

            if (!isValidCoordinate(row, col)) {
                System.out.println("Geçersiz koordinat. Lütfen tekrar giriniz.");
                continue;
            }

            if (revealed[row][col]) {
                System.out.println("Bu koordinat daha önce seçildi, başka bir koordinat girin.");
                continue;
            }

            reveal(row, col);

            // Check if the game is won
            if (isGameWon()) {
                printBoard(board);
                System.out.println("Oyunu Kazandınız!");
                break;
            }
        }
        scanner.close();
    }

    private boolean isGameWon() {
        int revealedCount = 0;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (revealed[i][j]) {
                    revealedCount++;
                }
            }
        }
        return revealedCount == (rows * cols - mineCount);
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int rows, cols;

        while (true) {
            System.out.print("Matrisin satır sayısı: ");
            rows = scanner.nextInt();
            System.out.print("Matrisin sütun sayısı: ");
            cols = scanner.nextInt();

            if (rows >= 2 && cols >= 2) break;
            System.out.println("Matris boyutu en az 2x2 olmalıdır. Lütfen tekrar giriniz.");
        }

        MineSweeper game = new MineSweeper(rows, cols);
        game.play();
    }
}