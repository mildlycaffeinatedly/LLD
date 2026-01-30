import java.util.Scanner;

public class GameRunner {
    private Game game;
    private Scanner scanner;
    
    public GameRunner() {
        this.scanner = new Scanner(System.in);
    }

    public void initializeGame() {
        System.out.print("Enter the board size: ");
        int boardSize = Integer.parseInt(scanner.nextLine());

        System.out.print("Player1: Enter your name: ");
        String player1Name = scanner.nextLine();

        System.out.print("Player1: Enter your marker(X or O): ");
        String marker1String = scanner.nextLine().trim().toUpperCase();
        Marker player1Marker = marker1String.equals("X") ? Marker.X : Marker.O;

        Player player1 = new Player(player1Name, player1Marker);

        System.out.print("Player2: Enter your name: ");
        String player2Name = scanner.nextLine();

        System.out.print("Player2: Enter your marker(X or O): ");
        String marker2String = scanner.nextLine().trim().toUpperCase();
        Marker player2Marker = marker2String.equals("X") ? Marker.X : Marker.O;

        Player player2 = new Player(player2Name, player2Marker);

        this.game = new Game(boardSize, player1, player2);
    }

    public void startGame() {
        if (game == null) {
            throw new IllegalStateException("Game not initialized. Call initializeGame() first.");
        }
        
        while(game.isGameActive()) {
            game.getBoard().showBoard();
            
            Player currentPlayer = game.getCurrentPlayer();
            System.out.print(currentPlayer.getPlayerName() + " : Enter move row: ");
            int x = scanner.nextInt();
            System.out.print(currentPlayer.getPlayerName() + " : Enter move column: ");
            int y = scanner.nextInt();

            try {
                game.makeMove(x, y);
            } catch (Exception e) {
                System.out.println("Invalid move: " + e.getMessage());
                continue;
            }

            if(game.hasWinner()) {
                game.getBoard().showBoard();
                System.out.println(currentPlayer.getPlayerName() + " won the game.");
                break;
            }

            if(game.isGameDraw()) {
                game.getBoard().showBoard();
                System.out.println("Match was a draw.");
                break;
            }

            game.switchPlayer();
        }
        
        scanner.close();
    }
}
