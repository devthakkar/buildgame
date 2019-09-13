package byog.Core;

import byog.TileEngine.TERenderer;
import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;

import java.awt.Color;
import java.awt.Font;
//import java.awt.*;
import java.util.Random;
import edu.princeton.cs.introcs.StdDraw;

public class Game implements java.io.Serializable {
    TERenderer ter = new TERenderer();
    /* Feel free to change the width and height. */
    private int defaultSeed = 12345;
    private final int WIDTH = 80;
    private final int yOffset = 3;
    private final int HEIGHT = 30 - yOffset;
    private final int boardHeight = 30;
    private TETile[][] gameBoard = new TETile[WIDTH][HEIGHT];
    private Room[] rooms;
    private int index = 0;
    private final char[] CHARACTERS = "abcdefghijklmnopqrstuvwxyz".toCharArray();
    private Player main;
    private Player two;
    private Game gameToSave = this;


    /**
     * Method used for playing a fresh game. The game should start from the main menu.
     */




    /**
     * Method used for playing a fresh game. The game should start from the main menu.
     */
    public void playWithKeyboard() {
        boolean gameStart = false;
        boolean gameOver = false;
        int inputSeed = 1234;
        String inputString = "";
        mainMenu();
        while (!gameStart) {
            if (StdDraw.hasNextKeyTyped()) {
                char keyPress = StdDraw.nextKeyTyped();
                if (keyPress == 'n') {
                    inputSeed = inputSeed();
                    gameStart = true;
                    TERenderer ter1 = new TERenderer();
                    ter1.initialize(WIDTH, boardHeight, 0, yOffset);
                    worldGenerator(inputSeed);
                    ter1.renderFrame(gameBoard);
                } else if (keyPress == 'l') {
                    gameStart = true;
                    Game temp = GameFunctions.restoreGame();
                    this.gameBoard = temp.getGameBoard();
                    assignPlayer(temp);
                    TERenderer ter3 = new TERenderer();
                    ter3.initialize(temp.WIDTH, temp.boardHeight, 0, temp.yOffset);
                    ter3.renderFrame(gameBoard);
                } else if (keyPress == 'q') {
                    System.exit(0);
                }
            }
        }
        drawHeader();
        String oldDisplay = " ";
        while (!gameOver) {
            if (StdDraw.hasNextKeyTyped()) {
                char keyPress = StdDraw.nextKeyTyped();
                inputString += keyPress;
                if (keyPress == 'w') {
                    moveMainDirection(0);
                } else if (keyPress == 's') {
                    moveMainDirection(1);
                } else if (keyPress == 'a') {
                    moveMainDirection(3);
                } else if (keyPress == 'd') {
                    moveMainDirection(2);
                } else if (keyPress == 'i') {
                    moveSecondDirection(0);
                } else if (keyPress == 'k') {
                    moveSecondDirection(1);
                } else if (keyPress == 'j') {
                    moveSecondDirection(3);
                } else if (keyPress == 'l') {
                    moveSecondDirection(2);
                }
                if (inputString.contains(":q")) {
                    GameFunctions.saveGame(this);
                    System.exit(0);
                }
            }
            int mouseX = (int) StdDraw.mouseX();
            int mouseY = (int) StdDraw.mouseY();
            String toDisplay = " ";
            if (getGameTile(mouseX, mouseY - yOffset).equals(Tileset.NOTHING)) {
                toDisplay = "Nothing: nothing to see here";
            } else if (getGameTile(mouseX, mouseY - yOffset).equals(Tileset.FLOOR)) {
                toDisplay = "Floor: the floor is lava! Just kidding.";
            } else if (getGameTile(mouseX, mouseY - yOffset).equals(Tileset.WALL)) {
                toDisplay = "Wall: tall, hard, and cold to the touch";
            } else if (getGameTile(mouseX, mouseY - yOffset).equals(Tileset.PLAYER)) {
                toDisplay = "Player One: Don't @ me";
            } else if (getGameTile(mouseX, mouseY - yOffset).equals(Tileset.PLAYER2)) {
                toDisplay = "Player Two: who cares about player two";
            }
            if (oldDisplay != toDisplay) {
                updateDisplay(" ");
                updateDisplay(toDisplay);
            }
            oldDisplay = toDisplay;
        }
    }

