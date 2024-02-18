/**
 * @Authors Tiago Caniceiro & Pedro Monteiro
 * @Version 1.0
 */
package src.RockStar.Model;

import src.RockStar.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;

/**
 * Classe que gere e constrói os objetos do tipo musica.
 */
public class Music implements Serializable {
    private String name;
    private Genre.GENRE genre;
    private MusicCreator musicCreator;
    private ArrayList<MusicEvaluation> evaluationList;
    private double price;
    private ArrayList<PriceHistory> priceHistory;
    private boolean isActive;
    private double classification;
    private Album associatedAlbum;
    /**
     * Construtor para o objeto do tipo música.
     * @param name Nome da música.
     * @param genre género da música.
     * @param musicCreator O criador associado à música.
     * @param price o preço correspondente da música.
     */
    public Music(String name, Genre.GENRE genre, MusicCreator musicCreator, double price) {
        this.name = name;
        this.genre = genre;
        this.musicCreator = musicCreator;
        this.evaluationList = new ArrayList<>();
        this.price = price;
        this.isActive = true;
        this.priceHistory = new ArrayList<>();
        this.priceHistory.add(new PriceHistory(price,LocalDateTime.now()));
    }
    public ArrayList<PriceHistory> getPriceHistory() {
        return priceHistory;
    }
    public void setAssociatedAlbum(Album associatedAlbum) {
        this.associatedAlbum = associatedAlbum;
    }
    public double getClassification() {
        return classification;
    }
    public Album getAssociatedAlbum() {
        return associatedAlbum;
    }
    public MusicCreator getMusicCreator() {
        return musicCreator;
    }
    public String getArtistNameFromMusic(){
        return musicCreator.getName();
    }
    public Genre.GENRE getGenre() {
        return genre;
    }
    public String getName() {return name;}
    public void setName(String name) {this.name = name;}
    public double getPrice() {return price;}
    public void setPrice(double price) {
        this.price = price;
        priceHistory.add(new PriceHistory(price, LocalDateTime.now()));
    }
    /**
     * Método que define se determinada música está activa no sistema.
     * @return true ou false consoante a música ser sido definida como ativa ou inativa.
     */
    public boolean isActive() {
        return isActive;
    }
    public void setActive(boolean active) {
        isActive = active;
    }
    public void setGenre(Genre.GENRE genre) {
        this.genre = genre;
    }
    /**
     * Método para calcular a classificação média de uma determinada música consoante as classificações dadas pelos
     * utilizadores.
     * Método chamado sempre que é feita uma classificação ou a música é editada.
     */
    public void calculateClassification(){
        double classificationsSum = 0;
        if(!evaluationList.isEmpty()) {
            for(MusicEvaluation me : evaluationList){
                classificationsSum += me.getEvaluation();
            }
            this.classification =  (classificationsSum / (double) evaluationList.size());
        }
    }
    /**
     * Método que adiciona uma classificação a um ficheiro de música por parte de um cliente.
     * Se o cliente decidir re-avaliar a música, altera a classificação já dada para a mais recente e adiciona uma data de
     * alteração à avaliação.
     * Se o cliente for novo/ainda não tiver feito nenhuma avaliação, adiciona essa avaliação à música.
     * @param client O cliente que está a avaliar a música.
     * @param evaluation A avaliação dada pelo utilizador.
     */
    public void addEvaluation(Client client, int evaluation){
        boolean evaluationAlreadyExists = false;
        for(MusicEvaluation me : evaluationList){
            if(me.getClient().equals(client)) {
                evaluationAlreadyExists = true;
                me.setEvaluation(evaluation);
                me.setEvaluationDateTime(LocalDateTime.now());
            }
        }
        if(!evaluationAlreadyExists){
            evaluationList.add(new MusicEvaluation(client,evaluation));
        }
        calculateClassification();
    }
    public int getClientEvaluationForSpecificMusic(User client){
        int findedEvaluation = -1;
        for(MusicEvaluation me : evaluationList){
            if(client.equals(me.getClient())) findedEvaluation = me.getEvaluation();
        }
        return findedEvaluation;
    }
    public String toString() {
        return name + " \t" + price + "€";
    }
}

