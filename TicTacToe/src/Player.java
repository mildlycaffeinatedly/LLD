public class Player {
    private String playerName;
    private Marker marker;

    public Player(String playerName, Marker marker) {
        if(playerName == null || playerName.isBlank()) {
            throw new IllegalArgumentException("Invalid name");
        }

        if(marker == null) {
            throw new IllegalArgumentException("Marker required");
        }
        
        this.playerName = playerName;
        this.marker = marker;
    }

    public String getPlayerName() {
        return playerName;
    }

    public Marker getPlayerMarker() {
        return marker;
    }
}
