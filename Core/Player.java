package byog.Core;

import byog.TileEngine.Tileset;
import byog.TileEngine.TETile;



public class Player implements java.io.Serializable {
    private int xCor;
    private int yCor;
    private TETile currentTile = Tileset.FLOOR;
    private int player;
    private Game g;

    public Player(int xCor, int yCor, int player, Game g) {
        this.xCor = xCor;
        this.yCor = yCor;
        this.player = player;
        this.g = g;
        g.setGameBoard(xCor, yCor, playerAvatar(player));
    }

    // 0 is up, 1 is down, 2 is right, 3 is left
    public void playerMovement(int direction) {
        if (direction == 0) {
            if (g.getGameTile(xCor, yCor + 1).equals(Tileset.FLOOR)) {
                g.setGameBoard(xCor, yCor + 1, playerAvatar(player));
                g.setGameBoard(xCor, yCor, currentTile);
                yCor += 1;
            }
        } else if (direction == 1) {
            if (g.getGameTile(xCor, yCor - 1).equals(Tileset.FLOOR)) {
                g.setGameBoard(xCor, yCor - 1, playerAvatar(player));
                g.setGameBoard(xCor, yCor, currentTile);
                yCor -= 1;
            }
        } else if (direction == 3) {
            if (g.getGameTile(xCor - 1, yCor).equals(Tileset.FLOOR)) {
                g.setGameBoard(xCor - 1, yCor, playerAvatar(player));
                g.setGameBoard(xCor, yCor, currentTile);
                xCor -= 1;
            }
        } else if (direction == 2) {
            if (g.getGameTile(xCor + 1, yCor).equals(Tileset.FLOOR)) {
                g.setGameBoard(xCor + 1, yCor, playerAvatar(player));
                g.setGameBoard(xCor, yCor, currentTile);
                xCor += 1;
            }
        }
    }

    public TETile playerAvatar(int playerNumber) {
        if (playerNumber == 1) {
            return Tileset.PLAYER;
        } else {
            return Tileset.PLAYER2;
        }
    }

    public int getXCor() {
        System.out.println(xCor);
        return xCor;
    }

    public int getYCor() {
        return yCor;
    }
}
