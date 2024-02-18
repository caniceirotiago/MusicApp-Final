/**
 * @Authors Tiago Caniceiro & Pedro Monteiro
 * @Version 1.0
 */
package src.RockStar.Model;

import src.RockStar.Model.Music;
import src.RockStar.Model.MusicCollection;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Classe abstrata de utilizador do sistema
 * Métodos abstratos que são implementados nas classes Client e Music Creator
 */
public abstract class User implements Serializable {
    protected String username;
    protected String password;
    protected String name;
    protected String email;
    protected ArrayList<Music> allMusic;
    protected ArrayList<MusicCollection> allCollections;
    public ArrayList<Music> getAllMusic() {
        return allMusic;
    }
    public String getUsername() {
        return username;
    }
    public String getPassword() {
        return password;
    }
    public String getEmail() {
        return email;
    }
    public String getName() {
        return name;
    }
    public ArrayList<MusicCollection> getAllCollections() {
        return allCollections;
    }
    /**
     * Criação de novo utilizador
     * @param name Nome do utilizador
     * @param username Username do utilizador
     * @param password Password do utilizador
     * @param email Email do utilizador
     */
    public User(String name, String username, String password, String email) {
        this.username = username;
        this.password = password;
        this.name = name;
        this.email = email;
        this.allMusic = new ArrayList<>();
        this.allCollections = new ArrayList<>();
    }
    public abstract void newCollection(String name);
    public abstract void newCollection(ArrayList<Music> listMusic);
    public abstract void addMusicToCollection(Music music, MusicCollection musicCollection);
    public abstract void newMusicToUserMainCollection(Music music);
    public abstract void removeMusicFromCollection(Music music, MusicCollection collection);
    public abstract void removeMusicCollection(MusicCollection collection);
}
