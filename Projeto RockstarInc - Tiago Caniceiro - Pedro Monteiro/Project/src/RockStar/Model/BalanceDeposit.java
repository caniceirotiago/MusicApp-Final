/**
 * @Authors Tiago Caniceiro & Pedro Monteiro
 * @Version 1.0
 */
package src.RockStar.Model;

import java.io.Serializable;
import java.time.LocalDateTime;
/**
 * Classe que gere os depósitos na conta do cliente
 */
public class BalanceDeposit implements Serializable {
    private double balanceToAdd;
    private LocalDateTime dateTime;
    /**
     * método para adicionar dinheiro à carteira do cliente
     * @param balanceToAdd valor a adicionar.
     */
    public BalanceDeposit(double balanceToAdd) {
        this.balanceToAdd = balanceToAdd;
        this.dateTime = LocalDateTime.now();
    }
}
