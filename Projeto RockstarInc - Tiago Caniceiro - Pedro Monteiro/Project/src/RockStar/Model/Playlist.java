/**
 * @Authors Tiago Caniceiro & Pedro Monteiro
 * @Version 1.0
 */
package src.RockStar.Model;

import src.RockStar.Model.Client;
import src.RockStar.Model.Music;
import src.RockStar.Model.MusicCollection;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Classe que gere a construção de uma coleção de música/playlist associada ao utilizador Cliente
 */
public class Playlist extends MusicCollection implements Serializable {
    private Boolean isPublic;
    private Client clientCreator;
    public Playlist() {}
    /**
     * Construtor da playlist do Cliente
     * @param name nome da playlist
     * @param clientCreator nome do utilizador da classe Cliente
     */
    public Playlist(String name, Client clientCreator) {
        //Creation of empty playlist
        super(name);
        this.isPublic = true;
        this.clientCreator = clientCreator;
    }
    /**
     * Construtor de uma playlist aleatória.
     * @param name nome da playlist.
     * @param clientCreator nome do utilizador da classe Cliente.
     * @param musicList lista de músicas a serem inseridas na playlist aleatória criada.
     */
    public Playlist(String name, Client clientCreator, ArrayList<Music> musicList) {
        //Creation of random playlist or temporary Gui Playlist
        super(name, musicList);
        this.isPublic = true;
        this.clientCreator = clientCreator;
    }
    public Client getClientCreator() {
        return clientCreator;
    }
    public Boolean getPublicState() {
        return isPublic;
    }
    public void setPublicState(Boolean aPublic) {
        isPublic = aPublic;
    }
    public void addMusicToCollection(Music music) {
        musicList.add(music);
    }
    public void removeMusicFromCollection(Music music) {
        musicList.remove(music);
    }
}
