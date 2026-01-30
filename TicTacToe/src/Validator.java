public class Validator {
    

    Validator(Game game, Move move) {

    }

    // validate if a move is valid
    public boolean isValid(Game game, Move move) {
        // validate if the game is active
        if(!game.isGameActive()) {
            return false; // placeholder: add more conditions later
        }
        return true; // placeholder: always valid for now
    }
}