    /**
     * Method used for autograding and testing the game code. The input string will be a series
     * of characters (for example, "n123sswwdasdassadwas", "n123sss:q", "lwww". The game should
     * behave exactly as if the user typed these characters into the game after playing
     * playWithKeyboard. If the string ends in ":q", the same world should be returned as if the
     * string did not end with q. For example "n123sss" and "n123sss:q" should return the same
     * world. However, the behavior is slightly different. After playing with "n123sss:q", the game
     * should save, and thus if we then called playWithInputString with the string "l", we'd expect
     * to get the exact same world back again, since this corresponds to loading the saved game.
     * @param input the input string to feed to your program
     * @return the 2D TETile[][] representing the state of the world
     */
    public TETile[][] playWithInputString(String input) {
        // and return a 2D tile representation of the world that would have been
        // drawn if the same inputs had been given to playWithKeyboard().
        /*
        TERenderer ter2 = new TERenderer();
        ter2.initialize(WIDTH, boardHeight, 0, yOffset);
         */
        String firstLetter = input.substring(0, 1);
        String rest = input.substring(1, input.length());
        String seedString;
        char[] restCharArray = rest.toCharArray();
        if (firstLetter.equals("n")) {
            int startPoint = restCharArray.length;
            for (int i = restCharArray.length - 1; i > 0; i--) {
                if (restCharArray[i] == ('s')) {
                    startPoint = i;
                }
            }
            seedString = input.substring(1, startPoint);
            rest = input.substring(startPoint + 1, input.length());
            long seed = Long.parseLong(seedString);
            worldGenerator(seed);
            restCharArray = rest.toCharArray();
            for (int j = 0; j < restCharArray.length; j++) {
                if (restCharArray[j] == 'w') {
                    main.playerMovement(0);
                } else if (restCharArray[j] == 's') {
                    main.playerMovement(1);
                } else if (restCharArray[j] == 'a') {
                    main.playerMovement(3);
                } else if (restCharArray[j] == 'd') {
                    main.playerMovement(2);
                } else if (restCharArray[j] == ':') {
                    if (restCharArray[j + 1] == 'q') {
                        GameFunctions.saveGame(this);
                    }
                }
            }
            //ter2.renderFrame(gameBoard);
        } else if (firstLetter.equals("l")) {
            Game temp = GameFunctions.restoreGame();
            this.gameBoard = temp.getGameBoard();
            assignPlayer(temp);
            for (int j = 0; j < restCharArray.length; j++) {
                if (restCharArray[j] == 'w') {
                    main.playerMovement(0);
                } else if (restCharArray[j] == 's') {
                    main.playerMovement(1);
                } else if (restCharArray[j] == 'a') {
                    main.playerMovement(3);
                } else if (restCharArray[j] == 'd') {
                    main.playerMovement(2);
                } else if (restCharArray[j] == ':') {
                    if (restCharArray[j + 1] == 'q') {
                        GameFunctions.saveGame(this);
                    }
                }
            }
        }

        /*
        boolean ifSave = false;
        if (input.substring(input.length() - 2, input.length()).equals(new String(":q"))) {
            ifSave = true;
            rest = input.substring(1, input.length() - 2);
        }
        */


        //ter.initialize(WIDTH, HEIGHT);

        return gameBoard;
    }

