/**
 * @Authors Tiago Caniceiro & Pedro Monteiro
 * @Version 1.0
 */
package src.RockStar.Model;

import src.RockStar.Model.Client;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Classe de avaliação de música
 */
public class MusicEvaluation implements Serializable {
    private Client client;
    private LocalDateTime evaluationDateTime;
    private int evaluation;
    /**
     * Construtor da classe que tem como parâmetro um cliente e a avaliação que deseja dar.
     * @param client O utilizador Client na aplicação
     * @param evaluation A avaliação que deseja dar à música
     */
    public MusicEvaluation(Client client, int evaluation) {
        this.client = client;
        this.evaluationDateTime = LocalDateTime.now();
        this.evaluation = evaluation;
    }
    public int getEvaluation() {
        return evaluation;
    }
    public Client getClient() {
        return client;
    }
    public void setEvaluation(int evaluation) {
        this.evaluation = evaluation;
    }
    /**
     * Método que associa uma data à avaliação de uma música
     * @param evaluationDateTime data de avaliação de música.
     */
    public void setEvaluationDateTime(LocalDateTime evaluationDateTime) {
        this.evaluationDateTime = evaluationDateTime;
    }
}
