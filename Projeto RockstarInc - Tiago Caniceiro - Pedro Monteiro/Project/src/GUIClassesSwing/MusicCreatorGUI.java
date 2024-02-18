package src.GUIClassesSwing;

import src.GUIClassesSwing.OtherClasses.ImagePaths;
import src.GUIClassesSwing.OtherClasses.RetangleBarChartComp;
import src.RockStar.*;
import src.RockStar.Model.Album;
import src.RockStar.Model.Music;
import src.RockStar.Model.MusicCollection;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Vector;

/**
 * A classe MusicCreatorGUI representa a interface gráfica do criador de música no sistema.
 * Esta classe estende JFrame e incorpora componentes como tabelas, painéis e rótulos para
 * interação com o utilizador e exibição de estatísticas relacionadas ao criador de música.
 */
public class MusicCreatorGUI extends JFrame {
    private GUIManager guiManager;
    private DefaultTableModel centralTableModel;
    private DefaultTableModel searchMusicTableModel;
    private DefaultListModel<MusicCollection> listModelWest;
    private JPanel centerPanel;
    private JPanel westPanel;
    private JPanel southPanel;
    private MusicCollection selectedAlbum;
    private JTable centralTable;
    private JTable searchMusicTable;
    private JPanel southGrapgStatsPanel;
    private JPanel graphicStatsPanel;
    private JList<MusicCollection> albumListWest;
    private int lastPositionMouseRightClickX;
    private int lastPositionMouseRightClickY;
    private MusicCollection currentUserCollection;
    private JLabel totalUsers;
    private JLabel totalSongs ;
    private JLabel totalPriceValue ;
    private JLabel totalSales ;
    private JLabel individualSales ;
    private JLabel individualMusicCreated ;
    private CardLayout centralCardLayout;
    private CardLayout statsCardLayout;
    private Search search;
    private JTextField searchTextField;
    private TextField musicNameTextField;
    private TextField priceTextField;
    private JComboBox<Genre.GENRE> selectedGender;

    /**
     * Construtor da classe MusicCreatorGUI.
     * @param username O nome de utilizador associado ao criador de música.
     * @param guiManager O gestor da interface gráfica responsável pela comunicação com o sistema.
     */
    public MusicCreatorGUI(String username, GUIManager guiManager){
        super("Music Creator - " + username);
        this.guiManager = guiManager;
        initComponents();
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(1100,1000);
        setLocationRelativeTo(null);
        setVisible(true);
        ImageIcon imageIcon = new ImageIcon(ImagePaths.APP_ICON);
        setIconImage(imageIcon.getImage());
        setMinimumSize(new Dimension(1300, 800));
    }

