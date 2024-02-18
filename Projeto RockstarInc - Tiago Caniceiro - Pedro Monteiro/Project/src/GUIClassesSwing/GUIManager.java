package src.GUIClassesSwing;

import src.GUIClassesSwing.OtherClasses.EditMusicDialog;
import src.GUIClassesSwing.OtherClasses.LogRegFrame;
import src.GUIClassesSwing.OtherClasses.RandonPlaylistSelectionDialog;
import src.RockStar.*;
import src.GUIClassesSwing.*;
import src.RockStar.Model.*;

import javax.swing.*;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Classe responsável pela gestão e ligação da Classe Rockstar (e consequentemente toda a lógica da aplicação) à interface
 * gráfica com a qual o utilizador interage
 */
public class GUIManager  {
    private ClientGUI clientFrame;
    private MusicCreatorGUI musicCreatorFrame;
    private LoginRegistrationGUI loginRegistrationGUI;
    private LogRegFrame loginFrame;
    private LogRegFrame registrationFrame;
    private final RockstarIncManager logicManager;

    /**
     * Construtor do gestor gráfico que liga à classe RockstarManager
     * @param logicManager o construtor da classe RockstarManager
     */
    public GUIManager(RockstarIncManager logicManager) {
        this.logicManager = logicManager;
    }

    public void run(){
        loginRegistrationGUI = new LoginRegistrationGUI(GUIManager.this);
    }

    /**
     * Método que mostra ao utilizador a tentativa de login e que comunica com a classe RockstarManager.
     * No caso do login ser bem sucedido, inicia a frame do utilizador que está a fazer o login (Cliente ou Criador de
     * Musica)
     * @param userField campo para o utilizador colocar o username.
     * @param passToString campo para colocar a password.
     * @param isMCreator botão para identificar se o utilizador é um criador de musica.
     * @param pin campo de pin para validar o login de um criador de musica.
     */
    public void loginAttempt(String userField, String passToString, boolean isMCreator, String pin){
        logicManager.loginAttempt(userField,passToString,isMCreator,pin);
    }
    public void unsuccessfulLogin(){
        JOptionPane.showMessageDialog(null,"Unsuccessful Login");
    };

    /**
     * Método que permite ao utilizador registar-se na plataforma
     * Comunica com os métodos na Classe RockstarManager
     * @param name campo para o utilizador colocar o nome
     * @param usernameField campo para o utilizador colocar o seu username
     * @param password campo para o utilizador colocar a password
     * @param email campo para o utilizador colocar o seu email
     * @param isMCreator botão para identificar o utilizador como criador de musica
     * @param pin campo para o utilizador registar o pin
     */
    public void newUserAttempt(String name,String usernameField,String password,String email,boolean isMCreator, String pin){
        logicManager.newUserAttempt(name, usernameField,password,email, isMCreator, pin);
    }
    public void successfulRegistration(){
        JOptionPane.showMessageDialog(null,"New User Created");
    }

    /**
     * método para mostrar ao utilizador uma mensagem de erro consoante o erro que o programa detectar
     * @param cod código de erro respetivo
     */
    public void unsuccessfulRegistration(int cod){
        switch (cod){
            case 1 : JOptionPane.showMessageDialog(null,
                    "Unsuccessful Registration - The email already exists");
                break;
            case 2 : JOptionPane.showMessageDialog(null,
                    "Unsuccessful Registration - The username already exists");
                break;
            case 3: JOptionPane.showMessageDialog(null,
                    "That email is not valid");
                break;
            case 4: JOptionPane.showMessageDialog(null,
                    "The username have size requirements (MIN:3 MAX 20) special characters allowed");
                break;
            case 5: JOptionPane.showMessageDialog(null,
                    "The pin is not valid (MIN:4 MAX:8) only digits");
                break;
            case 6: JOptionPane.showMessageDialog(null,
                    "Invalid Name (MIN:3 MAX 30) only letters");
                break;
            case 7: JOptionPane.showMessageDialog(null, "Invalid Password (MIN:3 MAX 20)");
                break;
        }

    }

    /**
     * Método para mostrar ao utilizador na tentativa de criação de uma playlist aleatória
     * @param selectedGenre género selecionado para a playlist aleatória
     * @param nMusics número de musicas escolhidas para a playlist aleatória
     */
    public void randomPlaylistCreationAttempt(Genre.GENRE selectedGenre,int nMusics){
        logicManager.newRandomPlaylistAttempt(selectedGenre,nMusics);
    }

    public void notEnoughMusicForRandom(int maxSize,boolean freeMusics){
        String freeMusicsString = "";
        if(freeMusics) freeMusicsString = "free";
        JOptionPane.showMessageDialog(null,"Not enough musics for this random playlist " +
                "\nOn the selected genre there are only " + maxSize + " " + freeMusicsString + " musics available");
    }



    //---------------------------------Frame and JDialog management--------------------------------

    /**
     *
     * @return
     */
    public LogRegFrame creationLoginFrame(){
        LogRegFrame lf = new LogRegFrame();
        this.loginFrame = lf;
        return lf;
    }

    public LogRegFrame creationRegistrationFrame(){
        LogRegFrame rf = new LogRegFrame();
        this.registrationFrame = rf;
        return rf;
    }

    public void logoutClient() {
        clientFrame.dispose();
        logicManager.logout();
        run();
    }

