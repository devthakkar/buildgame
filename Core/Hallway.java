package byog.Core;

import byog.TileEngine.Tileset;

public class Hallway implements java.io.Serializable {
    private int currentX;
    private int currentY;
    private int startX;
    private int startY;
    private int targetX;
    private int targetY;
    private int xDir;
    private int yDir;
    private boolean floorOnly;
    private Game g;

    public Hallway(int startX, int startY, int targetX, int targetY, Game g) {
        currentX = startX;
        currentY = startY;
        this.targetX = targetX;
        this.targetY = targetY;
        this.startX = startX;
        this.startY = startY;
        this.xDir = directionSetter(targetX - startX);
        this.yDir = directionSetter(targetY - startY);
        this.g = g;
        //Game.gameBoard[startX][startY] = Tileset.FLOOR;
        //Game.setGameBoard(startX, startY, Tileset.FLOOR);
        //Game.gameBoard[targetX][targetY] = Tileset.FLOOR;
        //Game.setGameBoard(targetX, targetY, Tileset.FLOOR);
        hallwayPaver(g);
    }


    // real paver
    private void hallwayPaver(Game game) {
        xPaver(game);
        createCorner(game);
        yPaver(game);
        if (game.getGameTile(currentX, currentY) == Tileset.WALL) {
            game.setGameBoard(currentX, currentY, Tileset.FLOOR);
        }
        /*
        if(Game.getGameTile(currentX,currentY) == Tileset.NOTHING) {
            Game.setGameBoard(currentX, currentY, Tileset.WALL);
        }
        */

    }

    private void xPaver(Game game) {
        while (currentX != targetX && currentX > 1) {
            if (game.getGameTile(currentX + xDir, currentY) == Tileset.NOTHING) {
                floorOnly = false;
                paveX(game);
                currentX += xDir;
            } else {
                floorOnly = true;
                paveX(game);
                currentX += xDir;
            }
        }
    }

    private void yPaver(Game game) {

        while (currentY != targetY  && currentY < game.getHEIGHT() - 1) {
            if (game.getGameTile(currentX, currentY + yDir) == Tileset.NOTHING) {
                floorOnly = false;
                paveY(game);
                currentY += yDir;
            } else {
                floorOnly = true;
                paveY(game);
                currentY += yDir;
            }
        }
        if (game.getGameTile(currentX - 1, currentY) == Tileset.NOTHING) {
            game.setGameBoard(currentX - 1, currentY, Tileset.WALL);
        }
        if (game.getGameTile(currentX + 1, currentY) == Tileset.NOTHING) {
            game.setGameBoard(currentX + 1, currentY, Tileset.WALL);
        }

        if (currentY > g.getHEIGHT() - 2) {
            game.setGameBoard(currentX, currentY - 1, Tileset.WALL);
        }


        //Game.gameBoard[currentX][currentY - 1] = Tileset.WALL;
        //Game.setGameBoard(currentX, currentY - 1, Tileset.WALL);
        //Game.setGameBoard(currentX, currentY, Tileset.WALL);

    }


    private void createCorner(Game game) {
        //Game.gameBoard[currentX][currentY] = Tileset.FLOOR;
        game.setGameBoard(currentX, currentY, Tileset.FLOOR);
        //Game.gameBoard[currentX + xDir][currentY] = Tileset.WALL;
        if (game.getGameTile(currentX + xDir, currentY) == Tileset.NOTHING) {
            game.setGameBoard(currentX + xDir, currentY, Tileset.WALL);
        }
        //Game.gameBoard[currentX][currentY - yDir] = Tileset.WALL;
        if (game.getGameTile(currentX, currentY - yDir) == Tileset.NOTHING) {
            game.setGameBoard(currentX, currentY - yDir, Tileset.WALL);
        }
        //Game.gameBoard[currentX + xDir][currentY - yDir] = Tileset.WALL;
        if (game.getGameTile(currentX + xDir, currentY - yDir) == Tileset.NOTHING) {
            game.setGameBoard(currentX + xDir, currentY - yDir, Tileset.WALL);
        }
        currentY += yDir;
    }

    //paves hallway in vertical direction
    private void paveY(Game game) {
        if (currentX < game.getWIDTH()) {
            if (floorOnly) {
                if (game.getGameTile(currentX + 1, currentY) == Tileset.NOTHING) {
                    game.setGameBoard(currentX + 1, currentY, Tileset.WALL);
                    //Game.gameBoard[currentX + 1][currentY] = Tileset.WALL;
                }
                if (game.getGameTile(currentX - 1, currentY) == Tileset.NOTHING) {
                    game.setGameBoard(currentX - 1, currentY, Tileset.WALL);
                    //Game.gameBoard[currentX - 1][currentY + 1] = Tileset.WALL;
                }

                //Game.gameBoard[currentX][currentY] = Tileset.FLOOR;
                game.setGameBoard(currentX, currentY, Tileset.FLOOR);
            } else {
                //Game.gameBoard[currentX][currentY] = Tileset.FLOOR;
                //Game.gameBoard[currentX - 1][currentY] = Tileset.WALL;
                //Game.gameBoard[currentX + 1][currentY] = Tileset.WALL;
                game.setGameBoard(currentX, currentY, Tileset.FLOOR);
                game.setGameBoard(currentX - 1, currentY, Tileset.WALL);
                game.setGameBoard(currentX + 1, currentY, Tileset.WALL);
            }
        }

    }

    //paves hallway in horizontal direction
    private void paveX(Game game) {
        if (currentY < game.getHEIGHT()) {
            if (floorOnly) {
                if (game.getGameTile(currentX, currentY + 1) == Tileset.NOTHING) {
                    game.setGameBoard(currentX, currentY + 1, Tileset.WALL);
                }
                if (game.getGameTile(currentX, currentY - 1) == Tileset.NOTHING) {
                    game.setGameBoard(currentX, currentY - 1, Tileset.WALL);
                }

                //Game.gameBoard[currentX][currentY] = Tileset.FLOOR;
                game.setGameBoard(currentX, currentY, Tileset.FLOOR);
            } else {
                //Game.gameBoard[currentX][currentY] = Tileset.FLOOR;
                //Game.gameBoard[currentX][currentY + 1] = Tileset.WALL;
                //Game.gameBoard[currentX][currentY - 1] = Tileset.WALL;
                game.setGameBoard(currentX, currentY, Tileset.FLOOR);
                game.setGameBoard(currentX, currentY + 1, Tileset.WALL);
                game.setGameBoard(currentX, currentY - 1, Tileset.WALL);
            }
        }
    }

    //returns 1 if positive direction, -1 if negative direction
    private int directionSetter(int distance) {
        if (distance < 0) {
            return -1;
        } else {
            return 1;
        }
    }

}
