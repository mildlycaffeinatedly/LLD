import java.util.ArrayList;

public class Board {
    private Marker[][] board;
    private int boardSize;

    Board(int boardSize) {
        this.boardSize = boardSize;
        board = new Marker[boardSize][boardSize];
        for(int i = 0; i < boardSize; i++) {
            for(int j = 0; j < boardSize; j++) {
                board[i][j] = null;
            }
        }
    }

    public void showBoard() {
        for(int i = 0; i < boardSize; i++) {
            for(int j = 0; j < boardSize; j++) {
                Marker curr = board[i][j];
                if(curr == null) {
                    System.out.print(" . ");
                } else {
                    System.out.print(" " + curr + " ");
                }
            }
            System.out.println();
        }
        System.out.println();
    }

    public void placeMarker(Marker marker, int x, int y) {
        if(marker == null) {
            throw new IllegalArgumentException("Marker can not be null");
        }

        if(x < 0 || y < 0 || x >= boardSize || y >= boardSize) {
            throw new IndexOutOfBoundsException("Invalid position.");
        }

        if(board[x][y] != null) {
            throw new IllegalStateException("Cell is already occupied");
        }

        board[x][y] = marker;
    }

    public Marker getCell(int x, int y) {
        if(x < 0 || y < 0 || x >= boardSize || y >= boardSize) {
            throw new IndexOutOfBoundsException("Invalid position.");
        }

        return board[x][y];
    }
}
