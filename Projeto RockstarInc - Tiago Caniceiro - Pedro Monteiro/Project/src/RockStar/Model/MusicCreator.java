/**
 * @Authors Tiago Caniceiro & Pedro Monteiro
 * @Version 1.0
 */
package src.RockStar.Model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Classe MusicCreator que herda parâmetros e funcionalidades da classe abstrata User.
 * Tem como parâmetros o pin associado no registo e a receita total da venda das suas músicas.
 */
public class MusicCreator extends User implements Serializable {
    private String pin;
    private double totalValueSales;
    /**
     * Construtor da classe do criador de musica.
     * @param name Nome do criador
     * @param username Username do criador
     * @param password Password do criador
     * @param email email do criador
     * @param pin pin associado a este criador, fornecido aquando registo
     */
    public MusicCreator(String name, String username, String password, String email, String pin) {
        super(name, username, password, email);
        this.pin = pin;
    }
    public String getPin() {
        return pin;
    }
    public double getTotalValueSales() {
        return totalValueSales;
    }
    /**
     * Método para a criação de um álbum de músicas vazio.
     * @param name nome associado ao álbum.
     */
    @Override
    public void newCollection(String name) {
        //Criação de um álbum vazio
        allCollections.add(new Album(name,  this));
    }
    /**
     * Método para criação de álbum com músicas associadas.
     * @param musicList lista de músicas que são inseridas no álbum.
     */
    public void newCollection(ArrayList<Music> musicList) {}
    @Override
    public void addMusicToCollection(Music music, MusicCollection album) {
        if(allCollections.contains(album)){
            album.addMusicToCollection(music);
            music.setAssociatedAlbum((Album)album);
        }
    }
    public void newMusicToUserMainCollection(Music music){
        allMusic.add(music);
    }
    public void removeMusicFromCollection(Music music, MusicCollection collection){
        if(allCollections.contains(collection)){
            collection.removeMusicFromCollection(music);
        }
    }
    public void removeMusicCollection(MusicCollection collection){
        for(Music m : collection.getMusicList()){
            m.setAssociatedAlbum(null);
        }
        allCollections.remove(collection);
    }
    public void addRevenueFromMusicSale (double valueToAdd){
        totalValueSales += valueToAdd;
    }
}
