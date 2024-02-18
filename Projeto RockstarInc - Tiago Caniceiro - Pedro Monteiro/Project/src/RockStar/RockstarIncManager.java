/**
 * @Authors Tiago Caniceiro & Pedro Monteiro
 * @Version 1.0
 */
package src.RockStar;

import src.GUIClassesSwing.GUIManager;
import src.RockStar.Model.*;

import java.io.Serializable;
import java.util.ArrayList;
/**
 * Classe principal do programa
 * Contem informação sobre clientes, criadores de música, músicas que existem no sistema
 * Classe que faz a ponte entre o back-end e o front-end
 * Classe que é inicializada no inicio do programa
 */
public class RockstarIncManager  implements Serializable {
    private ArrayList<User> clientList;
    private ArrayList<User> musicCreatorList;
    private User currentUser;
    private boolean isCurUserMusicCreator;
    private transient GUIManager guiManager;
    public RockstarIncManager(){
        this.clientList = new ArrayList<>();
        this.musicCreatorList = new ArrayList<>();
    }
    public void run(){
        startGUI();
    }
    public void startGUI() {
        guiManager = new GUIManager(RockstarIncManager.this);
        guiManager.run();
    }
    /**
     * Método para o utilizador já registado tentar entrar na aplicação
     * @param username o nome do utilizador
     * @param password a password do utilizador
     * @param isMCreator condição para verificar se o utilizador é criador
     * @param pin pin que é fornecido ao criador de música para aceder ao programa
     */
    public void loginAttempt(String username, String password, Boolean isMCreator, String pin) {
        boolean sucessfulLogin = false;
        if (isMCreator) {
            for (User us : musicCreatorList) {
                if (us.getUsername().equals(username) && us.getPassword().equals(password) &&
                        ((MusicCreator) us).getPin().equals(pin)) {
                    isCurUserMusicCreator = true;
                    sucessfulLogin = true;
                    currentUser = us;
                }
            }
        } else {
            //no caso do utilizador ser um cliente
            for (User us : clientList) {
                if (us.getUsername().equals(username) && us.getPassword().equals(password)) {
                    isCurUserMusicCreator = false;
                    sucessfulLogin = true;
                    currentUser = us;
                }
            }
        }
        //se login for bem sucedido, entra na aplicação com o utilizador respetivo
        if (sucessfulLogin) {
            guiManager.sucessfullLogin(currentUser.getUsername(), isMCreator);
            System.out.println("Successful Login");
        } else {
            guiManager.unsuccessfulLogin();
            System.out.println("Wrong Login");
        }
    }
    /**
     * Método para gerir a entrada de um novo utilizador na aplicação
     * @param name nome do utilizador que se quer registar
     * @param username username do utilizador que se quer registar
     * @param password password do utilizador para efetuar o registo
     * @param email email do utilizador
     * @param isMCreator condição para verificar se o utilizador que se quer registar é cliente ou criador de música
     * @param pin pin de 4 digitos fornecido ao novo criador de música que se regista
     */
    public void newUserAttempt(String name, String username, String password, String email, boolean isMCreator, String pin){
        boolean emailAlreadyExists = false;
        boolean usernameAlreadyExists = false;
        if(!isMCreator){
            for(User us : clientList){
                if(us.getEmail().equals(email)){
                    emailAlreadyExists = true;
                    System.out.println("email already exists");
                    //código que emite um aviso respetivo ao erro, gerido pelo método unsucessfulRegistration
                    guiManager.unsuccessfulRegistration(1);
                }
                if(us.getUsername().equals(username)){
                    usernameAlreadyExists = true;
                    System.out.println("username already exists");
                    guiManager.unsuccessfulRegistration(2);
                }
            }
        }
        if(isMCreator){
            for(User us : musicCreatorList){
                if(us.getEmail().equals(email)){
                    emailAlreadyExists = true;
                    System.out.println("email already exists");
                    guiManager.unsuccessfulRegistration(1);
                }
                if(us.getUsername().equals(username)){
                    usernameAlreadyExists = true;
                    System.out.println("username already exists");
                    guiManager.unsuccessfulRegistration(2);
                }
            }
        }
        //caso todas as condições passem no filtro de registo
        //efectua se o novo registo do utilizador
        boolean validRegistration = termValidationOnNewRegistration(name,username, password, email,  isMCreator, pin);
        if(!emailAlreadyExists && !usernameAlreadyExists && validRegistration){
            if(isMCreator) musicCreatorList.add(new MusicCreator(name, username, password, email, pin));
            else clientList.add(new Client(name, username,password,email,0));
            System.out.println("New user created");
            guiManager.successfulRegistration();
        }
    }
    /**
     * método sequencial para verificar se as credencias respeitam as regras impostas pelos programadores
     * @param name nome do utilizador que tem de respeitar certas caracteristicas
     * @param username username do utilizador que tem de respeitar certas caracteristicas
     * @param password password do utilizador que tem de respeitar certas caracteristicas
     * @param email email do utilizador que tem de respeitar certas caracteristicas
     * @param isCreator parametro que define se o utilizador é criador ou nao
     * @param pin parametro fornecido ao criador de música
     * @return retorna um registo válido que fica guardado na lista de utilizadores registados
     */
    public boolean termValidationOnNewRegistration(String name, String username, String password, String email,
                                                   boolean isCreator, String pin){
        boolean validRegistration = true;
        //Pin
        if(isCreator){
            boolean validPin = false;
            if(Validation.isAnNumber(pin)) validPin = true;
            else validRegistration = false;
            if(!validPin) guiManager.unsuccessfulRegistration(5);
            if(!Validation.pinSizeIsCorrect(pin)){
                guiManager.unsuccessfulRegistration(5);
                validRegistration = false;
            }
        }
        //Name
        boolean validName = Validation.hasNameOnlyLetters(name);
        boolean validNameLenght = Validation.nameHasRightLenght(name);
        if(!validName || !validNameLenght) {
            validRegistration = false;
            guiManager.unsuccessfulRegistration(6);
        }
        //Username
        if (!Validation.usernameOrPassWordHasRightLenght(username)){
            guiManager.unsuccessfulRegistration(4);
            validRegistration = false;
        }
        //Password
        if (!Validation.usernameOrPassWordHasRightLenght(password)){
            guiManager.unsuccessfulRegistration(7);
            validRegistration = false;
        }
        //Email
        boolean isEmailValid = Validation.emailValidation(email);
        if(!isEmailValid){
            guiManager.unsuccessfulRegistration(3);
        }
        return validRegistration;
    }
    /**
     * Procura por músicas e coleções de música com base no termo de pesquisa
     * Se o utilizador atual não for um criador de música, a pesquisa inclui todas as músicas disponíveis
     * associadas ou não a albuns.
     * Se o utilizador atual for um criador de música, a pesquisa inclui apenas as músicas criadas pelo proprio.
     * @param searchTerm O termo a ser pesquisado nos nomes das músicas, nomes dos artistas e nomes das coleções.
     * @return Um objeto Search que contem os resultados da pesquisa, incluindo músicas e albuns encontrados.
     */
    public Search search(String searchTerm) {
        ArrayList<Music> foundMusics= new ArrayList<>();
        ArrayList<Music> foundMusicsByArtist = new ArrayList<>();
        ArrayList<MusicCollection> foundMusicCollections = new ArrayList<>();
        if(!isCurUserMusicCreator){
            for(User us : musicCreatorList){
                for(Music m : us.getAllMusic()){
                    if(m.getName().toLowerCase().contains(searchTerm.toLowerCase())) {
                        foundMusics.add(m);
                    }
                    if(m.getMusicCreator().getName().toLowerCase().contains(searchTerm.toLowerCase())){
                        foundMusicsByArtist.add(m);
                    }
                }
            }
            for(User us :  musicCreatorList){
                for(MusicCollection mc : us.getAllCollections()){
                    if(mc.getName().toLowerCase().contains(searchTerm.toLowerCase()))foundMusicCollections.add(mc);
                }
            }
            for(User us :  clientList){
                for(MusicCollection mc : us.getAllCollections()){
                    if(mc.getName().toLowerCase().contains(searchTerm.toLowerCase()) && ((Playlist)mc).getPublicState()){
                        foundMusicCollections.add(mc);
                    }
                }
            }
            return new Search(foundMusics,foundMusicsByArtist,foundMusicCollections);
        } else {
            //se for um criador de música, retorna apenas a música desse proprio criador
            for(Music m : currentUser.getAllMusic()){
                if(m.getName().toLowerCase().contains(searchTerm.toLowerCase())) foundMusics.add(m);
            }
            return new Search(foundMusics);
        }
    }
    /**
     * Tenta criar uma nova lista aleatória de músicas com base no género escolhido e no número de músicas escolhidas.
     * Se não houver músicas suficientes desse genero para o numero que o utilizador escolheu devolve uma mensagem de
     * aviso.
     * No caso de ser possivel a criação de uma playlist aleatoria por existirem músicas suficientes, chama on método
     * randomPlaylistCreator();
     * @param genre O género músical escolhido para a nova lista aleatória.
     * @param nOfMusics O número de músicas para a nova lista aleatória.
     */
    public void newRandomPlaylistAttempt(Genre.GENRE genre, int nOfMusics){
        ArrayList<Music> allMusicOfTheChosenGenre = new ArrayList<>();
        for(User mc : musicCreatorList){
            for(Music m : mc.getAllMusic()){
                if(m.getGenre().equals(genre) && m.isActive()) allMusicOfTheChosenGenre.add(m);
            }
        }
        int maxSize = allMusicOfTheChosenGenre.size();
        if(maxSize < nOfMusics) {
            System.out.println("There are not enough musics");
            guiManager.notEnoughMusicForRandom(maxSize,false);
        }
        else{
            randomPlaylistCreation(nOfMusics,allMusicOfTheChosenGenre);
        }
    }
    /**
     * Cria uma nova lista de músicas aleatória com base no número de músicas escolhido
     * e em uma seleção aleatória de músicas do género escolhido.
     * Cria playlist aleatoria no caso das músicas selecionadas serem gratuitas/ já serem do utilizador.
     * Utiliza para tal o metodo randomMusicSelection() que retorna duas listas: lista de músicas gratuitas/ adquiridas
     * e a lista de músicas que tem de ser adquirida.
     * No caso de pelo menos uma música ter de ser adquirida, chama o método processorOnRandomToPayMusic();
     * @param nOfMusics O número de músicas desejado para a nova lista de reprodução aleatória.
     * @param allMusicOfTheChosenGenre Lista de todas as músicas do género escolhido.
     */
    public void randomPlaylistCreation(int nOfMusics, ArrayList<Music> allMusicOfTheChosenGenre){
        ArrayList<ArrayList<Music>> musicSelection = randomMusicSelection(nOfMusics,allMusicOfTheChosenGenre);
        //
        ArrayList<Music> randomMusicSelection = musicSelection.get(0);
        ArrayList<Music> notFreeMusicSelection = musicSelection.get(1);
        boolean successfullyCreated;
        if(!notFreeMusicSelection.isEmpty()){
            successfullyCreated = processorOnRandomToPayMusic(randomMusicSelection, notFreeMusicSelection, nOfMusics,
                    allMusicOfTheChosenGenre);
        }
        else {
            currentUser.newCollection(randomMusicSelection);
            successfullyCreated = true;
        }
        if(successfullyCreated) guiManager.randomPLSuccssefullyCreated();
    }
    /**
     * Método que realiza a seleção de músicas de forma aleatória, utilizando o método randomIndexVector(), que cria
     * uma lista de indices aleatorio que irá corresponder aos indices da lista de músicas do género selecionadas, que
     * foram criadas anteriormente.
     * Todas as músicas que são pagas e não adquiridas pelo utilizador irão para uma segunda lista.
     * @param nOfMusics número de músicas que se quer escolher
     * @param allMusicOfTheChosenGenre tipo de género do qual queremos as músicas.
     * @return as listas de músicas gratuitas e as não-gratuitas.
     */
    public ArrayList<ArrayList<Music>> randomMusicSelection(int nOfMusics, ArrayList<Music> allMusicOfTheChosenGenre){
        ArrayList<ArrayList<Music>> lists = new ArrayList<>();
        ArrayList<Music> randomMusicSelection = new ArrayList<>();
        ArrayList<Music> notFreeMusicSelection = new ArrayList<>();
        int[] listOfIndexes = randomIndexVector(nOfMusics, allMusicOfTheChosenGenre.size());
        for (int listOfIndex : listOfIndexes) {
            Music music = allMusicOfTheChosenGenre.get(listOfIndex);
            if (currentUser.getAllMusic().contains(music)) {
                randomMusicSelection.add(music);
            } else if (!currentUser.getAllMusic().contains(music) && music.getPrice() == 0) {
                currentUser.newMusicToUserMainCollection(music);
                randomMusicSelection.add(music);
            } else if(!currentUser.getAllMusic().contains(music) && music.getPrice() > 0){
                notFreeMusicSelection.add(music);
            }
        }
        lists.add(randomMusicSelection);
        lists.add(notFreeMusicSelection);
        return lists;
    }
    /**
     * Método responsavel pela gestão da criação de novas listas aleatorias, cujas músicas selecionadas sao pagas e
     * que não estão adquiridas pelo utilizador.
     * Este método chama um método acessório que calcula o preco de uma lista de músicas selecionadas, para depois
     * apresentar a opção de compra ao utilizador.
     * Este método é tambem o responsavel pela comunicação com a interface gráfica e envia informações ao utilizador
     * como a lista de músicas a serem adquiridas para a construção da playlist, o preco dessa lista e uma boolean
     * que confirma a possibilidade do utilizador comprar a lista. Esta resposta ativa um switch statement, que
     * redireciona a lista de músicas para o carrinho de compras, permite e executa a compra das músicas automaticamente
     * ou redireciona para um novo método de criação de playlists aleatórias com músicas adquiridas ou grátis
     * newRandomPlaylistOnlyFree().
     * @param randomMusicSelection lista de música escolhida aleatóriamente
     * @param notFreeMusicSelection lista de música que não é gratuita
     * @param nOfMusics número de músicas escolhidas
     * @param allMusicOfTheChosenGenre lista de músicas com o género escolhido
     * @return retorna a playlist aleatória criada com sucesso.
     */
    public boolean processorOnRandomToPayMusic(ArrayList<Music> randomMusicSelection, ArrayList<Music> notFreeMusicSelection,
                                               int nOfMusics, ArrayList<Music> allMusicOfTheChosenGenre){
        double totalPrice = (double) Math.round(musicListPriceCalculator(notFreeMusicSelection)*100)/100;
        double balance = ((Client)currentUser).getBalance();
        boolean canBuy  = totalPrice < balance;
        int userOption = guiManager.randomPlaylistToPaySongsChoose(notFreeMusicSelection, totalPrice,canBuy);
        boolean successfullyCreated = false;
        switch (userOption){
            case 1:
                for (Music m : notFreeMusicSelection){
                    ((Client)currentUser).addMusicToBasket(m); //Se optar por adicionar ao carrinho apenas as músicas gratuitas serão adicionadas
                }
                currentUser.newCollection(randomMusicSelection);
                successfullyCreated = true;
                break;
            case 2:
                if(canBuy){
                    ((Client)currentUser).validationOfAquisition(notFreeMusicSelection);
                    randomMusicSelection.addAll(notFreeMusicSelection);
                }
                else System.out.println("Not enough money");
                currentUser.newCollection(randomMusicSelection);
                successfullyCreated = true;
                break;
            case 3:
                newRandomPlaylistOnlyFree(allMusicOfTheChosenGenre,nOfMusics);
                break;
        }
        return successfullyCreated;
    }
    public double musicListPriceCalculator(ArrayList<Music> musicList){
        double totalPrice = 0;
        for (Music m : musicList){
            totalPrice += m.getPrice();
        }
        return totalPrice;
    }
    /**
     * Método que cria a playlist aleatória apenas com as músicas gratuitas
     * @param musicOfTheChosenGenre músicas do género escolhido
     * @param nOfMusics número de músicas desejadas para a playlist.
     */
    public void newRandomPlaylistOnlyFree(ArrayList<Music> musicOfTheChosenGenre, int nOfMusics){
        ArrayList<Music> onlyFreeMusicByGenre = new ArrayList<>();
        ArrayList<Music> freeMusicSelection = new ArrayList<>();
        for(Music m : musicOfTheChosenGenre){
            if(currentUser.getAllMusic().contains(m)) onlyFreeMusicByGenre.add(m);
            else if (m.getPrice() == 0) onlyFreeMusicByGenre.add(m);
        }
        int maxSyzeFreeMusic = onlyFreeMusicByGenre.size();
        if(maxSyzeFreeMusic >= nOfMusics){
            int[] listOfIndexesFreeMusic = randomIndexVector(nOfMusics, onlyFreeMusicByGenre.size());
            for (int listOfIndexesfree : listOfIndexesFreeMusic) {
                Music music = onlyFreeMusicByGenre.get(listOfIndexesfree);
                if (currentUser.getAllMusic().contains(music)) {
                    freeMusicSelection.add(music);
                } else  {
                    currentUser.newMusicToUserMainCollection(music);
                    freeMusicSelection.add(music);
                }
            }
            currentUser.newCollection(freeMusicSelection);
            guiManager.randomPLSuccssefullyCreated();
        } else{
            guiManager.notEnoughMusicForRandom(maxSyzeFreeMusic,true);
        }
    }
    /**
     * Método que inicia a criação de uma playlist aleatória em que apenas músicas gratuitas foram selecioknadas
     * @param sizeOfNewVector define o tamanho do novo vector.
     * @param sizeOfSample define o alcance de indexes para a escolha aleatória
     * @return retorna uma lista de indexes.
     */
    public int[] randomIndexVector(int sizeOfNewVector, int sizeOfSample){
        int[] listOfIndexes = new int[sizeOfNewVector];
        ArrayList<Integer> addedIndexes = new ArrayList<>();
        for (int i = 0; i < sizeOfNewVector; i++) {
            int randomIndex;
            do {
                randomIndex = (int) (Math.floor(Math.random() * sizeOfSample));
            } while (addedIndexes.contains(randomIndex));

            listOfIndexes[i] = randomIndex;
            addedIndexes.add(randomIndex);
        }
        return listOfIndexes;
    }
    /**
     * Método que gere a validação dos termos para o nome e preço de uma música para a implementar no sistema.
     * Adiciona a música à lista geral de músicas no sistema e as respectivas listas de utilizadores.
     * @param name nome da música.
     * @param priceString preço da música.
     * @param genre género da música.
     */
    public void newMusic(String name, String priceString, Genre.GENRE genre){
        boolean validatedName = musicNameValidation(name);
        double price = musicPriceProcessor(priceString);
        if(validatedName && price != -1){
            Music music = new Music(name, genre,(MusicCreator) currentUser, price);
            currentUser.newMusicToUserMainCollection(music);
            guiManager.newMusicCreated();
        }
    }
    /**
     * Método que lida com a tentativa de edição de música. Verifica se os parametros da música estão corretos,
     * gere a lógica da edição de música e comunica com a interface gráfica aquando sucesso na edição.
     * @param selectedMusic Musica selecionada para editar.
     * @param name Parametro para validação do nome da música.
     * @param priceString preço associado à música selecionada.
     * @param genre género da música.
     * @param state determina o estado da música, se está activa ou não.
     */
    public void musicEditionAttempt(Music selectedMusic, String name, String priceString, Genre.GENRE genre, int state){
        boolean musicEdited = false;
        if(!name.isEmpty() && musicNameValidation(name)){
            selectedMusic.setName(name);
            musicEdited = true;
        }
        if(!priceString.isEmpty()){
            double price = musicPriceProcessor(priceString);
            if(price != -1) {
                selectedMusic.setPrice(price);
                musicEdited = true;
            }
        }
        if(genre != selectedMusic.getGenre()){
            selectedMusic.setGenre(genre);
            if(selectedMusic.getAssociatedAlbum() != null)selectedMusic.getAssociatedAlbum().calculateMainGenre();
            musicEdited = true;
        }
        boolean selectedState;
        if(state == 0) selectedState = true;
        else selectedState = false;

        if(selectedState != selectedMusic.isActive()){
            selectedMusic.setActive(selectedState);
            musicEdited = true;
        }
        if(musicEdited) guiManager.musicSuccessfullyEdited();
    }
    /**
     * Método para validação do nome da música consoante as regras desejadas.
     * @param name Nome da música que vai ser verificada consonte os parametros impostos.
     * @return retorna o nome da música validado.
     */
    public boolean musicNameValidation(String name){
        boolean validatedName = true;
        boolean nameAlreadyExists = false;
        for(Music m : currentUser.getAllMusic()){
            if(m.getName().equals(name)) {
                nameAlreadyExists = true;
                validatedName = false;
            }
        }
        if(!Validation.validMusicName(name)) {
            guiManager.musicAttemptError(1);
            validatedName = false;
        }
        else if (nameAlreadyExists) {
            guiManager.musicAttemptError(3);
            validatedName = false;
        }
        return validatedName;
    }
    /**
     * Método para validação do preço colocado numa música.
     * @param priceString Parâmetro de preço que se quer colocar numa música.
     * @return retorna o preço da música.
     */
    public double musicPriceProcessor(String priceString){
        priceString = priceString.replace(',','.');
        priceString = priceString.replace("€","");
        double price = -1;
        try{
            price = Double.parseDouble(priceString);
        }catch (NumberFormatException e){
            guiManager.musicAttemptError(0);
        }
        if (price > 50 || price < 0) {
            guiManager.musicAttemptError(2);
            price = -1;
        }
        return price;
    }
    public void logout(){
        currentUser = null;
    }
    public ArrayList<Music> getCurrentUserALlMusic(){
        return currentUser.getAllMusic();
    }
    public ArrayList<MusicCollection> getCurretUserAllCollections(){
        return currentUser.getAllCollections();
    }
    public double getCurrentUserBalance(){
        return (double) Math.round(((Client) currentUser).getBalance() * 100)/100;
    }
    public Playlist getClientAllMusicAsCollection(){
        return new Playlist("Owned Music",(Client) currentUser,currentUser.getAllMusic());
    }
    public Album getMusicCreatorAllMusicAsCollection(){
        return new Album("Created Music",(MusicCreator) currentUser,currentUser.getAllMusic());
    }
    public int getClientEvaluation(Music music){
        return music.getClientEvaluationForSpecificMusic(currentUser);
    }
    public ArrayList<Music> getUserBasketList(){
        return ((Client)currentUser).getMusicOnBasketList();
    }
    public void removeMusicFromCollection(Music selectedMusic,MusicCollection selectedPlaylist){
        selectedMusic.setAssociatedAlbum(null);
        currentUser.removeMusicFromCollection(selectedMusic,selectedPlaylist);
    }
    public void addMusicToCollection(Music selectedMusic,MusicCollection cl){
        currentUser.addMusicToCollection(selectedMusic,cl);
    }
    public void newCollection(String collection){
        currentUser.newCollection(collection);
    }
    public void removeMusicCollection(MusicCollection selected){
        currentUser.removeMusicCollection(selected);
    }
    public void newMusicToAllCollection(Music selectedMusic){
        currentUser.newMusicToUserMainCollection(selectedMusic);
    }
    public void evaluateMusic(int evaluation, Music selectedMusic){
        selectedMusic.addEvaluation((Client)currentUser, evaluation);
    }
    public void validationOfAquisition(){
        ((Client)currentUser).validationOfAquisition(getUserBasketList());
        ((Client)currentUser).getMusicOnBasketList().clear();
    }
    public void addMoney(double money){
        ((Client)currentUser).addMoney(money);
    }
    public void addMusicToBasket(Music selectedMusic){
        ((Client)currentUser).addMusicToBasket(selectedMusic);
    }
    public int totalUsers(){return clientList.size() + musicCreatorList.size();}
    public double musicTotalPriceValue(){ //valor total músicas no sistema
        double price = 0.0;
        for(User mc : musicCreatorList){
            for (Music m : mc.getAllMusic()){
                price += m.getPrice();
            }
        }
        return price;
    }
    public int totalSongs(){
        int countMusic = 0;
        for(User mc : musicCreatorList){
            countMusic += mc.getAllMusic().size();
        }
        return countMusic;
    }
    public int totalAlbumsByGenre(Genre.GENRE albumGenre){
        int cont = 0;
        for (User us : musicCreatorList){
            for (MusicCollection ab : us.getAllCollections()){
                if(((Album)ab).getMainGenre() != null){
                    if (((Album)ab).getMainGenre().equals(albumGenre)){
                        cont++;
                    }
                }
            }
        }
        return cont;
    }
    public double totalSalesValue() {
        double totalValue = 0.0;
        for (User us :  clientList){
            for (MusicAcquisition ma : ((Client) us).getListOfAcquisitions()){
                totalValue += ma.getTotalPrice();
            }
        }
        return totalValue;
    }
    public double salesCurrentUser(){
        return ((MusicCreator)currentUser).getTotalValueSales();
    }
    public int currentUserTotalMusicCreated(){
        return currentUser.getAllMusic().size();
    }

