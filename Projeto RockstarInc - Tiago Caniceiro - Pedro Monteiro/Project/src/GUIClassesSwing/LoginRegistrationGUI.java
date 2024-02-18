package src.GUIClassesSwing;

import src.GUIClassesSwing.OtherClasses.ImagePaths;
import src.GUIClassesSwing.OtherClasses.LogRegFrame;

import javax.swing.*;
import java.awt.*;

/**
 * Classe responsável pela interface gráfica de login e registro de usuários.
 * Esta classe cria a janela principal para a autenticação e registro, fornecendo
 * campos de entrada para informações do usuário, como nome de usuário, senha, e-mail,
 * além de opções específicas para criadores de música.
 */
public class LoginRegistrationGUI extends JFrame {
    GUIManager guiManager;
    private JLabel loginPinLbl;
    private JLabel registPinLbl;
    private JRadioButton musicCreatorLoginBtn;
    private JRadioButton musicCreatorRegisBtn;
    private JTextField usernameFieldOnLogin;
    private JPasswordField userPasswordFieldOnLogin;
    private JTextField firstNameOnRegistration;
    private JTextField usernameOnRegistration;
    private JTextField loginPinField;
    private JPasswordField userPasswordFieldOnRegistration;
    private JPasswordField userPasswordFieldOnRegistrationConf;
    private JTextField emailOnRegistration;
    private JTextField registerPinField;
    private LogRegFrame loginFrame = null;
    private LogRegFrame registrationFrame = null;
    private JPanel loginPanel;
    private JPanel registrationPanel;
    /**
     * Construtor da classe LoginRegistrationGUI.
     * Inicializa a janela de login e registo com todas as suas componentes,
     * incluindo botões, campos de texto, e outros elementos gráficos.
     * @param guiManager Referência ao gestor de interface gráfica que controla esta classe.
     */
    public LoginRegistrationGUI(GUIManager guiManager) {
        super("Login and Registration"); // Define o título da janela
        this.guiManager = guiManager;
        // Inicializa os componentes gráficos
        initComponents();

        // Configurações padrão da janela
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(750, 750);
        setUndecorated(true);
        setVisible(true);
        setLocationRelativeTo(null);
        setResizable(false);
        ImageIcon imageIcon = new ImageIcon(ImagePaths.APP_ICON);
        setIconImage(imageIcon.getImage());
    }
    /**
     * Método responsável por inicializar todos os componentes gráficos da janela.
     * Configura o layout, botões, campos de texto e outros elementos necessários para
     * a interface de login e registo.
     */
    private void initComponents() {
        JLabel background = new JLabel(new ImageIcon(ImagePaths.Login_Registration_Background));

        Container mainContainer = new Container();
        mainContainer.setLayout(new BorderLayout());
        JButton btnLoginOnMain = new JButton("Login");
        JButton btnRegistrationOnMain = new JButton("Registration");
        JButton btnExitOnMain = new JButton("Exit");
        Container btnContainer = new Container();
        btnContainer.setLayout(new FlowLayout());
        btnContainer.add(btnLoginOnMain);
        btnContainer.add(btnRegistrationOnMain);
        btnContainer.add(btnExitOnMain);

        mainContainer.add(btnContainer,"South");
        mainContainer.add(background);

        btnLoginOnMain.addActionListener(e -> creationOfLoginAndRegistrationFrame(1));
        btnRegistrationOnMain.addActionListener(e -> creationOfLoginAndRegistrationFrame(2));
        btnExitOnMain.addActionListener(e -> System.exit(0));

        add(mainContainer);
    }
    /**
     * Método para criar e exibir a janela de login ou registo com base na opção escolhida.
     * Configura as propriedades da janela e adiciona os elementos gráficos necessários.
     *
     * @param option valor inteiro que determina qual janela será criada: 1 para Login, 2 para Registro.
     */
    public void creationOfLoginAndRegistrationFrame(int option){
        switch (option){
            case 1:
                if(loginFrame == null){
                    loginFrame = guiManager.creationLoginFrame();
                    GridBagConstraints constraints = loginFrame.getConstraints();

                    loginPanel =  new JPanel(new GridBagLayout());
                    JLabel userLoginLbl = new JLabel("Username");
                    usernameFieldOnLogin = new JTextField("",15);
                    JLabel passwordLoginLbl = new JLabel("Password");
                    userPasswordFieldOnLogin = new JPasswordField("", 15);
                    musicCreatorLoginBtn = new JRadioButton("Music Creator Access");
                    musicCreatorLoginBtn.addActionListener(e -> onRadioLoginMusicCreatorBtnClick());
                    loginPinLbl = new JLabel("PIN");
                    loginPinField = new JTextField("",6);
                    JButton loginConfirmationBtn = new JButton("Login");
                    loginConfirmationBtn.addActionListener(e -> onLoginConfirmationBtnClick());

                    loginPanel.add(userLoginLbl,constraints);
                    loginPanel.add(usernameFieldOnLogin,constraints);
                    loginPanel.add(passwordLoginLbl,constraints);
                    loginPanel.add(userPasswordFieldOnLogin, constraints);
                    loginPanel.add(musicCreatorLoginBtn,constraints);
                    loginPanel.add(loginPinLbl,constraints);
                    loginPanel.add(loginPinField,constraints);
                    loginPanel.add(loginConfirmationBtn, constraints);
                    loginPinLbl.setVisible(false);
                    loginPinField.setVisible(false);

                    loginFrame.setContentPane(loginPanel);
                    loginFrame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
                    loginFrame.setVisible(true);
                } else loginFrame.setVisible(true);

                break;
            case 2:
                if(registrationFrame == null){
                    registrationFrame = guiManager.creationRegistrationFrame();
                    GridBagConstraints constraints = registrationFrame.getConstraints();

                    registrationPanel = new JPanel(new GridBagLayout());
                    JLabel lblFirstNameOnReg = new JLabel("First Name");
                    firstNameOnRegistration =  new JTextField("",15);
                    JLabel lblUsernameOnReg = new JLabel("Username");
                    usernameOnRegistration =  new JTextField("",15);
                    JLabel lblPasswordOnReg = new JLabel("Password");
                    userPasswordFieldOnRegistration = new JPasswordField("",15);
                    JLabel lblPasswordConfOnReg = new JLabel("Password Confirmation");
                    userPasswordFieldOnRegistrationConf = new JPasswordField("",15);
                    JLabel lblEmailOnReg = new JLabel("Email");
                    emailOnRegistration =  new JTextField("",15);
                    musicCreatorRegisBtn = new JRadioButton("Music Creator Registration");
                    musicCreatorRegisBtn.addActionListener(e -> onRadioRegistMusicCreatorBtnClick());
                    registPinLbl = new JLabel("PIN");
                    registerPinField = new JTextField("",6);
                    JButton btnConfirmRegistration = new JButton("Confirm Registration");
                    btnConfirmRegistration.addActionListener(e -> onbtnConfirmRegistrationClick());

                    registrationPanel.add(lblFirstNameOnReg,constraints);
                    registrationPanel.add(firstNameOnRegistration,constraints);
                    registrationPanel.add(lblUsernameOnReg,constraints);
                    registrationPanel.add(usernameOnRegistration,constraints);
                    registrationPanel.add(lblPasswordOnReg,constraints);
                    registrationPanel.add(userPasswordFieldOnRegistration,constraints);
                    registrationPanel.add(lblPasswordConfOnReg,constraints);
                    registrationPanel.add(userPasswordFieldOnRegistrationConf,constraints);
                    registrationPanel.add(lblEmailOnReg,constraints);
                    registrationPanel.add(emailOnRegistration,constraints);
                    registrationPanel.add(musicCreatorRegisBtn,constraints);
                    registrationPanel.add(registPinLbl,constraints);
                    registrationPanel.add(registerPinField,constraints);
                    registrationPanel.add(btnConfirmRegistration,constraints);
                    registPinLbl.setVisible(false);
                    registerPinField.setVisible(false);

                    registrationFrame.setContentPane(registrationPanel);
                    registrationFrame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
                    registrationFrame.setVisible(true);
                } else registrationFrame.setVisible(true);
                break;
        }
    }
    /**
     * Método chamado quando o botão de confirmação de login é clicado.
     * Realiza a tentativa de login utilizando as informações fornecidas pelo utilizador,
     * como nome, senha e, se aplicável, PIN para acesso de criador de música.
     */
    public void onLoginConfirmationBtnClick(){
        String userField = usernameFieldOnLogin.getText();
        char[] passField = userPasswordFieldOnLogin.getPassword();
        String passToString = new String(passField);
        String pin = loginPinField.getText();
        if(musicCreatorLoginBtn.isSelected()){
            guiManager.loginAttempt(userField,passToString,true,pin);
        } else if (!musicCreatorLoginBtn.isSelected()) {
            guiManager.loginAttempt(userField,passToString,false,pin);
        }
    }
    /**
     * Método chamado quando o botão de confirmação de registo é clicado.
     * Regista um novo utilizador com as informações fornecidas, como nome,
     * senha, confirmação de senha, e-mail e, se aplicável, PIN para registro de criador de música.
     */
    public void onbtnConfirmRegistrationClick(){
        String name = firstNameOnRegistration.getText();
        String usernameField = usernameOnRegistration.getText();
        char[] passField = userPasswordFieldOnRegistration.getPassword();
        char[] passFieldConfirmation = userPasswordFieldOnRegistrationConf.getPassword();
        String email = emailOnRegistration.getText();
        String passToString = new String(passField);
        String passToStringConf = new String(passFieldConfirmation);
        String pin = registerPinField.getText();
        if(!passToString.equals(passToStringConf)) {
            System.out.println("different passwords");
            JOptionPane.showMessageDialog(null,"The two passwords are different");
        }
        else{
            if(musicCreatorRegisBtn.isSelected()){
                guiManager.newUserAttempt(name,usernameField,passToString,email,true,pin);
            } else if (!musicCreatorRegisBtn.isSelected()) {
                guiManager.newUserAttempt(name,usernameField,passToString,email, false,pin);
            }
        }
    }
    /**
     * Método chamado quando o botão de rádio para login de criador de música é clicado.
     * Ativa ou desativa a visibilidade do campo de entrada de PIN com base na seleção do botão.
     */
    public void onRadioLoginMusicCreatorBtnClick(){
        if(musicCreatorLoginBtn.isSelected()){
            loginPinLbl.setVisible(true);
            loginPinField.setVisible(true);
            loginPanel.revalidate();
            loginPanel.repaint();
        } else if (!musicCreatorLoginBtn.isSelected()) {
            loginPinLbl.setVisible(false);
            loginPinField.setVisible(false);
            loginPanel.revalidate();
            loginPanel.repaint();
        }
    }
    /**
     * Método chamado quando o botão de confirmação de registo é clicado.
     * Regista um novo utilizador com as informações fornecidas, como nome,
     * senha, confirmação de senha, e-mail e, se aplicável, PIN para registro de criador de música.
     */
    public void onRadioRegistMusicCreatorBtnClick(){
        if(musicCreatorRegisBtn.isSelected()){
            registPinLbl.setVisible(true);
            registerPinField.setVisible(true);
            registrationPanel.revalidate();
            registrationPanel.repaint();
        } else if (!musicCreatorRegisBtn.isSelected()) {
            registPinLbl.setVisible(false);
            registerPinField.setVisible(false);
            registrationPanel.revalidate();
            registrationPanel.repaint();
        }
    }
}
