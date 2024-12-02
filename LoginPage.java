import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class LoginPage {
    private JFrame frame;
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton, registerButton;

    public LoginPage() {
        frame = new JFrame("Authentification");
        frame.setSize(400, 250);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new GridBagLayout());
        frame.getContentPane().setBackground(new Color(240, 240, 240)); // Couleur de fond

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10); // Espacement autour des composants

        // Titre
        JLabel titleLabel = new JLabel("Bienvenue !");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(new Color(50, 50, 50));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2; // Prendre deux colonnes
        frame.add(titleLabel, gbc);

        // Nom d'utilisateur
        gbc.gridwidth = 1; // Réinitialiser à une colonne
        gbc.gridx = 0;
        gbc.gridy = 1;
        frame.add(new JLabel("Nom d'utilisateur:"), gbc);

        usernameField = new JTextField(15);
        gbc.gridx = 1;
        frame.add(usernameField, gbc);

        // Mot de passe
        gbc.gridx = 0;
        gbc.gridy = 2;
        frame.add(new JLabel("Mot de passe:"), gbc);

        passwordField = new JPasswordField(15);
        gbc.gridx = 1;
        frame.add(passwordField, gbc);

        // Boutons
        loginButton = new JButton("Connexion");
        registerButton = new JButton("Inscription");
        
        // Styliser les boutons
        loginButton.setBackground(new Color(95, 0, 200)); // Couleur de fond du bouton
        loginButton.setForeground(Color.WHITE); // Couleur du texte
        registerButton.setBackground(new Color(230, 0, 0));
        registerButton.setForeground(Color.WHITE);
        
        loginButton.addActionListener(e -> login());
        registerButton.addActionListener(e -> register());

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(loginButton);
        buttonPanel.add(registerButton);
        
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2; // Prendre deux colonnes
        frame.add(buttonPanel, gbc);

        frame.setVisible(true);
    }

    private void login() {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());
        
        JOptionPane.showMessageDialog(frame, "Tentative de connexion avec:\nUtilisateur: " + username + "\nMot de passe: " + password);
    }

    private void register() {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());
        
        JOptionPane.showMessageDialog(frame, "Tentative d'inscription avec:\nUtilisateur: " + username + "\nMot de passe: " + password);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(LoginPage::new);
    }
}