    /**
     * Método que retorna os valores associados às estatísticas gerais do criador de música
     * @return retorna os valores estatísticos
     */
    public ArrayList<Double> getOverallStatistics(){
        ArrayList<Double> overallStatistics =  new ArrayList<>();
        overallStatistics.add((double)totalUsers());
        overallStatistics.add((double)totalSongs());
        overallStatistics.add((double)Math.round(musicTotalPriceValue()*100)/100);
        overallStatistics.add((double)Math.round(totalSalesValue()*100)/100);
        overallStatistics.add((double)Math.round(salesCurrentUser()*100)/100);
        overallStatistics.add((double)currentUserTotalMusicCreated());
        return overallStatistics;
    }

    /**
     * Método que retorna os géneros dos álbuns para análise estatística
     * @return retorna os géneros dos álbuns do criador de música
     */
    public ArrayList<Integer> getAlbumTypeStatistics(){
        ArrayList<Integer> albumStatistics =  new ArrayList<>();
        ArrayList<Integer> albumCountByGenre = new ArrayList<>();
        for(Genre.GENRE ge : Genre.GENRE.values()){
            albumCountByGenre.add(totalAlbumsByGenre(ge));
        }
        albumCountByGenre.add(totalAlbumsByGenre(null));
        int totalAlbuns = 0;
        for(Integer i: albumCountByGenre){
            totalAlbuns += i;
        }
        albumStatistics.add(totalAlbuns);
        albumStatistics.addAll(albumCountByGenre);
        return albumStatistics;
    }
    public ArrayList<MusicAcquisition> getPurchaseHistory(){
        return ((Client)currentUser).getListOfAcquisitions();
    }
}