/**
 * @Authors Tiago Caniceiro & Pedro Monteiro
 * @Version 1.0
 */
package src.RockStar;

import src.RockStar.Model.Music;
import src.RockStar.Model.MusicCollection;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Classe pesquisa que gere a lógica da pesquisa de músicas
 * Funcionalidades de encontrar músicas por nome, por Artista, álbuns de artistas e playlists públicas
 */
public class Search implements Serializable {
    private ArrayList<Music> foundMusics;
    private ArrayList<Music> foundMusicsByArtist;
    private ArrayList<MusicCollection> foundMusicCollections;
    public Search(){}
    public Search(ArrayList<Music> foundMusics, ArrayList<Music> foundMusicsByArtist, ArrayList<MusicCollection> foundMusicCollections) {
        this.foundMusics = foundMusics;
        this.foundMusicsByArtist = foundMusicsByArtist;
        this.foundMusicCollections = foundMusicCollections;
    }
    public Search(ArrayList<Music> foundMusics) {
        this.foundMusics = foundMusics;
    }
    public ArrayList<Music> getFoundMusics() {
        return foundMusics;
    }
    public ArrayList<Music> getFoundMusicsByArtist() {
        return foundMusicsByArtist;
    }
    public ArrayList<MusicCollection> getFoundMusicCollections() {
        return foundMusicCollections;
    }
}
