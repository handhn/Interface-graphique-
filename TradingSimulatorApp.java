import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TradingSimulatorApp {
    private JFrame loginFrame;
    private JFrame mainFrame;
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JComboBox<String> symbolComboBox;
    private JComboBox<String> strategyComboBox;
    private JTextArea resultArea;
    private String currentUser;
    private Signal signal;
    private String accessKey;
    private Map<String, List<String>> userSimulationHistory;
    private Map<String, String> users;

    // Couleurs
    private final Color PRIMARY_COLOR = new Color(75, 0, 130);
    private final Color SECONDARY_COLOR = new Color(106, 90, 205);
    private final Color BACKGROUND_COLOR = new Color(240, 248, 255);
    private final Color TEXT_COLOR = new Color(25, 25, 112);

    public TradingSimulatorApp() {
        signal = new Signal();
        accessKey = "e151d80c378a3619067daf2e67d2dade"; // Remplacez par votre clé d'accès
        userSimulationHistory = new HashMap<>();
        users = new HashMap<>();
        createLoginPage();
    }

    private void createLoginPage() {
    loginFrame = new JFrame("Connexion - Simulateur de Trading");
    loginFrame.setSize(450, 350);
    loginFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    loginFrame.setLayout(new GridBagLayout());
    loginFrame.getContentPane().setBackground(BACKGROUND_COLOR);

    GridBagConstraints gbc = new GridBagConstraints();
    gbc.insets = new Insets(15, 15, 15, 15);

    JLabel titleLabel = new JLabel("Connexion");
    titleLabel.setFont(new Font("Arial", Font.BOLD, 32));
    titleLabel.setForeground(PRIMARY_COLOR);
    gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
    loginFrame.add(titleLabel, gbc);

    gbc.gridwidth = 1;
    gbc.anchor = GridBagConstraints.WEST;

    JLabel userLabel = new JLabel("Utilisateur:");
    userLabel.setForeground(TEXT_COLOR);
    userLabel.setFont(new Font("Arial", Font.BOLD, 16));
    gbc.gridx = 0; gbc.gridy = 1;
    loginFrame.add(userLabel, gbc);

    usernameField = new JTextField(20);
    usernameField.setFont(new Font("Arial", Font.PLAIN, 16));
    gbc.gridx = 1; gbc.gridy = 1;
    loginFrame.add(usernameField, gbc);

    JLabel passLabel = new JLabel("Mot de passe:");
    passLabel.setForeground(TEXT_COLOR);
    passLabel.setFont(new Font("Arial", Font.BOLD, 16));
    gbc.gridx = 0; gbc.gridy = 2;
    loginFrame.add(passLabel, gbc);

    passwordField = new JPasswordField(20);
    passwordField.setFont(new Font("Arial", Font.PLAIN, 16));
    gbc.gridx = 1; gbc.gridy = 2;
    loginFrame.add(passwordField, gbc);

    JButton loginButton = createStyledButton("Se connecter");
    gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2;
    gbc.fill = GridBagConstraints.HORIZONTAL;
    loginFrame.add(loginButton, gbc);

    JButton registerButton = createStyledButton("S'inscrire");
    gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 2;
    loginFrame.add(registerButton, gbc);

    loginButton.addActionListener(e -> login());
    registerButton.addActionListener(e -> register());

    loginFrame.setLocationRelativeTo(null);
    loginFrame.setVisible(true);
}

	private void login() {
    	String username = usernameField.getText().trim();
    	String password = new String(passwordField.getPassword());
    	if (username.isEmpty() || password.isEmpty()) {
        	JOptionPane.showMessageDialog(loginFrame, "Veuillez remplir tous les champs.", "Erreur", JOptionPane.ERROR_MESSAGE);
    	} else if (users.containsKey(username) && users.get(username).equals(password)) {
        	currentUser = username;
        	loginFrame.dispose();
        	createMainPage();
    	} else {
        	JOptionPane.showMessageDialog(loginFrame, "Nom d'utilisateur ou mot de passe incorrect.", "Erreur", JOptionPane.ERROR_MESSAGE);
    	}
	}

	private void register() {
    	String username = usernameField.getText().trim();
    	String password = new String(passwordField.getPassword());
    	if (username.isEmpty() || password.isEmpty()) {
        	JOptionPane.showMessageDialog(loginFrame, "Veuillez remplir tous les champs.", "Erreur", JOptionPane.ERROR_MESSAGE);
    	} else if (users.containsKey(username)) {
        	JOptionPane.showMessageDialog(loginFrame, "Cet utilisateur existe déjà.", "Erreur", JOptionPane.ERROR_MESSAGE);
    	} else {
        	users.put(username, password);
        	JOptionPane.showMessageDialog(loginFrame, "Inscription réussie. Vous pouvez maintenant vous connecter.", "Succès", JOptionPane.INFORMATION_MESSAGE);
    	}
	}


    private void createMainPage() {
        mainFrame = new JFrame("Simulateur de Trading - " + currentUser);
        mainFrame.setSize(800, 600);
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setLayout(new BorderLayout());
        mainFrame.getContentPane().setBackground(BACKGROUND_COLOR);

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 20));
        topPanel.setBackground(BACKGROUND_COLOR);

        symbolComboBox = new JComboBox<>(new String[]{"AAPL", "GOOGL", "MSFT", "AMZN", "FB", "HDB"});
        strategyComboBox = new JComboBox<>(new String[]{"RSI", "MACD", "Moyennes Mobiles", "Tendance", "Bollinger"});

        styleComboBox(symbolComboBox);
        styleComboBox(strategyComboBox);

        topPanel.add(new JLabel("Symbole:"));
        topPanel.add(symbolComboBox);
        topPanel.add(new JLabel("Stratégie:"));
        topPanel.add(strategyComboBox);

        JButton simulateButton = createStyledButton("Lancer la simulation");
        topPanel.add(simulateButton);

        mainFrame.add(topPanel, BorderLayout.NORTH);

        resultArea = new JTextArea();
        resultArea.setEditable(false);
        resultArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
        resultArea.setBackground(new Color(250, 250, 250));
        resultArea.setForeground(TEXT_COLOR);
        JScrollPane scrollPane = new JScrollPane(resultArea);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        mainFrame.add(scrollPane, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 20));
        bottomPanel.setBackground(BACKGROUND_COLOR);

        JButton historyButton = createStyledButton("Consulter l'historique");
        JButton logoutButton = createStyledButton("Se déconnecter");

        bottomPanel.add(historyButton);
        bottomPanel.add(logoutButton);
        mainFrame.add(bottomPanel, BorderLayout.SOUTH);

        simulateButton.addActionListener(e -> simulate());
        historyButton.addActionListener(e -> showHistory());
        logoutButton.addActionListener(e -> logout());

        mainFrame.setLocationRelativeTo(null);
        mainFrame.setVisible(true);
    }

    private JButton createStyledButton(String text) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                if (getModel().isArmed()) {
                    g.setColor(SECONDARY_COLOR.darker());
                } else {
                    g.setColor(SECONDARY_COLOR);
                }
                g.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                super.paintComponent(g);
            }
        };
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setOpaque(false);

        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setForeground(Color.YELLOW);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setForeground(Color.WHITE);
            }
        });

        return button;
    }

    private void styleComboBox(JComboBox<?> comboBox) {
        comboBox.setBackground(Color.WHITE);
        comboBox.setForeground(TEXT_COLOR);
        comboBox.setFont(new Font("Arial", Font.PLAIN, 14));
    }

    private void simulate() {
    String symbol = (String) symbolComboBox.getSelectedItem();
    String strategy = (String) strategyComboBox.getSelectedItem();
    
    try {
        signal.updateFromMarketstack(accessKey, symbol);
        List<Double> closingPrices = signal.getClosingPrices();

        if (closingPrices.size() < 26) {
            throw new Exception("Pas assez de données pour effectuer l'analyse");
        }

        String result = executeStrategy(strategy, closingPrices);
        
        // Ajout de la date actuelle
        String currentDate = java.time.LocalDateTime.now().format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"));
        
        String simulationResult = "Date : " + currentDate + "\n" +
                                  "Symbole : " + symbol + "\n" +
                                  "Stratégie : " + strategy + "\n" +
                                  "Résultat : " + result + "\n" ;

        
        resultArea.setText(simulationResult);
        userSimulationHistory.computeIfAbsent(currentUser, k -> new ArrayList<>()).add(simulationResult);
    } catch (Exception e) {
        resultArea.setText("Erreur lors de la simulation : " + e.getMessage());
    }
}


    private void showHistory() {
    JFrame historyFrame = new JFrame("Historique des Simulations - " + currentUser);
    historyFrame.setSize(600, 400);
    historyFrame.setLayout(new BorderLayout());
    
    JTextArea historyArea = new JTextArea();
    historyArea.setEditable(false);
    historyArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
    historyArea.setBackground(new Color(250, 250, 250));
    historyArea.setForeground(TEXT_COLOR);
    
    List<String> userHistory = userSimulationHistory.getOrDefault(currentUser, new ArrayList<>());
    if (userHistory.isEmpty()) {
        historyArea.append("Aucun historique de simulation disponible.\n");
    } else {
        for (String simulation : userHistory) {
            historyArea.append(simulation + "\n----------------------------\n");
        }
    }
    
    JScrollPane scrollPane = new JScrollPane(historyArea);
    scrollPane.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
    historyFrame.add(scrollPane, BorderLayout.CENTER);
    
    historyFrame.setLocationRelativeTo(mainFrame);
    historyFrame.setVisible(true);
}

    private void logout() {
        mainFrame.dispose();
        createLoginPage();
    }

    private String executeStrategy(String strategy, List<Double> closingPrices) throws Exception {
        switch (strategy) {
            case "RSI":
                IndicateurTechnique rsiIndicator = IndicateurTechnique.createRSI(closingPrices, 14);
                StrategieRSI strategieRSI = new StrategieRSI(rsiIndicator);
                return strategieRSI.executerAvecPrixCloture(closingPrices);
            case "MACD":
                IndicateurTechnique macdIndicator = IndicateurTechnique.createMACD(closingPrices, 12, 26, 9);
                StrategieMACD strategieMACD = new StrategieMACD(macdIndicator);
                return strategieMACD.executerAvecPrixCloture(closingPrices);
            case "Moyennes Mobiles":
                IndicateurTechnique smaIndicator = IndicateurTechnique.createSMA(closingPrices, 20);
                StrategieMoyennesMobiles strategieMoyennesMobiles = new StrategieMoyennesMobiles(smaIndicator);
                return strategieMoyennesMobiles.executerAvecPrixCloture(closingPrices);
            case "Tendance":
                IndicateurTechnique emaIndicator = IndicateurTechnique.createEMA(closingPrices, 20);
                StrategieTendance strategieTendance = new StrategieTendance(emaIndicator);
                return strategieTendance.executerAvecPrixCloture(closingPrices);
            case "Bollinger":
                IndicateurTechnique bollingerBandsIndicator = IndicateurTechnique.createBollingerBands(closingPrices, 20, 2.0);
                StrategieBollinger strategieBollinger = new StrategieBollinger(bollingerBandsIndicator);
                return strategieBollinger.executerAvecPrixCloture(closingPrices);
            default:
                throw new IllegalArgumentException("Stratégie non reconnue");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }
            new TradingSimulatorApp();
        });
    }
}