    /**
     * Método que inicializa os componentes gráficos
     * No geral, os componentes são divididos em paineis Norte, Este, Centro, Oeste e Sul, e os seus detalhes, como botões,
     * Pop-up menus e outros detalhes que correspondem a cada painel individualmente.
     * O painel Norte é responsável por ter o logo da aplicação, a barra de pesquisa e o botão de logout para ser
     * possível ao utilizador sair da sua frame de criador de música e voltar à frame de registo/login.
     * O painel Oeste é o painel responsável por lidar com a coleção geral de músicas do utilizador e por ter uma lista
     * onde é possível criar e manipular novos álbuns. Ao criar um novo álbum, uma JOptionPane aparece ao
     * utilizador a pedir o nome do novo álbum.
     * O painel Central é o responsável por mostrar ao utilizador as músicas que possuí de sua autoria, as músicas
     * que incluiu nos álbuns que criou e também as suas músicas que existem no programa quando o utilizador pesquisa
     * por músicas.
     * O painel Este permite ao utilizador a criação de uma nova música, com campos para colocar o nome, o género e o
     * preço que lhe deseja atríbuir.
     * O painel Sul mostra as estatísticas associadas ao utilizador, com botões que permitem alterar entre as estatísticas
     * globais e individuais do utilizador e também as estatísticas dos seus álbuns.
     */
    public void initComponents(){
        Container mainContainer = new Container();
        mainContainer.setLayout(new BorderLayout());

        /**
         *Esta secção destina-se aos detalhes como popup menus e submenus que são utilizados nos painéis da frame do
         * criador de música. Foram colocados separadamente para permitir melhor visualização e organização do código.
         */

        //PopUp menu da tabela central para adicionar músicas a álbuns ou editar músicas, este menu apenas é acionado
        //quando o primeiro elemento da lista do painel oeste (músicas totais do criador) está selecionado.
        JPopupMenu centralTablePopMenu = new JPopupMenu();
        JMenuItem addToAlbumMenu = new JMenuItem("Add to Album");
        JMenuItem editMusicMenu = new JMenuItem("Edit Music");
        centralTablePopMenu.add(addToAlbumMenu);
        centralTablePopMenu.add(editMusicMenu);
        addToAlbumMenu.addActionListener(e -> addMusicToAlbumOnClick());
        editMusicMenu.addActionListener(e -> editMusicOnClick());

        //Popup menu central para remover músicas de um álbum ou editar música. Este popup menu só surge
        //quando se clica na música de um álbum e não no primeiro elemento da lista oeste.
        JPopupMenu centralTablePUM2 = new JPopupMenu();
        JMenuItem removeFromAlbum = new JMenuItem("Remove from Album");
        JMenuItem editMusicMenu2 = new JMenuItem("Edit Music");
        centralTablePUM2.add(removeFromAlbum);
        centralTablePUM2.add(editMusicMenu2);
        removeFromAlbum.addActionListener(e -> onRemoveFromAlbumClick());
        editMusicMenu2.addActionListener(e -> editMusicOnClick());

        //PopUp menu oeste que abre opções relacionadas com álbuns criados pelo criador de música. É possível apagar álbuns.
        JPopupMenu westListPopMenu = new JPopupMenu();
        JMenuItem deleteAlbum = new JMenuItem("Delete Album");
        westListPopMenu.add(deleteAlbum);
        deleteAlbum.addActionListener(e -> onDeleteAlbumClick());


        //PopUpMenu disponível ao clicar em uma música após realizar uma pesquisa de músicas,
        //permitindo a edição das mesmas.
        JPopupMenu centralTableSearchedMusicPuM = new JPopupMenu();
        JMenuItem editMusicMenu3 = new JMenuItem("Edit Music");
        centralTableSearchedMusicPuM.add(editMusicMenu3);
        editMusicMenu3.addActionListener(e -> editMusicSearchTableOnClick());

        /**
         * No painel Oeste há uma representação visual de todos os álbuns que o utilizador criou. O primeiro elemento
         * da lista não é um álbum, mas sim a coleção geral de músicas criadas pelo artista. O nome do elemento denomina-se
         * "Created music" e as suas funcionalidades são diferentes do que as dos outros elementos da lista.
         * Quando o programa inicia esta é a primeira coleção selecionada e respectivamente a primeira que é revelada na
         * tabela central.
         * Esta lista tem dois action listeners associados. O primeiro (botão esquerdo do rato) permite selecionar o álbum
         * de interesse. No caso de se selecionar a primeira lista ("Created Music"), o segundo action listener que está
         * associado ao botão direito do rato não tem nenhuma funcionalidade. No caso de se selecionar um álbum que não seja
         * o "Created Music", o segundo action listener permite a funcionalidade de apagar o álbum.
         * Neste painel existe também o botão de adicionar um novo álbum. Este botão tem um action listener que abre uma
         * janela de diálogo, pedido ao utilizador para colocar o nome que deseja atribuir ao album, que seguidamente
         * irá ser colocado neste painel Oeste.
         */
        //Criação do painel Oeste e implementação das suas funcionalidades
        westPanel = new JPanel(new GridBagLayout());
        JLabel playlistLabel =  new JLabel();
        playlistLabel.setText("Album");
        listModelWest = new DefaultListModel<>();
        updateMusicJListModel();
        //No caso de um novo utilizador é criada uma nova instância de álbum para futuramente guardar a totalidade de
        // músicas adquiridas
        if(currentUserCollection == null) currentUserCollection = new Album();
        selectedAlbum = currentUserCollection;
        albumListWest = new JList<>(listModelWest);
        albumListWest.addListSelectionListener(e -> {
            if(!e.getValueIsAdjusting()){
                selectedAlbum = albumListWest.getSelectedValue();
                if(selectedAlbum != null){
                    //atualiza tabela central e mostra o primeiro cartão no painel central
                    //(O segundo cartão mostra as tabelas de pesquisa)
                    updateMusicJTableModel(selectedAlbum.getMusicList());
                    centralCardLayout.show(centerPanel,"1");
                }
            }
        });
        albumListWest.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                //Guarda as coordenadas do rato para posicionar corretamente a posição dos popup menus
                lastPositionMouseRightClickX = e.getX();
                lastPositionMouseRightClickY = e.getY();
                if(SwingUtilities.isRightMouseButton(e)){
                    int row = albumListWest.locationToIndex(e.getPoint());
                    Rectangle rectangle = albumListWest.getCellBounds(row,row);
                    if(rectangle != null && rectangle.contains(e.getPoint()) &&
                            //adiciona 1 à lógica das linhas para garantir que o primeiro elemento não está incluido
                            row >= 1 && row < listModelWest.getSize()){
                        albumListWest.setSelectedIndex(row);
                        westListPopMenu.show(e.getComponent(),lastPositionMouseRightClickX,lastPositionMouseRightClickY);
                    }
                }
            }
        });
        JScrollPane scrollPane = new JScrollPane(albumListWest);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

        JButton newAlbumBtn = new JButton("New Album");
        newAlbumBtn.addActionListener(e -> onNewAlbumbtnClick());

        westPanel.setPreferredSize(new Dimension(175, 0));
        GridBagConstraints cw = new GridBagConstraints();

        cw.gridx = 0;
        cw.gridy = 0;
        cw.weightx = 0.2;
        cw.anchor = GridBagConstraints.CENTER;
        cw.fill = GridBagConstraints.NONE;
        westPanel.add(playlistLabel, cw);

        cw.gridy++;
        cw.weighty = 0.6;
        cw.fill = GridBagConstraints.BOTH;
        westPanel.add(scrollPane, cw);

        cw.gridy++;
        cw.weighty = 0.2;
        cw.fill = GridBagConstraints.NONE;
        cw.anchor = GridBagConstraints.NORTH;
        westPanel.add(newAlbumBtn, cw);

        /**
         * O painel Este é responsável pela funcionalidade de criação de um novo ficheiro de música, através de campos
         * de texto e um botão com funcionalidade de adicionar nova música à lista "Created music".
         */
        //Criação do painel Este e suas funcionalidades
        JPanel eastPanel = new JPanel(new GridBagLayout());
        JLabel newMusicLbl = new JLabel("Name");
        musicNameTextField = new TextField(20);

        Genre.GENRE[] genres = Genre.GENRE.values();
        selectedGender = new JComboBox<>(genres);

        JLabel priceLbl =  new JLabel("Price €");
        priceTextField = new TextField(20);

        JButton createMusicBtn = new JButton("New Music");
        createMusicBtn.addActionListener(e -> onCreateMusicBtnClick());

        eastPanel.setPreferredSize(new Dimension(175, 0));
        GridBagConstraints ce = new GridBagConstraints();

        ce.gridx= GridBagConstraints.REMAINDER;
        ce.gridy = GridBagConstraints.RELATIVE;
        ce.gridwidth = GridBagConstraints.REMAINDER;
        ce.fill = GridBagConstraints.HORIZONTAL;
        eastPanel.add(newMusicLbl, ce);
        eastPanel.add(musicNameTextField,ce);
        eastPanel.add(selectedGender, ce);
        eastPanel.add(priceLbl, ce);
        eastPanel.add(priceTextField, ce);
        eastPanel.add(createMusicBtn, ce);

        /**
         * O painel Norte tem o logo da aplicação, o campo de texto para a pesquisa, o respectivo botão de pesquisa e
         * o botão de logout da aplicação, que retorna o utilizador para o painel de Registo/Login.
         */
        int newWidth = 100;
        int newHeight = 100;
        ImageIcon originalIcon = new ImageIcon(ImagePaths.APP_ICON);
        Image originalImage = originalIcon.getImage();
        Image resizedImage = originalImage.getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH);
        ImageIcon resizedIcon = new ImageIcon(resizedImage);
        JLabel logo =  new JLabel(resizedIcon);

        searchTextField = new JTextField("",20);
        JButton searchBtn = new JButton("\uD83D\uDD0D");
        searchBtn.addActionListener(e -> newSearch());

        JButton logOutbtn = new JButton("Logout");
        logOutbtn.addActionListener(e -> {
            try {
                onlogOutbtnClick();
            } catch (IOException | ClassNotFoundException ex) {
                throw new RuntimeException(ex);
            }
        });
        JPanel northPanel = new JPanel(new GridBagLayout());
        GridBagConstraints cn = new GridBagConstraints();
        northPanel.setPreferredSize(new Dimension(0, 100));

        cn.gridx = 0;
        cn.gridy = 0;
        cn.weightx = 0.01;
        cn.weighty = 1;
        cn.fill = GridBagConstraints.NONE;
        cn.anchor = GridBagConstraints.WEST;
        cn.insets = new Insets(0, 40, 0, 0);
        northPanel.add(logo, cn);

        cn.gridx++;
        cn.weightx = 0.25;
        cn.fill = GridBagConstraints.HORIZONTAL;
        northPanel.add(Box.createHorizontalStrut(0), cn);

        cn.gridx++;
        cn.weightx = 0;
        cn.fill = GridBagConstraints.NONE;
        northPanel.add(searchTextField, cn);

        cn.gridx++;
        cn.weightx = 0;
        cn.fill = GridBagConstraints.NONE;
        northPanel.add(searchBtn, cn);

        cn.gridx++;
        cn.weightx = 0.25;
        cn.fill = GridBagConstraints.HORIZONTAL;
        northPanel.add(Box.createHorizontalStrut(0), cn);

        cn.gridx++;
        cn.weightx = 0.01;
        cn.anchor = GridBagConstraints.EAST;
        cn.insets = new Insets(0, 0, 0, 40);
        northPanel.add(logOutbtn, cn);

        /**
         * O painel central é caracterizado por um sistema de "Card layout". Altera entre a tabela que mostra toda a
         * música nos álbuns do utilizador e a tabela de pesquisa.
         * O botão de pesquisa no painel Norte é responsável por fazer o painel de pesquisa visível. O botão de retroceder
         * ou o click em qualquer local no painel Oeste volta a mostrar o primeiro painel.
         * As tabelas não são editáveis e são ordenadas com recurso ao método "setAutoCreateRowSorter()". O index
         * das músicas e coleções é corrigido no método respectivo.
         * O popup menu na tabela de pesquisa de música permite adicionar música a um álbum específico e edita-la.
         * O popup menu na pesquisa de músicas permite ao utilizador editar músicas específicas.
         */
        String[] columnNamesMusic = {"Title", "Artist", "Album", "Classification","Price €","Genre","Active"};
        centralTableModel = new DefaultTableModel(columnNamesMusic,0){
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
            public Class<?> getColumnClass(int column) {
                if(column == 3 || column == 4){
                    return Double.class;
                }
                else {
                    return String.class;
                }
            }
        };
        ArrayList<Music> userAllMusic = guiManager.getUserAllMusic();
        updateMusicJTableModel(userAllMusic);
        centralTable = new JTable(centralTableModel);
        centralTable.getTableHeader().setReorderingAllowed(false);
        centralTable.setAutoCreateRowSorter(true);

        //Este action listener abre differentes popup menus dependendo do elemento selecionado no painel Oeste
        centralTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                lastPositionMouseRightClickX = e.getX();
                lastPositionMouseRightClickY = e.getY();
                if(SwingUtilities.isRightMouseButton(e)){
                    int row = centralTable.rowAtPoint(e.getPoint());
                    if(row>=0 && row < centralTable.getRowCount()){
                        centralTable.setRowSelectionInterval(row,row);
                        if(selectedAlbum == null) selectedAlbum = currentUserCollection;
                        if(selectedAlbum.equals(currentUserCollection)){
                            centralTablePopMenu.show(e.getComponent(),lastPositionMouseRightClickX,lastPositionMouseRightClickY);
                        } else {
                            centralTablePUM2.show(e.getComponent(),lastPositionMouseRightClickX,lastPositionMouseRightClickY);
                        }
                    }
                }
            }
        });
        JScrollPane scrollPane3 = new JScrollPane(centralTable);
        scrollPane3.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane3.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

        if(search == null) search = new Search();
        searchMusicTableModel = new DefaultTableModel(columnNamesMusic,0){
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
            public Class<?> getColumnClass(int column) {
                if(column == 3 || column == 4){
                    return Double.class;
                }
                else {
                    return String.class;
                }
            }
        };
        updateSearchMusicTable(search.getFoundMusics());
        searchMusicTable = new JTable(searchMusicTableModel);
        searchMusicTable.getTableHeader().setReorderingAllowed(false);
        searchMusicTable.setAutoCreateRowSorter(true);

        searchMusicTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                lastPositionMouseRightClickX = e.getX();
                lastPositionMouseRightClickY = e.getY();
                if(SwingUtilities.isRightMouseButton(e)){
                    int row = searchMusicTable.rowAtPoint(e.getPoint());
                    if(row>=0 && row < searchMusicTable.getRowCount()){
                        searchMusicTable.setRowSelectionInterval(row,row);
                        centralTableSearchedMusicPuM.show(e.getComponent(),lastPositionMouseRightClickX,
                                lastPositionMouseRightClickY);
                    }
                }
            }
        });

        //Criação de uma tabela de pesquisa por coleções inserida num CardLayout aninhado.
        String[] columnNamesCollection = {"Collection", "Type", "Creator"};
        DefaultTableModel searchCollectionTableModel = new DefaultTableModel(columnNamesCollection, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        JTable searchCollectionTable = new JTable(searchCollectionTableModel);
        searchCollectionTable.getTableHeader().setReorderingAllowed(false);
        searchCollectionTable.setAutoCreateRowSorter(true);

        JLabel searchLabel = new JLabel("Search");
        JButton backToMainbtn = new JButton("Back");
        JPanel searchbtnPanel = new JPanel(new FlowLayout());
        searchbtnPanel.add(searchLabel);
        searchbtnPanel.add(backToMainbtn);

        JScrollPane scrollPane4 = new JScrollPane(searchMusicTable);
        scrollPane4.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane4.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);


        JPanel searchPanel = new JPanel(new BorderLayout());
        searchPanel.add(searchbtnPanel,"North");
        searchPanel.add(scrollPane4,"Center");

        //Criação do carLayout externo
        centralCardLayout = new CardLayout();
        centerPanel = new JPanel(centralCardLayout);

        centerPanel.add(scrollPane3,"1");
        centerPanel.add(searchPanel,"2");
        centralCardLayout.show(centerPanel, "1");
        backToMainbtn.addActionListener(e -> centralCardLayout.show(centerPanel, "1"));

        /**
         * Criação do painel Sul
         * Este painel é responsável por conter a informação estatística.
         * Esta informação é dividida em estatísticas globais e estatísticas sobre os géneros incluidos nos álbuns do criador.
         * A estatística global revela o número total de utilizadores no sistema, o número total de utilizadores,
         * o total de músicas, o valor total das músicas que existem e a receita total com as vendas das músicas que existem
         * em tempo real no sistema.
         * As estatísticas relativas aos géneros encontrados por álbum são mostrados ao utilizador de maneira numérica e
         * por uma barra que demonstra o tipo de género que pertence ao álbum.
         */

        southPanel = new JPanel();
        ArrayList<Double> overallStatistics = guiManager.getStatistics();
        JPanel firstStatsPanel = new JPanel(new FlowLayout());
        totalUsers = new JLabel();
        totalSongs = new JLabel();
        totalPriceValue = new JLabel();
        totalSales = new JLabel( );
        individualSales = new JLabel( );
        individualMusicCreated = new JLabel( );
        updateFirstStatsPanel(overallStatistics);

        firstStatsPanel.add(new JLabel("Global Statistics"));
        firstStatsPanel.add(totalUsers);
        firstStatsPanel.add(totalSongs);
        firstStatsPanel.add(totalPriceValue);
        firstStatsPanel.add(totalSales);
        firstStatsPanel.add(new JLabel("Individual Statistics"));
        firstStatsPanel.add(individualSales);
        firstStatsPanel.add(individualMusicCreated);


        ArrayList<Integer> albumStatistics = guiManager.getAlbumTypeStatistics();
        graphicStatsPanel = new JPanel();
        updateSecondStatsPanel(albumStatistics);


        JPanel secondStatsPanel = new JPanel(new BorderLayout());
        secondStatsPanel.add(graphicStatsPanel, BorderLayout.CENTER);
        secondStatsPanel.add(new JLabel("Global Statistics - Albums by Genre"), BorderLayout.SOUTH);

        statsCardLayout = new CardLayout();
        southGrapgStatsPanel = new JPanel(statsCardLayout);
        southGrapgStatsPanel.add(firstStatsPanel,"1");
        southGrapgStatsPanel.add(secondStatsPanel,"2");
        statsCardLayout.show(southGrapgStatsPanel,"1");

        JButton swithcStatsBtn1 = new JButton("Global and individual statistics");
        JButton swithcStatsBtn2 = new JButton("Albums by genre statistics");
        swithcStatsBtn1.addActionListener(e -> onSwithcStatsBtnClick1());
        swithcStatsBtn2.addActionListener(e -> onSwithcStatsBtnClick2());
        JPanel btnPanel = new JPanel();
        btnPanel.add(swithcStatsBtn1);
        btnPanel.add(swithcStatsBtn2);

        southPanel = new JPanel();
        southPanel.setPreferredSize(new Dimension(0, 175));
        southPanel.add(southGrapgStatsPanel,BorderLayout.CENTER);
        southPanel.add(btnPanel,BorderLayout.SOUTH);

        mainContainer.add(northPanel,"North");
        mainContainer.add(centerPanel,"Center");
        mainContainer.add(eastPanel,"East");
        mainContainer.add(westPanel,"West");
        mainContainer.add(southPanel,"South");

        add(mainContainer);
    }
    /**
     * Método que atualiza a tabela de músicas central
     * Caso seja criada uma música nova e adicionada à coleção de música criada pelo utilizador, atualiza a tabela para
     * uma nova em que essa música foi integrada
     * Caso seja selecionado um álbum que contêm ficheiros de música, atualiza a tabela central para essas músicas que pertencem
     * ao álbum escolhido
     * @param selectedAlbum é o álbum que o utilizador escolhe e que irá atualizar a lista de músicas da tabela central
     * para mostrar as que pertencem a esse álbum específico
     */
    public void updateMusicJTableModel(ArrayList<Music> selectedAlbum){
        centralTableModel.setRowCount(0);
        System.out.println("Album size: " + selectedAlbum.size());
        for(Music ms : selectedAlbum){
            Vector <Object> line = new Vector<>();
            line.add(ms.getName());
            line.add(ms.getArtistNameFromMusic());
            String albumName;
            if(ms.getAssociatedAlbum() == null) albumName = "Single";
            else albumName = ms.getAssociatedAlbum().getName();
            line.add(albumName);
            line.add(ms.getClassification());
            line.add(ms.getPrice());
            line.add(ms.getGenre().toString());
            String status;
            if(ms.isActive()) status = "Active";
            else status = "Inactive";
            line.add(status);
            centralTableModel.addRow(line);
        }
    }
    /**
     * Método que atualiza a lista de álbuns aquando a criação de um novo álbum.
     */
    public void updateMusicJListModel(){
        currentUserCollection =guiManager.getCorrentUserMainCollectionMusicCreator();
        listModelWest.removeAllElements();
        listModelWest.addElement(currentUserCollection);
        for(MusicCollection cl : guiManager.getUserAllCollection()){
            listModelWest.addElement(cl);
        }
    }
    /**
     * Método que atualiza o index do elemento selecionado;
     * @return retorna nulo no caso de a fila ser -1;
     */
    public Music getSelectedMusicOnCentralTable(){
        int row = centralTable.getSelectedRow();
        if(row != -1){
            int updatedIndex = centralTable.convertRowIndexToModel(row);
            ArrayList<Music> musics = selectedAlbum.getMusicList();
            return musics.get(updatedIndex);
        }
        return null;
    }
    public Music getSelectedMusicOnSearchTable(){
        int row = searchMusicTable.getSelectedRow();
        if(row != -1){
            int updatedIndex = searchMusicTable.convertRowIndexToModel(row); //este método atualiza o index do elemento
            ArrayList<Music> musics = new ArrayList<>();
            musics = search.getFoundMusics();
            return musics.get(updatedIndex);
        }
        return null;
    }
    public MusicCollection getSelectedAlbum(){
        int row = albumListWest.getSelectedIndex()-1;
        if(row != -1){
            ArrayList<MusicCollection> playlist = guiManager.getUserAllCollection();
            return playlist.get(row);
        }
        return null;
    }
    public void onlogOutbtnClick() throws IOException, ClassNotFoundException {
        guiManager.logoutMCreator();
    }
    public void onRemoveFromAlbumClick(){
        Music selectedMusic = getSelectedMusicOnCentralTable();
        if(selectedMusic!= null){
            guiManager.removeMusicFromCollection(selectedMusic, selectedAlbum);
            updateMusicJTableModel(selectedAlbum.getMusicList());
            updateSecondStatsPanel(guiManager.getAlbumTypeStatistics());
            centerPanel.revalidate();
            centerPanel.repaint();
            System.out.println("Music eliminated from Album");
        }
    }
    public void addMusicToAlbumOnClick(){
        JPopupMenu albumsMenu =new JPopupMenu();
        int nOfPlaylists = guiManager.getUserAllCollection().size();
        if(nOfPlaylists == 0){
            JMenuItem emptyList = new JMenuItem("No Albums to Show");
            albumsMenu.add(emptyList);
        } else{
            for(MusicCollection al: guiManager.getUserAllCollection()){
                JMenuItem albumItem  = new JMenuItem(al.getName());
                albumItem.addActionListener(e -> {
                    Music selectedMusic = getSelectedMusicOnCentralTable();
                    if(selectedMusic != null){
                        if(al.getMusicList().contains(selectedMusic)){
                            JOptionPane.showMessageDialog(null,
                                    "This music is already on that album");
                        }
                        else if(selectedMusic.getAssociatedAlbum() != null){

                            String album = selectedMusic.getAssociatedAlbum().getName();
                            JOptionPane.showMessageDialog(null,
                                    "This music is on another album: " + album);
                        }
                        else {
                            guiManager.addMusicToCollection(selectedMusic,al);
                            updateMusicJTableModel(selectedAlbum.getMusicList());
                            updateSecondStatsPanel(guiManager.getAlbumTypeStatistics());
                        }
                    }
                });
                albumsMenu.add(albumItem);
            }
        }
        albumsMenu.show(centralTable,lastPositionMouseRightClickX,lastPositionMouseRightClickY);
    }
    public void editMusicOnClick(){
        Music selectedMusic = getSelectedMusicOnCentralTable();
        guiManager.editMusicDialogCall(selectedMusic);
        updateMusicJTableModel(selectedAlbum.getMusicList());
        updateSecondStatsPanel(guiManager.getAlbumTypeStatistics());
        updateFirstStatsPanel(guiManager.getStatistics());
    }
    public void editMusicSearchTableOnClick(){
        Music selectedMusic = getSelectedMusicOnSearchTable();
        guiManager.editMusicDialogCall(selectedMusic);
        updateSearchMusicTable(search.getFoundMusics());
    }
    public void onNewAlbumbtnClick(){
        centralCardLayout.show(centerPanel,"1");
        String albumName = JOptionPane.showInputDialog("Enter the name of the new album");
        if(albumName != null && !albumName.trim().isEmpty()){
            boolean nameAlreadyExists = false;
            for(MusicCollection mc: guiManager.getUserAllCollection()){
                if(mc.getName().equals(albumName)) nameAlreadyExists = true;
            }
            if(!nameAlreadyExists){
                guiManager.newCollection(albumName);
                MusicCollection newMusicCollection =
                        guiManager.getUserAllCollection().get(guiManager.getUserAllCollection().size()-1);
                selectedAlbum = newMusicCollection;
                updateMusicJListModel();
                updateMusicJTableModel(newMusicCollection.getMusicList());
                updateSecondStatsPanel(guiManager.getAlbumTypeStatistics());
                westPanel.revalidate();
                westPanel.repaint();
            }else {
                JOptionPane.showMessageDialog(null,"Album name already exists");
            }
        }
    }
    public void onCreateMusicBtnClick(){
        guiManager.newMusicAttempt(musicNameTextField.getText(), priceTextField.getText(),
                selectedGender.getItemAt(selectedGender.getSelectedIndex()));
        selectedAlbum = currentUserCollection;
        updateMusicJTableModel(selectedAlbum.getMusicList());
    }
    public void onDeleteAlbumClick(){
        MusicCollection selected = getSelectedAlbum();
        if(selected != null){
            int confirmation = JOptionPane.showConfirmDialog(null, "Comfirm the elimination of "
                    + selected.getName());
            if(confirmation == 0){
                guiManager.removeMusicCollection(selected);
                updateMusicJListModel();
                updateMusicJTableModel(currentUserCollection.getMusicList());
                updateSecondStatsPanel(guiManager.getAlbumTypeStatistics());
                westPanel.revalidate();
                westPanel.repaint();
                System.out.println("Album deleted");
            }
        }
    }
    public void newSearch(){
        centralCardLayout.show(centerPanel, "2");
        search = guiManager.newSearch(searchTextField.getText());
        updateSearchMusicTable(search.getFoundMusics());
        centerPanel.revalidate();
        centerPanel.repaint();
    }
    /**
     * Método que atualiza a tabela de pesquisa consoante os termos de música inseridos
     * @param list lista de músicas presente na coleção de músicas do sistema
     */
    public void updateSearchMusicTable(ArrayList<Music> list){
        searchMusicTableModel.setRowCount(0);
        if(list != null){
            for(Music ms : list){
                Vector <Object> line = new Vector<>();
                line.add(ms.getName());
                line.add(ms.getArtistNameFromMusic());
                String albumName;
                if(ms.getAssociatedAlbum() == null) albumName = "Single";
                else albumName = ms.getAssociatedAlbum().getName();
                line.add(albumName);
                line.add(ms.getClassification());
                line.add(ms.getPrice());
                line.add(ms.getGenre().toString());
                String status;
                if(ms.isActive()) status = "Active";
                else status = "Inactive";
                line.add(status);
                searchMusicTableModel.addRow(line);
            }
        }
    }
    public void onSwithcStatsBtnClick1(){
        statsCardLayout.show(southGrapgStatsPanel,"1");
    }
    public void onSwithcStatsBtnClick2(){
        statsCardLayout.show(southGrapgStatsPanel,"2");
    }
    /**
     * Método responsável pela atualização em tempo real das estatisticas globais do criador de música
     * @param overallStatistics lista de valores associados ao tipo de estatística que está a ser mostrada ao
     * utilizador
     */
    public void updateFirstStatsPanel(ArrayList<Double> overallStatistics){
        totalUsers.setText("<html>Total Users<br><p style='text-align:center;'>" +
                (int)(double) overallStatistics.get(0) + "</p></html>");
        totalSongs.setText("<html>Total Musics<br><p style='text-align:center;'>" +
                (int)(double) overallStatistics.get(1) + "</p></html>");
        totalPriceValue.setText("<html>Total Music Value<br><p style='text-align:center;'>" +
                overallStatistics.get(2) + "€</p></html>");
        totalSales.setText( "<html>Total Sales<br><p style='text-align:center;'>" +
                overallStatistics.get(3) + "€</p></html>");
        individualSales.setText( "<html>Individual Sales<br><p style='text-align:center;'>" +
                overallStatistics.get(4) + "€</p></html>");
        individualMusicCreated.setText( "<html>Music Created<br><p style='text-align:center;'>" +
                (int)(double) overallStatistics.get(5) + "</p></html>");
    }
    /**
     * Método responsável pela atualização dos dados relativos aos géneros dos álbuns de música que existem no sistema
     * @param albumStatistics Os valores que são associados ao tipo de género encontrado no álbum
     */
    public void updateSecondStatsPanel(ArrayList<Integer> albumStatistics){
        int totalAlbums = (int)(double)albumStatistics.get(0);
        int counter = 1;
        graphicStatsPanel.removeAll();
        for(Genre.GENRE ge : Genre.GENRE.values()){
            int genreValue = (int)(double) albumStatistics.get(counter);
            JLabel labelGenre = new JLabel("<html>"+ ge +"<br><p style='text-align:center;'>" +
                    genreValue + "</p></html>");
            RetangleBarChartComp bar = new RetangleBarChartComp(genreValue,totalAlbums);
            JPanel genrePanel = new JPanel(new BorderLayout());
            genrePanel.add(labelGenre, BorderLayout.NORTH);
            genrePanel.add(bar, BorderLayout.CENTER);
            graphicStatsPanel.add(genrePanel);
            southPanel.revalidate();
            southPanel.repaint();
            counter++;
        }
    }
}