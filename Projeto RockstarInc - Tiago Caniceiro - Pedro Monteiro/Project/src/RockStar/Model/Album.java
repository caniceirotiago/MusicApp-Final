/**
 * @Authors Tiago Caniceiro & Pedro Monteiro
 * @Version 1.0
 */
package src.RockStar.Model;
import src.RockStar.Genre;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Classe para criação de coleções de música do tipo "Album" associadas ao utilizador Music Creator
 */
public class Album extends MusicCollection implements Serializable {
    private MusicCreator mainCreator;
    private Genre.GENRE mainGenre;
    public Album(){}
    /**
     * Construtor de um album vazio
     * @param name nome do album
     * @param mainCreator nome do criador de música associado
     */
    public Album(String name, MusicCreator mainCreator) {
        //Creation of empty album
        super(name);
        this.mainCreator = mainCreator;
    }
    /**
     * Construtor de um album com uma lista de músicas já integradas
     * @param name Nome do album
     * @param musicCreator nome do criador de música
     * @param musicList lista de músicas a juntar ao album
     */
    public Album(String name, MusicCreator musicCreator, ArrayList<Music> musicList) {
        //Criação de um álbum com músicas
        super(name, musicList);
        this.mainCreator = musicCreator;
    }
    public MusicCreator getCreator() {
        return mainCreator;
    }
    public Genre.GENRE getMainGenre() {
        return mainGenre;
    }
    public void addMusicToCollection(Music music) {
        musicList.add(music);
        calculateMainGenre();
    }
    public void removeMusicFromCollection(Music music) {
        musicList.remove(music);
        calculateMainGenre();
    }
    /**
     * Método para calcular o género principal no album
     * Utiliza um hashmap para associar um género especifico encontrado nas músicas do album e aumentar um valor
     * correspondente a esse género encontrado.
     * O género cujo valor for mais alto é considerado o género do album.
     */
    public void calculateMainGenre(){
        //primeiro passo é associar o género das músicas encontradas no album a um valor int através de um hashmap
        HashMap <Genre.GENRE, Integer> genreFrequency = new HashMap<>();
        //para cada música encontrada na lista da música
        //adiciona 1 valor à entrada correspondente cada vez que encontra uma música desse género
        for (Music mc : musicList){
            genreFrequency.put(mc.getGenre(), genreFrequency.getOrDefault(mc.getGenre(),0)+1);
        }
        //max frequency é utilizado para definir qual o género que é encontrado com mais frequencia (contador)
        int maxFreq = 0;
        //a "ciclar" pelo hashmap de modo a encontrar o género com a frequencia maxima
        for (Map.Entry<Genre.GENRE,Integer> entry : genreFrequency.entrySet()){
            if (entry.getValue() > maxFreq){
                maxFreq = entry.getValue();
            }
        }
        //adiciona à lista os géneros principais no caso de haver mais do que um
        ArrayList <Genre.GENRE> genreList = new ArrayList<>();
        for (Map.Entry<Genre.GENRE,Integer> entry : genreFrequency.entrySet()){
            if (entry.getValue() == maxFreq){
                genreList.add(entry.getKey());
            }
        }
        //define o género principal nesse album
        if (genreList.size() == 1){
            mainGenre = genreList.get(0);
        } else mainGenre = null;
    }
}


