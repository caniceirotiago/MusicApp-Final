/**
 * @Authors Tiago Caniceiro & Pedro Monteiro
 * @Version 1.0
 */
package src;

import src.RockStar.RockstarIncManager;
import src.RockStar.SaveFileManager;

import java.io.*;

/**
 * Classe Main que corre o programa e as suas funcionalidades.
 */
public class Main {
    public static void main(String[] args) throws IOException, ClassNotFoundException {
       SaveFileManager.run();
       RockstarIncManager gc = SaveFileManager.getRm();
        gc.run();
    }
}
