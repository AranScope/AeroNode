package uicomponents;

import telemetrics.Db;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;

/**
 * Login Interface Class
 * @author Aran Long
 *
 * Username, Login and Email (hidden) text fields are included. When text fields are
 * focused their background will lighten to give an indication to the user. When
 * selecting the signup option the email text field will be set to visible and the User
 * sign up method will be called rather than the User log in method called as standard.
 *
 */

public class Login extends JPanel{
    private JTextField userField;
    private JPasswordField passwordField;
    private JPanel loginPanel;
    private JButton loginButton;
    private JButton signUpButton;
    private JProgressBar progressBar1;
    private JTextField emailField;
    private JButton backButton;
    private JLabel errorLabel;
    private JLabel imageLabel;
    public static JFrame frame = new JFrame("Login");
    public void setValue(int value) {
        progressBar1.setValue(value);
    }
    public static boolean loggedIn = false;

    Db db = new Db();

    /**
     * Returns current login state of User.
     *
     * @return
     */
    public Boolean isLoggedIn(){
        return loggedIn;
    }

    /**
     * Class constructor, Sets all UI defaults for Buttons, Fields, Labels of Login JPanel (Opacities, Locations, Colors, Borders).
     * Sets JFrame UI defaults (Size, Location, Close Operation, Decoration, Visibility).
     * Adds all Graphical Components to loginPanel JPanel.
     * Adds focus listeners to buttons and text fields to enable color change on hover.
     * Adds text field listeners to verify data entered by user with help of Db class (Accessing data from User table in database).
     */
    public Login() {
        imageLabel = new JLabel();
        try {
            imageLabel.setIcon(new ImageIcon("res/aerologo.png"));
            frame.setIconImage(ImageIO.read(new File("res/aeroicon.png")));
        }catch(Exception e){
            System.out.println("Login: Icon image aerologo.png or aeroicon.png not found");
        }

        frame.setPreferredSize(new Dimension(600, 400));
        frame.setLocation((int) Toolkit.getDefaultToolkit().getScreenSize().getWidth() / 2 - 300, (int) Toolkit.getDefaultToolkit().getScreenSize().getHeight() / 2 - 200);
        frame.getContentPane().setBackground(Ui.frameBackground);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setUndecorated(true);
        frame.setVisible(true);

        frame.add(loginPanel);
        frame.pack();
        frame.add(imageLabel);

        loginPanel.setOpaque(false);
        userField.setBackground(Ui.widgetBackground);
        userField.setForeground(Color.decode("0xD0CED6"));
        emailField.setBackground(Ui.widgetBackground);
        emailField.setForeground(Color.decode("0xD0CED6"));
        passwordField.setBackground(Ui.widgetBackground);
        passwordField.setForeground(Color.decode("0xD0CED6"));
        backButton.setBackground(Ui.widgetBackground);
        backButton.setForeground(Color.decode("0xD0CED6"));
        loginButton.setBackground(Ui.widgetBackground);
        loginButton.setForeground(Color.decode("0xD0CED6"));
        signUpButton.setBackground(Ui.widgetBackground);
        signUpButton.setForeground(Color.decode("0xD0CED6"));
        progressBar1.setBackground(Ui.widgetBackground);
        progressBar1.setForeground(Color.decode("0xD0CED6"));
        errorLabel.setForeground(Color.decode("0xD0CED6"));

        progressBar1.setBorderPainted(false);
        userField.setBorder(BorderFactory.createEmptyBorder());
        passwordField.setBorder(BorderFactory.createEmptyBorder());
        emailField.setBorder(BorderFactory.createEmptyBorder());
        backButton.setBorder(BorderFactory.createEmptyBorder());
        loginButton.setBorder(BorderFactory.createEmptyBorder());
        signUpButton.setBorder(BorderFactory.createEmptyBorder());

        userField.setCaretColor(Color.decode("0xD0CED6"));
        passwordField.setCaretColor(Color.decode("0xD0CED6"));

        emailField.setVisible(false);
        backButton.setVisible(false);
        errorLabel.setVisible(true);

        userField.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                userField.setBackground(Ui.widgetForegroundGray);
                if(userField.getText().equals("User ID")){
                    userField.setText("");
                }
                else if(userField.getText().equals("")){
                    userField.setText("User ID");
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                userField.setBackground(Ui.widgetBackground);
                if(userField.getText().equals("")){
                    userField.setText("User ID");
                }
            }
        });
        passwordField.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                passwordField.setBackground(Ui.widgetForegroundGray);
                if(passwordField.getText().equals("Password")){
                    passwordField.setText("");
                }
                else if(passwordField.getText().equals("")){
                    passwordField.setText("Password");
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                passwordField.setBackground(Ui.widgetBackground);
                if(passwordField.getText().equals("")){
                    passwordField.setText("Password");
                }
            }
        });
        emailField.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                emailField.setBackground(Ui.widgetForegroundGray);
                if(emailField.getText().equals("Email")){
                    emailField.setText("");
                }
                else if(emailField.getText().equals("")){
                    emailField.setText("Email");
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                emailField.setBackground(Ui.widgetBackground);
                if(emailField.getText().equals("")){
                    emailField.setText("Email");
                }
            }
        });
        loginButton.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                loginButton.setBackground(Ui.widgetForegroundGray);
            }

            public void mouseExited(MouseEvent e) {
                loginButton.setBackground(Ui.widgetBackground);
            }

            public void mouseClicked(MouseEvent e) {
                String username = userField.getText();
                String password = passwordField.getText();
                //loggedIn = db.loginUser(username, password);
                loggedIn = true;


                if (loggedIn) {
                    errorLabel.setText("Logged in");
                } else {
                    errorLabel.setText("Failed to login");
                    System.out.println("Failed to login");
                }
            }
        });
        signUpButton.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                signUpButton.setBackground(Ui.widgetForegroundGray);
            }
            public void mouseExited(MouseEvent e) {
                signUpButton.setBackground(Ui.widgetBackground);
            }
            public void mouseClicked(MouseEvent e) {
                if(emailField.isVisible()){
                    try {
                        if(emailField.getText().matches("([A-z]+[.])*[A-z]+[@][A-z]+.[A-z]+([.]?[A-z]+)*")) {
                            if (db.signUpUser(userField.getText(), passwordField.getText(), emailField.getText())) {
                                errorLabel.setText("Account created");
                                loggedIn = true;
                            }
                            else{
                                errorLabel.setText("Invalid account data");
                            }
                        }
                        else{
                            errorLabel.setText("Invalid account data");
                        }
                    }catch(Exception ex){
                        System.out.println("Login: error signing up user\n" + ex.toString());
                    }
                }
                emailField.setVisible(true);
                backButton.setVisible(true);
                loginButton.setVisible(false);
                emailField.requestFocus();
            }
        });
        backButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                emailField.setVisible(false);
                backButton.setVisible(false);
                loginButton.setVisible(true);

            }

            @Override
            public void mouseEntered(MouseEvent e) {
                backButton.setBackground(Ui.widgetForegroundGray);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                backButton.setBackground(Ui.widgetBackground);
            }
        });



        frame.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                super.mouseDragged(e);
                frame.setLocation(e.getLocationOnScreen());
            }
        });

        passwordField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if(e.getKeyCode() == KeyEvent.VK_UP){
                    userField.requestFocus();
                }
            }
        });

        userField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if(e.getKeyCode() == KeyEvent.VK_DOWN){
                    passwordField.requestFocus();
                }
                else if(e.getKeyCode() == KeyEvent.VK_UP && emailField.isVisible()){
                    emailField.requestFocus();
                }
            }
        });
        emailField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if(e.getKeyCode() == KeyEvent.VK_DOWN){
                    userField.requestFocus();
                }
            }
        });

        loginPanel.setPreferredSize(new Dimension(800, 600));
        progressBar1.setMaximum(1000);
        progressBar1.setMinimum(0);
    }
}