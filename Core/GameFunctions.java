package byog.Core;

import java.io.File;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.FileInputStream;


public class GameFunctions implements java.io.Serializable {
    public static void saveGame(Game g) {
        File f = new File("Game.txt");
        try {
            if (!f.exists()) {
                f.createNewFile();
            }
            FileOutputStream fileOut = new FileOutputStream(f);
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(g);
            out.close();
            System.out.printf("Serialized data is saved");
            //System.exit(0);

        } catch (FileNotFoundException e) {
            System.out.println("file not found");
            //System.exit(0);
        } catch (IOException i) {
            System.out.println("hi");
            System.out.println(i);
            //System.exit(0);
        }
    }

    public static Game restoreGame() {
        File f = new File("Game.txt");
        if (f.exists()) {
            try {
                System.out.println("creating file/object input stream...");
                FileInputStream fileIn = new FileInputStream(f);
                ObjectInputStream in = new ObjectInputStream(fileIn);

                System.out.println("loading hashtable object...");

                Game g = (Game) in.readObject();
                System.out.println("game g made");
                in.close();
                return g;


            } catch (FileNotFoundException e) {
                System.out.println("file not found");
                System.exit(0);
            } catch (IOException i) {
                System.out.println("io exception");
                System.exit(0);
            } catch (ClassNotFoundException c) {
                System.out.println("Game class not found");
                System.exit(0);
            }
        }
        return new Game();
    }

}
