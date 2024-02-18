package src.GUIClassesSwing.OtherClasses;

import javax.swing.*;
import java.awt.*;

/**
 * Classe que estende JFrame, destinada à criação de janelas para as interfaces de login e registo.
 * Esta classe configura os parâmetros base da janela, como dimensão, cor de fundo, visibilidade, localização e possibilidade de redimensionamento.
 * Define também as definições padrão para o uso de GridBagConstraints, que é utilizado na gestão de layout.
 */
public class LogRegFrame extends JFrame {
    GridBagConstraints constraints;
    /**
     * Construtor da classe LogRegFrame.
     * Inicializa a janela com configurações específicas, tais como dimensão, cor, visibilidade, entre outras.
     * Define igualmente os parâmetros iniciais para o GridBagConstraints, que irão auxiliar no posicionamento dos componentes na janela.
     */
    public LogRegFrame()  {
        setSize(250,350);
        setBackground(Color.GRAY);
        setUndecorated(true);
        setVisible(true);
        setLocationRelativeTo(null);
        setResizable(false);
        getContentPane();
        constraints =  new GridBagConstraints();
        constraints.gridx= GridBagConstraints.REMAINDER; //Ocupa o restante espaço na linha
        constraints.gridy = GridBagConstraints.RELATIVE; // O componente é colucado na linha seguinte
        constraints.gridwidth = GridBagConstraints.REMAINDER;
    }
    /**
     * Método para obter as configurações de GridBagConstraints.
     * Utilizado para aplicar estas configurações aos componentes que serão adicionados à janela.
     * @return Retorna o objeto GridBagConstraints com as configurações definidas.
     */
    public GridBagConstraints getConstraints() {
        return constraints;
    }
}