    /* creates a randomized world based on the project specifications */
    private void worldGenerator(long seed) {
        nothingFloor();
        Random num = new Random(seed);
        int numRooms = RandomUtils.uniform(num, 10, 20);
        rooms = new Room[numRooms];
        for (int i = 0; i < numRooms; i++) {
            randomRoomGenerator(num);
        }
        //ter.renderFrame(gameBoard);

        for (Room t: rooms) {
            if (!t.getConnected()) {
                Room random = rooms[RandomUtils.uniform(num, 0, numRooms)];
                while (t == random) {
                    random = rooms[RandomUtils.uniform(num, 0, numRooms)];
                }
                makeHallway(t, random, num);
                //ter.renderFrame(gameBoard);
                random.changeConnected();
                t.changeConnected();
            }
        }


        /*
        for (int x = 0; x < numRooms; x++) {
            if (x == numRooms - 1) {
                makeHallway(rooms[x], rooms[0], num);
            } else {
                makeHallway(rooms[x], rooms[x + 1], num);
            }
        }
        */

        for (Room room: rooms) {
            room.fillExits(this);
        }
        int[] playerCor = playerSpawnLocation(num);
        int[] playerTwoCor = playerSpawnLocation(num);
        main = new Player(playerCor[0], playerCor[1], 1, this);
        two = new Player(playerTwoCor[0], playerTwoCor[1], 2, this);


        // makeHallway(rooms[0], rooms[1], num);
        // makeHallway(rooms[2], rooms[3], num);
    }

    /* sets the entire gameBoard to nothing tiles. to be used upon instantiation of the gameBoard */
    public void nothingFloor() {
        for (int x = 0; x < WIDTH; x++) {
            for (int y = 0; y < HEIGHT; y++) {
                gameBoard[x][y] = Tileset.NOTHING;
            }
        }
    }

    /* creates a room of random size with wall tiles and fills it in with floor tiles */
    public void randomRoomGenerator(Random gen) {
        int maxSize = WIDTH / 10;
        int height = RandomUtils.uniform(gen, 4, maxSize);
        int width = RandomUtils.uniform(gen, 4, maxSize);
        int xCor = RandomUtils.uniform(gen, 1, WIDTH - 1);
        int yCor = RandomUtils.uniform(gen, 1, WIDTH - 1);
        if (!roomLegitChecker(xCor, yCor, width, height)) {
            randomRoomGenerator(gen);
        } else {
            Room saucito = new Room(xCor, yCor, height, width, index, gen, this);
            rooms[index] = saucito;
            index += 1;
        }

    }

    /* checks if room can be created without going out of bounds or overlapping existing rooms */
    private boolean roomLegitChecker(int xCor, int yCor, int width, int height) {
        if (xCor + width > WIDTH - 1 || yCor + height > HEIGHT - 1) {
            return false;
        }
        for (int x = xCor; x < xCor + width; x++) {
            for (int y = yCor; y < yCor + height; y++) {
                if (gameBoard[x][y] != Tileset.NOTHING) {
                    return false;
                }
            }
        }
        return true;
    }

    /* changes the tiles in gameBoard to create a room */
    public void roomCreator(int xCor, int yCor, int width, int height) {
        for (int x = xCor; x < xCor + width; x++) {
            for (int y = yCor; y < yCor + height; y++) {
                gameBoard[x][y] = Tileset.WALL;
            }
        }
        for (int a = xCor + 1; a < xCor + width - 1; a++) {
            for (int b = yCor + 1; b < yCor + height - 1; b++) {
                gameBoard[a][b] = Tileset.FLOOR;
            }
        }
    }

    // creates a hallway between two rooms

    public void makeHallway(Room one, Room second, Random gen) {
        int[] start = one.randomExit(gen);
        int[] end = second.randomExit(gen);
        new Hallway(start[0], start[1], end[0], end[1], this);
    }

    public void setGameBoard(int xCor, int yCor, TETile tile) {
        gameBoard[xCor][yCor] = tile;
    }

    public TETile getGameTile(int xCor, int yCor) {
        return gameBoard[xCor][yCor];
    }