    public void logoutMCreator() {
        musicCreatorFrame.dispose();
        logicManager.logout();
        run();
    }
    public void sucessfullLogin(String username, boolean isMCreator){
        if(isMCreator){
            musicCreatorFrame = new MusicCreatorGUI(username, this);
            loginRegistrationGUI.setVisible(false);
            loginFrame.dispose();
            if(registrationFrame != null) registrationFrame.dispose();
        } else {
            clientFrame = new ClientGUI(username,this);
            loginRegistrationGUI.setVisible(false);
            loginFrame.dispose();
            if(registrationFrame != null) registrationFrame.dispose();
        }
    }

    public int randomPlaylistToPaySongsChoose(ArrayList<Music> notFreeMusicSelection, double totalPrice, boolean canBuy) {
        RandonPlaylistSelectionDialog rpp = new RandonPlaylistSelectionDialog(this,
                clientFrame, notFreeMusicSelection,totalPrice,canBuy);
        int userOption = rpp.getReturnValue();
        System.out.println(userOption);
        return userOption;
    }

    public void randomPLSuccssefullyCreated(){
        JOptionPane.showMessageDialog(null,"Random playlist created");
        clientFrame.updateMusicJTableModel(logicManager.getCurrentUserALlMusic());
        clientFrame.updateBasketJListModel();
        clientFrame.updateBalance();
        clientFrame.updateTotalBascketPrice();
    }

    public Search newSearch(String searchTextField){
        return logicManager.search(searchTextField);
    }

    public ArrayList<MusicCollection> getUserAllCollection(){
        return logicManager.getCurretUserAllCollections();
    }

    public double getUserBalance(){
        return logicManager.getCurrentUserBalance();
    }
    public int getClientEvaluation(Music music){
        return logicManager.getClientEvaluation(music);
    }

    public ArrayList<Music> getUserAllMusic(){
        return logicManager.getCurrentUserALlMusic();
    }

    public Playlist getCorrentUserMainCollectionClient(){
        return logicManager.getClientAllMusicAsCollection();
    }

    public Album getCorrentUserMainCollectionMusicCreator(){
        return logicManager.getMusicCreatorAllMusicAsCollection();
    }

    public ArrayList<Music> getListOfMusicsToBuy(){
        return logicManager.getUserBasketList();
    }

    public void removeMusicFromCollection(Music selectedMusic,MusicCollection selectedPlaylist){
        logicManager.removeMusicFromCollection(selectedMusic,selectedPlaylist);
    }

    public void addMusicToCollection(Music selectedMusic,MusicCollection cl){
        logicManager.addMusicToCollection(selectedMusic,cl);
    }

    public void evaluateMusic(int evaluation, Music selectedMusic){
        logicManager.evaluateMusic(evaluation, selectedMusic);
    }

    public void newCollection(String playlistName){
       logicManager.newCollection(playlistName);
    }

    public void validationOfAquisition(){
        logicManager.validationOfAquisition();
    }

    public void addMoney(double money){
        logicManager.addMoney(money);
    }

    public void removeMusicCollection(MusicCollection selected){
        logicManager.removeMusicCollection(selected);
    }

    public void newMusicToAllCollection(Music selectedMusic){
        logicManager.newMusicToAllCollection(selectedMusic);
    }

    public void addMusicToMusicToBuy(Music selectedMusic){
        logicManager.addMusicToBasket(selectedMusic);
    }

    public void newMusicAttempt(String musicNameTextField, String priceTextField, Genre.GENRE selectedGender){
        logicManager.newMusic(musicNameTextField, priceTextField, selectedGender);
    }

    /**
     * Método que mostra uma janela de diálogo consoante o erro cometifo na criação de musica
     * @param errorN tipo de erro
     */
    public void musicAttemptError(int errorN){
        switch (errorN){
            case 0:
                JOptionPane.showMessageDialog(null,"Price format error");
                break;
            case 1:
                JOptionPane.showMessageDialog(null,"Music name should have 1 - 20 characters");
                break;
            case 2:
                JOptionPane.showMessageDialog(null,"Music price should be between 0€ and 50€");
                break;
            case 3:
                JOptionPane.showMessageDialog(null,
                        "You already have created a music with this name");
        }
    }
    public void newMusicCreated(){
        JOptionPane.showMessageDialog(null,"New Music Created");
        musicCreatorFrame.updateMusicJTableModel(getCorrentUserMainCollectionMusicCreator().getMusicList());
        musicCreatorFrame.updateFirstStatsPanel(getStatistics());
    }

    /**
     * Método que permite chamar a janela de diálogo para a edição de musica e que atualiza as estatisticas a tempo real
     * @param selectedMusic Musica selecionada para editar
     */
    public void editMusicDialogCall(Music selectedMusic){
        EditMusicDialog editMusicDialog = new EditMusicDialog(this, musicCreatorFrame, selectedMusic);
        String name = editMusicDialog.getNewName();
        String price = editMusicDialog.getNewPrice();
        Genre.GENRE genre = editMusicDialog.getSelectedGender();
        int state = editMusicDialog.getMusicState();
        logicManager.musicEditionAttempt(selectedMusic,name, price, genre, state);
        musicCreatorFrame.updateSecondStatsPanel(getAlbumTypeStatistics());
    }
    public ArrayList<MusicAcquisition> getPurchaseHistory(){
        return logicManager.getPurchaseHistory();
    }
    public void musicSuccessfullyEdited(){
        JOptionPane.showMessageDialog(null,
                "Music Successfully Edited");
    }

    public ArrayList<Double> getStatistics(){
        return logicManager.getOverallStatistics();
    }
    public ArrayList<Integer> getAlbumTypeStatistics(){
        return logicManager.getAlbumTypeStatistics();
    }
}
