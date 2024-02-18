package src.GUIClassesSwing.OtherClasses;

import src.GUIClassesSwing.GUIManager;
import src.RockStar.Model.Music;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

/**
 * Classe que faz a gestão da janela de diálogo que é chamada quando o utilizador Cliente quer criar uma nova
 * playlist aleatória.
 */
public class RandonPlaylistSelectionDialog extends JDialog {
    private JButton addToBasketbtn;
    private JButton buyMusicbtn;
    private JButton onlyFreebtn;
    private GUIManager guiManager;
    private int returnValue;
    /**
     * A classe que faz gestão da caixa de diálogo para seleção de músicas pagas quando se cria uma playlist aleatória
     * Permite ao utilizador adicionar músicas ao carrinho, comprar músicas ou escolher apenas músicas gratuitas.
     * @param guiManager O gestor da interface gráfica responsável pela comunicação com o sistema.
     * @param associated O quadro associado à caixa de diálogo.
     * @param songNames A lista de músicas selecionadas na lista de reprodução aleatória.
     * @param totalPrice O preço total das músicas pagas na seleção.
     * @param canBuy Indica se o utilizador tem saldo suficiente para comprar as músicas pagas.
     */
    public RandonPlaylistSelectionDialog(GUIManager guiManager, Frame associated, ArrayList<Music> songNames, double totalPrice, boolean canBuy){
        super (associated,"Some of the chosen songs require purchase", true);
        this.guiManager = guiManager;
        JPanel musicListPanel = new JPanel();
        musicListPanel.setLayout(new BoxLayout(musicListPanel, BoxLayout.Y_AXIS));
        for(Music m : songNames){
            musicListPanel.add(new JLabel(m.toString()));
        }
        musicListPanel.add(new JLabel("Total price: " + totalPrice));
        addToBasketbtn = new JButton("Add to Basket");
        buyMusicbtn = new JButton("Buy Music");
        onlyFreebtn = new JButton("Only Free Music");
        buyMusicbtn.setEnabled(canBuy); // Se não tiver dinheiro o butão não  dá para clicar
        addToBasketbtn.addActionListener(e -> onAddToBasckeClickbtn());
        buyMusicbtn.addActionListener(e -> onBuyMusicbtnClick());
        onlyFreebtn.addActionListener(e -> onOnlyFreebtnClick());

        JScrollPane scrollPane =  new JScrollPane(musicListPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

        JPanel btnPanel = new JPanel();
        btnPanel.add(addToBasketbtn);
        btnPanel.add(buyMusicbtn);
        btnPanel.add(onlyFreebtn);

        getContentPane().add(scrollPane,BorderLayout.CENTER);
        getContentPane().add(btnPanel,BorderLayout.SOUTH);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(associated);

        setSize(new Dimension(450,400));
        setResizable(false);
        setVisible(true);
    }
    private void onAddToBasckeClickbtn(){
        returnValue = 1;
        dispose();
    }
    private void onBuyMusicbtnClick(){
        returnValue = 2;
        dispose();
    }
    private void onOnlyFreebtnClick(){
        returnValue = 3;
        dispose();
    }
    public int getReturnValue() {
        return returnValue;
    }
}
