package src.GUIClassesSwing.OtherClasses;

import src.RockStar.Model.MusicAcquisition;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

/**
 * Classe que faz a gestão da janela de diálogo que é chamada quando o utilizador Cliente quer criar uma nova
 * playlist aleatória.
 */
public class HistoricPurchaseDialog extends JDialog {
    /**
     * A classe que faz gestão da caixa de diálogo para apresentação do histórico de compras.
     * @param associated O quadro associado à caixa de diálogo.
     * @param musicAquisitions Lista de aquisições.
     */
    public HistoricPurchaseDialog(Frame associated, ArrayList<MusicAcquisition> musicAquisitions){
        super (associated,"Historic Purchase", true);

        JPanel acquisitionPanel = new JPanel();
        acquisitionPanel.setLayout(new BoxLayout(acquisitionPanel, BoxLayout.Y_AXIS));
        for(MusicAcquisition ma : musicAquisitions){
            String labelText = "       " + ma.getDate() + "    -    " +
                    ((double) Math.round(ma.getTotalPrice() * 100) / 100) + "€     ";
            acquisitionPanel.add(new JLabel(labelText));
        }
        JScrollPane scrollPane =  new JScrollPane(acquisitionPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

        getContentPane().add(scrollPane,BorderLayout.CENTER);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(associated);

        setSize(new Dimension(300, 200));
        setResizable(false);
        setVisible(true);
    }
}
