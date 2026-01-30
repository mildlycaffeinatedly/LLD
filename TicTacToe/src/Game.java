public class Game {
    private int boardSize;
    private GameStatus gameStatus;
    private Player player1;
    private Player player2;
    private Player currentPlayer;
    private Board board;
    
    Game(int boardSize, Player player1, Player player2) {
        this.boardSize = boardSize;
        this.gameStatus = GameStatus.IN_PROGRESS;
        this.player1 = player1;
        this.player2 = player2;
        this.currentPlayer = player1;
        this.board = new Board(boardSize);
    }

    public boolean isGameActive() {
        return gameStatus == GameStatus.IN_PROGRESS;
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public boolean isGameOver() {
        return gameStatus != GameStatus.IN_PROGRESS;
    }

    public Board getBoard() {
        return board;
    }

    public void makeMove(int x, int y) {
        board.placeMarker(currentPlayer.getPlayerMarker(), x, y);
    }

    public void switchPlayer() {
        currentPlayer = (currentPlayer == player1) ? player2 : player1;
    }

    public boolean isGameDraw() {
        if(gameStatus == GameStatus.WON)
            return false;
        for(int i = 0; i < boardSize; i++) {
            for(int j = 0; j < boardSize; j++) {
                if(board.getCell(i, j) == null)
                    return false;
            }
        }
        return true;
    }

    public void makeMove(Move move) {
        board.placeMarker(move.player.getPlayerMarker(), move.x, move.y);
    }
    
    public boolean hasWinner() {
        // check rows
        for(int i = 0; i < boardSize; i++) {
            boolean flag = true;
            Marker first = board.getCell(i, 0);
            if (first == null) continue;
            for(int j = 0; j < boardSize; j++) {
                if(board.getCell(i, j) != first) {
                    flag = false;
                    break;
                }
            }
            if(flag) {
                gameStatus = GameStatus.WON;
                return true;
            }
        }

        // check columns
        for(int i = 0; i < boardSize; i++) {
            boolean flag = true;
            Marker first = board.getCell(0, i);
            if (first == null) continue;
            for(int j = 0; j < boardSize; j++) {
                if(board.getCell(j, i) != first) {
                    flag = false;
                    break;
                }
            }
            if(flag) {
                gameStatus = GameStatus.WON;
                return true;
            }
        }

        // check diagonals
        boolean flag = true;
        Marker first = board.getCell(0, 0);
        if (first != null) {
            for(int i = 0; i < boardSize; i++) {
                if(board.getCell(i, i) != first) {
                    flag = false;
                    break;
                }
            }
            if(flag) {
                gameStatus = GameStatus.WON;
                return true;
            }
        }

        flag = true;
        first = board.getCell(0, boardSize - 1);
        if (first != null) {
            for(int i = 0; i < boardSize; i++) {
                if(board.getCell(i, boardSize - i - 1) != first) {
                    flag = false;
                    break;
                }
            }
            if(flag) {
                gameStatus = GameStatus.WON;
                return true;
            }
        }
        
        return false;
    }
}
