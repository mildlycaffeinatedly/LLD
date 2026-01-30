public class Move {
    final int x;
    final int y;
    final Player player;

    Move(Player player, int x, int y) {
        this.player = player;
        this.x = x;
        this.y = y;
    }
}
