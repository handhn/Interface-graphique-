import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class LoginPage extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton, registerButton;

    public LoginPage() {
        setTitle("Authentification");
        setSize(300, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(3, 2));

        add(new JLabel("Nom d'utilisateur:"));
        usernameField = new JTextField();
        add(usernameField);

        add(new JLabel("Mot de passe:"));
        passwordField = new JPasswordField();
        add(passwordField);

        loginButton = new JButton("Connexion");
        registerButton = new JButton("Inscription");
        add(loginButton);
        add(registerButton);

        loginButton.addActionListener(e -> login());
        registerButton.addActionListener(e -> register());

        setVisible(true);
    }

    private void login() {
        // Vérifier les identifiants et ouvrir la page principale si correct
    }

    private void register() {
        // Enregistrer le nouvel utilisateur
    }
}
