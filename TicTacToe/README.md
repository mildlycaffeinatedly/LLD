// Tic-tac-toe design

// Entities:
    - Player
    - Game
    - Board
    - Move

// Responsibilites:


enum Marker {
    X,
    O
}


Player:
    Fields:
        - playerName : String
        - marker : Marker
    Methods:
        - getPlayerName(): String
        - setPlayerName(String)
        - getPlayerMarker(): Marker
        - setPlayerMarker(Marker)
        - Player() // constructor


Board:
    Fields:
        - board : ArrayList<ArrayList<Marker>>
        - player1 : Player
        - player2 : Player
        - currentPlayer : Player
        - boardSize : int
    Methods:
        - Board(int)
        - showBoard()
        - assignPlayers(Player, Player)
        - getCurrentPlayer(): Player
        - makeMove(Player, int, int)
        - isGameOver(): boolean
        - hasWinner(): boolean

Game:
    Fields:
        - boardSize : int
    Methods:
        - Game(int)
        - startGame()

Main:
    Methods:
        - main(String[] args)


// Not implemented in code but suggested:
GameStatus:
    IN_PROGRESS
    WON
    DRAW
    ABORTED

// Not implemented in code but suggested:
Move:
    - row : int
    - col : int
    - player : Player
