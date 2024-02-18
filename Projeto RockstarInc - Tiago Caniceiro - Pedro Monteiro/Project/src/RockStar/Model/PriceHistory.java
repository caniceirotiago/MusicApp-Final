/**
 * @Authors Tiago Caniceiro & Pedro Monteiro
 * @Version 1.0
 */
package src.RockStar.Model;
import java.io.Serializable;
import java.lang.reflect.Array;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;

public class PriceHistory implements Serializable {
    private LocalDateTime priceChangeDate;
    private double newPrice;
    /**
     * Construtor do histórico de preços, que atualiza o preço de uma música e adiciona uma data em que o preço foi
     * atualizado.
     * @param newPrice Novo preço a adicionar a uma música.
     * @param priceChangeDate data em que o preço foi alterado para adicionar ao histórico de atualizações.
     */
    public PriceHistory(double newPrice, LocalDateTime priceChangeDate) {
        this.priceChangeDate = LocalDateTime.now();
        this.newPrice = newPrice;
    }
    public LocalDateTime getPriceChangeDate() {
        return priceChangeDate;
    }
    public double getNewPrice() {
        return newPrice;
    }
}

