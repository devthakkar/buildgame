package byog.Core;

import byog.TileEngine.Tileset;

import java.util.Random;

public class Room implements java.io.Serializable {
    private int height;
    private int width;
    private int xCor;
    private int yCor;
    private boolean connected;
    private int index;
    private int numExits;
    private int[] wallSideExits;
    private int[][] exitCoordinates;
    private Game g;

    public Room(int xCor, int yCor, int height, int width, int index, Random gen, Game g) {
        this.xCor = xCor;
        this.yCor = yCor;
        this.height = height;
        this.width = width;
        this.index = index;
        this.g = g;
        numExits = RandomUtils.uniform(gen, 1, 4);
        wallSideExits = new int[numExits];
        exitCoordinates = new int[numExits][2];
        for (int x = xCor; x < xCor + width; x++) {
            for (int y = yCor; y < yCor + height; y++) {
                g.setGameBoard(x, y, Tileset.WALL);
                //Game.gameBoard[x][y] = Tileset.WALL;
            }
        }
        for (int a = xCor + 1; a < xCor + width - 1; a++) {
            for (int b = yCor + 1; b < yCor + height - 1; b++) {
                g.setGameBoard(a, b, Tileset.FLOOR);
                //Game.gameBoard[a][b] = Tileset.FLOOR;
            }
        }
        for (int i = 0; i < numExits; i++) {
            int wallSide = RandomUtils.uniform(gen, 1, 5);
            while (containsInt(wallSide, wallSideExits)) {
                wallSide = RandomUtils.uniform(gen, 1, 5);
            }
            wallSideExits[i] = wallSide;
            exitCoordinates[i] = randomWall(gen, wallSide);
        }
    }

    /* return index */
    public int getIndex() {
        return index;
    }

    /* returns exit coordinates */
    public int[][] getExitCoordinates() {
        return exitCoordinates;
    }

    /* returns the coordinates of 1 random wall tiles of the room */
    public int[] randomWall(Random gen, int wallSide) {
        //int wallSide = RandomUtils.uniform(gen, 1, 4);
        //1 is left, 2 is top, 3 is right, 4 is bottom
        int[] coordinatePair = new int[2];
        if (wallSide == 1) {
            int firstY = RandomUtils.uniform(gen, yCor + 1, yCor + height - 1);
            coordinatePair[0] = xCor;
            coordinatePair[1] = firstY;
        } else if (wallSide == 2) {
            int firstX = RandomUtils.uniform(gen, xCor + 1, xCor + width - 1);
            coordinatePair[0] = firstX;
            coordinatePair[1] = yCor + height - 1;
        } else if (wallSide == 3) {
            int secondY = RandomUtils.uniform(gen, yCor + 1, yCor + height - 1);
            coordinatePair[0] = xCor + width - 1;
            coordinatePair[1] = secondY;
        } else if (wallSide == 4) {
            int secondX = RandomUtils.uniform(gen, xCor + 1, xCor + width - 1);
            coordinatePair[0] = secondX;
            coordinatePair[1] = yCor;
        }
        //Game.setGameBoard(coordinatePair[0], coordinatePair[1], Tileset.FLOOR);
        //Game.gameBoard[coordinatePair[0]][coordinatePair[1]] = Tileset.FLOOR;
        return coordinatePair;
    }

    private boolean containsInt(int x, int[] y) {
        if (y == null) {
            return false;
        }
        for (int item: y) {
            if (item == x) {
                return true;
            }
        }
        return false;
    }

    // chooses a random exit from list of exits
    public int[] randomExit(Random gen) {
        return exitCoordinates[RandomUtils.uniform(gen, 0, numExits)];
    }

    public void fillExits(Game game) {
        this.g = game;
        for (int[] exit: exitCoordinates) {
            //boolean one = Game.gameBoard[exit[0] + 1][exit[1]] == Tileset.NOTHING;
            boolean one = game.getGameTile(exit[0] + 1, exit[1]) == Tileset.NOTHING;
            //boolean two = Game.gameBoard[exit[0] - 1][exit[1]] == Tileset.NOTHING;
            boolean two = game.getGameTile(exit[0] - 1, exit[1]) == Tileset.NOTHING;
            //boolean three = Game.gameBoard[exit[0]][exit[1] + 1] == Tileset.NOTHING;
            boolean three = game.getGameTile(exit[0], exit[1] + 1) == Tileset.NOTHING;
            //boolean four = Game.gameBoard[exit[0]][exit[1] - 1] == Tileset.NOTHING;
            boolean four = game.getGameTile(exit[0], exit[1] - 1) == Tileset.NOTHING;
            if (one || two || three || four) {
                //Game.gameBoard[exit[0]][exit[1]] = Tileset.WALL;
                g.setGameBoard(exit[0], exit[1], Tileset.WALL);
            }
        }
    }

    public boolean getConnected() {
        return this.connected;
    }

    public void changeConnected() {
        if (getConnected()) {
            connected = false;
        } else {
            connected = true;
        }
    }
}

