/**
 * @Authors Tiago Caniceiro & Pedro Monteiro
 * @Version 1.0
 */
package src.RockStar.Model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Classe cliente que implementa a classe abstrata User.
 * Para além de integrar as funcionalidades da classe User, como ter uma coleção de música, tem lista de compras,
 * uma carteira em que se consegue colocar dinheiro e a possibilidade de se comprar e adquirir ficheiros de música.
 *
 */
public class Client extends User implements Serializable {
    private double balance;
    ArrayList<MusicAcquisition> listOfAcquisitions;
    ArrayList<BalanceDeposit> listOfBalanceDeposits;
    ArrayList<Music> musicOnBasketList;
    /**
     * Construtor do utilizador Cliente
     * @param name nome do cliente
     * @param username Username do cliente
     * @param password password do cliente
     * @param email email do cliente
     * @param balance saldo do cliente
     */
    public Client(String name, String username, String password, String email, double balance) {
        super(name, username, password, email);
        this.balance = balance;
        this.listOfAcquisitions = new ArrayList<>();
        this.listOfBalanceDeposits = new ArrayList<>();
        this.musicOnBasketList = new ArrayList<>();
    }
    public double getBalance() {
        return balance;
    }
    public ArrayList<Music> getMusicOnBasketList() {
        return musicOnBasketList;
    }
    public ArrayList<MusicAcquisition> getListOfAcquisitions() {
        return listOfAcquisitions;
    }
    /**
     * Método para criação de uma nova playlist vazia.
     * @param name nome da playlist
     */
    public void newCollection(String name){
        //Creation of Empty playlist
        Playlist newPlaylist = new Playlist(name, this);
        allCollections.add(newPlaylist);
    }
    /**
     * Método auxiliar do construtor de música aleatória.
     * @param listOfMusic Uma lista de música escolhida pelos métodos random na Classe RockstarManager, no método
     * RandomPlaylistCreation.
     */
    public void newCollection(ArrayList<Music> listOfMusic){
        //Creation of collection by random methods
        if(!listOfMusic.isEmpty()){
            String genre = listOfMusic.get(0).getGenre().name();
            allCollections.add(new Playlist("Random Playlist - " + genre, this, listOfMusic));
        }
    }
    public void addMusicToBasket(Music music){
        if(!musicOnBasketList.contains(music)) musicOnBasketList.add(music);
    }
    /**
     * Método para adicionar música a uma playlist criada.
     * @param music música selecionada para adicionar
     * @param musicCollection Playlist escolhida onde se vai integrar a música
     */
    public void addMusicToCollection(Music music, MusicCollection musicCollection){
        if(allCollections.contains(musicCollection) && music.isActive()) musicCollection.addMusicToCollection(music);
    };
    /**
     * Método para adicionar músicas à coleção de músicas totais do cliente.
     * @param music ficheiro de música selecionado
     */
    public void newMusicToUserMainCollection(Music music){
        allMusic.add(music);
    }
    public void removeMusicFromCollection(Music music, MusicCollection collection){
        collection.removeMusicFromCollection(music);
    };
    public void removeMusicCollection(MusicCollection collection){
        allCollections.remove(collection);
    };
    public void addMoney(double moneyToAdd){
        balance += moneyToAdd;
        listOfBalanceDeposits.add(new BalanceDeposit(moneyToAdd));
    }
    /**
     *método que permite ao cliente validar a compra de músicas, consoante o saldo que tenha na carteira.
     *Se não tiver saldo não permite a compra de músicas.
     *@param musicList músicas escolhidas para comprar
     *@return Retorna falso se o utilizador não tiver dinheiro suficiente para aquisição de música.
     */
    public boolean validationOfAquisition(ArrayList<Music> musicList){
        double totalPrice = 0;
        for(Music m : musicList){
            totalPrice += m.getPrice();
        }
        if(totalPrice <= balance){
            listOfAcquisitions.add(new MusicAcquisition(musicList));
            balance -= totalPrice;
            allMusic.addAll(musicList);
            return true;
        }
        return false;
    }
}
