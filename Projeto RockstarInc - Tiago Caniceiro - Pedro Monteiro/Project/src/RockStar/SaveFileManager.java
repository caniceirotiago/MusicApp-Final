/**
 * @Authors Tiago Caniceiro & Pedro Monteiro
 * @Version 1.0
 */
package src.RockStar;

import java.io.*;
import java.util.Timer;
import java.util.TimerTask;
/**
 * Classe que gere gravação dos dados da aplicação.
 * Gere o carregamento de ficheiros guardados, grava automaticamente os ficheiros através de um temporizador
 * automático e  também no encerramento da aplicação.
 */
public class SaveFileManager {
    private static final String FILE_NAME = "mainSaveFile";
    private static RockstarIncManager rm;
    public static void run() throws IOException, ClassNotFoundException {
        loadFile();
        autoSave();
        saveOnShutdown();
    }
    public static void updateDataFile() throws IOException, ClassNotFoundException{
        FileOutputStream fos = new FileOutputStream(FILE_NAME);
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        oos.writeObject(rm);
        oos.close();
        System.out.println("File Updated");
    }
    public static void loadFile() throws IOException, ClassNotFoundException {
        try{
            FileInputStream fis = new FileInputStream(FILE_NAME);
            System.out.println("Open archive");
            ObjectInputStream ois = new ObjectInputStream(fis);
            System.out.println("File read");
            rm = (RockstarIncManager) ois.readObject();
            System.out.println("File loaded");
            ois.close();
            System.out.println("loaded file");
        }catch (FileNotFoundException e){
            rm = new RockstarIncManager();
             updateDataFile();
            System.out.println("Created file");
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            System.out.println("Load error");
        }
    }
    /**
     * Método de gravação automatica de x em x segundos.
     * Condição true cria uma thread "Daemon "que permite a gravação automática consoante um intervalo definido.
     * O delay corresponde ao tempo inicial de atraso em milissegundos em que se efectua a primeira gravação automática
     * O interval corresponde ao intervalo entre as execuções em milissegundos (exemplo: 2 minutos)
     */
    private static void autoSave(){
        Timer timer = new Timer(true);
        int delay = 1000 * 60 * 2; //
        int interval = 1000 * 60 * 2; //
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                try {
                    updateDataFile();
                    System.out.println("Auto Update executed");
                } catch (IOException e) {
                    e.printStackTrace();
                    System.out.println("Auto Update error");
                } catch (ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }
            }
        }, delay, interval);
    }
    private static void saveOnShutdown(){
        Runtime.getRuntime().addShutdownHook(new Thread(() ->{
            try{
                updateDataFile();
                System.out.println("Save on shutdown");
            } catch (IOException | ClassNotFoundException e){
                e.printStackTrace();
                System.out.println("Save on shutdown error");
            }
        }));
    }
    public static RockstarIncManager getRm() {
        return rm;
    }
}