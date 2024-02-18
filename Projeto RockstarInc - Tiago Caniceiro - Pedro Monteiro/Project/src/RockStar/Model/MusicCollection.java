/**
 * @Authors Tiago Caniceiro & Pedro Monteiro
 * @Version 1.0
 */
package src.RockStar.Model;

import src.RockStar.Model.Music;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;

/**
 * Classe abstrata das coleções de música.
 * Serve de base para as playlists do cliente e o álbum do criador de música
 */
public abstract class MusicCollection implements Serializable {
    protected String name;
    protected ArrayList<Music> musicList;
    protected LocalDateTime creationDate;
    public MusicCollection() {}
    /**
     * Construtor de uma coleção de músicas vazia.
     * @param name Define o nome da coleção.
     */
    public MusicCollection(String name) {
        this.name = name;
        this.musicList = new ArrayList<>();
        this.creationDate = LocalDateTime.now();
    }
    /**
     * Construtor de uma coleção de música com nome e uma lista de músicas associadas.
     * Utilizado na criação de playlists ou álbums.
     * @param name Nome da playlist.
     * @param musicList Lista de músicas a inserir na coleção.
     */
    public MusicCollection(String name, ArrayList<Music> musicList) {
        this.name = name;
        this.musicList = musicList;
        this.creationDate = LocalDateTime.now();
    }
    public String getName() {
        return name;
    }
    public ArrayList<Music> getMusicList() {
        return musicList;
    }
    public abstract void addMusicToCollection(Music music);
    public abstract void removeMusicFromCollection(Music music);
    @Override
    public String toString() {
        return name;
    }
}