    private void mainMenu() {
        StdDraw.setCanvasSize(WIDTH * 16, boardHeight * 16);
        Font titleFont = new Font("Monaco", Font.BOLD, 30);
        Font otherFont = new Font("Monaco", Font.PLAIN, 15);
        StdDraw.setFont(titleFont);
        StdDraw.setXscale(0, WIDTH);
        StdDraw.setYscale(0, boardHeight);
        StdDraw.clear(Color.black);
        StdDraw.setPenColor(Color.white);
        StdDraw.text(WIDTH / 2, (boardHeight * 3) / 4, "Dev and Theo's Amazing Game");
        StdDraw.setFont(otherFont);
        StdDraw.text(WIDTH / 2, (boardHeight / 2), "New Game (N)");
        StdDraw.text(WIDTH / 2, (boardHeight * 3) / 8, "Load Game (L)");
        StdDraw.text(WIDTH / 2, (boardHeight / 4), "Quit (Q)");
        StdDraw.show();
    }

    private void updateDisplay(String word) {
        drawHeader();
        Font displayFont = new Font("Monaco", Font.PLAIN, 10);
        StdDraw.setFont(displayFont);
        StdDraw.setPenColor(Color.black);
        StdDraw.text(10, boardHeight - 0.75, word);
        StdDraw.show();
    }

    public int inputSeed() {
        Font questionFont = new Font("Monaco", Font.PLAIN, 20);
        StdDraw.setFont(questionFont);
        StdDraw.clear(Color.black);
        StdDraw.setPenColor(Color.white);
        StdDraw.text(WIDTH / 2, boardHeight / 2, "Please input a seed");
        StringBuilder seed = new StringBuilder();
        boolean endSeed = false;
        while (!endSeed) {
            if (StdDraw.hasNextKeyTyped()) {
                char nextKey = StdDraw.nextKeyTyped();
                if (nextKey == 's') {
                    endSeed = true;
                } else {
                    seed.append((char) nextKey);
                }
            }
        }
        if (seed.length() > 0) {
            String seedString = seed.toString();
            int trueSeed = Integer.valueOf(seedString);
            return trueSeed;
        } else {
            return defaultSeed;
        }

    }

    private int[] playerSpawnLocation(Random gen) {
        int xCor = RandomUtils.uniform(gen, 0, WIDTH);
        int yCor = RandomUtils.uniform(gen, 0, HEIGHT);
        while (getGameTile(xCor, yCor) != Tileset.FLOOR) {
            xCor = RandomUtils.uniform(gen, 0, WIDTH);
            yCor = RandomUtils.uniform(gen, 0, HEIGHT);
        }
        return new int[] {xCor, yCor};
    }

    private void drawHeader() {
        StdDraw.setPenColor(Color.white);
        StdDraw.filledRectangle(WIDTH / 2, boardHeight - 0.5, WIDTH / 2, 0.5);
        StdDraw.show();
    }

    public TETile[][] getGameBoard() {
        return this.gameBoard;
    }

    public void assignPlayer(Game loadedGame) {
        /*
        TETile [][] loadedGameBoard = loadedGame.getGameBoard();
        int[] playerOne = new int[] {0, 0};
        int[] playerTwo = new int[] {0, 0};
        for (int x = 0; x < loadedGame.WIDTH; x++) {
            for (int y = 0; y < loadedGame.HEIGHT; y++) {
                if (getGameTile(x, y) == Tileset.PLAYER) {
                    System.out.println("testing nibba");
                    playerOne = new int[] {x, y};
                }
                if (getGameTile(x, y) == Tileset.PLAYER2) {
                    playerTwo = new int[] {x, y};
                }
            }
        }

        main = new Player(playerOne[0], playerOne[1], 1, loadedGame);
        two = new Player(playerTwo[0], playerTwo[1], 1, loadedGame);
        */

        main = new Player(loadedGame.main.getXCor(), loadedGame.main.getYCor(), 1, this);
        two = new Player(loadedGame.two.getXCor(), loadedGame.two.getYCor(), 2, this);

    }

    public int getWIDTH() {
        return WIDTH;
    }

    public int getHEIGHT() {
        return HEIGHT;
    }

    private void moveMainDirection(int direction) {
        main.playerMovement(direction);
        ter.renderFrame(gameBoard);
        drawHeader();
    }

    private void moveSecondDirection(int direction) {
        two.playerMovement(direction);
        ter.renderFrame(gameBoard);
        drawHeader();
    }



}
