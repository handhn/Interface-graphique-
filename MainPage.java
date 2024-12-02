import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

public class MainPage {
    private JFrame frame;
    private JComboBox<String> symbolComboBox;
    private JComboBox<String> strategyComboBox;
    private JButton simulateButton, historyButton, exitButton;
    private JTextArea resultArea;
    private List<String> simulationHistory;
    private Signal signal;
    private String accessKey;

    public MainPage() {
        frame = new JFrame("Simulateur de Trading Algorithmique");
        frame.setSize(500, 400);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        simulationHistory = new ArrayList<>();
        signal = new Signal();
        accessKey = "b7f506784eb6b5922bf44ecc26a22708"; // Assurez-vous d'utiliser votre propre clé d'accès

        // Panel pour les sélections
        JPanel selectionPanel = new JPanel(new GridLayout(3, 2, 10, 10));
        selectionPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        selectionPanel.add(new JLabel("Symbole d'action:"));
        symbolComboBox = new JComboBox<>(new String[]{"AAPL", "GOOGL", "MSFT", "AMZN", "FB", "HDB"});
        selectionPanel.add(symbolComboBox);

        selectionPanel.add(new JLabel("Stratégie:"));
        strategyComboBox = new JComboBox<>(new String[]{"RSI", "MACD", "Moyennes Mobiles", "Tendance", "Bollinger"});
        selectionPanel.add(strategyComboBox);

        simulateButton = new JButton("Lancer la simulation");
        simulateButton.setBackground(new Color(230, 0, 0)); // Couleur bleu clair
        simulateButton.setForeground(Color.WHITE);
        selectionPanel.add(simulateButton);

        frame.add(selectionPanel, BorderLayout.NORTH);

        // Zone de résultat
        resultArea = new JTextArea();
        resultArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(resultArea);
        frame.add(scrollPane, BorderLayout.CENTER);

        // Panel pour les boutons du bas
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        historyButton = new JButton("Historique");
        exitButton = new JButton("Quitter");
        bottomPanel.add(historyButton);
        bottomPanel.add(exitButton);
        frame.add(bottomPanel, BorderLayout.SOUTH);

        // Ajout des listeners
        simulateButton.addActionListener(e -> simulate());
        historyButton.addActionListener(e -> showHistory());
        exitButton.addActionListener(e -> System.exit(0));

        frame.setVisible(true);
    }

    private void simulate() {
        String symbol = (String) symbolComboBox.getSelectedItem();
        String strategy = (String) strategyComboBox.getSelectedItem();
        
        try {
            // Récupérer les données de l'API
            signal.updateFromMarketstack(accessKey, symbol);
            List<Double> closingPrices = signal.getClosingPrices();

            if (closingPrices.size() < 26) {
                throw new Exception("Pas assez de données pour effectuer l'analyse");
            }

            String result = executeStrategy(strategy, closingPrices);
            
            resultArea.setText("Résultat de la simulation pour " + symbol + " avec la stratégie " + strategy + ":\n" + result);
            simulationHistory.add("Simulation: " + symbol + " - " + strategy + " - Résultat: " + result);
        } catch (Exception e) {
            resultArea.setText("Erreur lors de la simulation : " + e.getMessage());
        }
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

    private void showHistory() {
        StringBuilder history = new StringBuilder("Historique des simulations:\n\n");
        for (String simulation : simulationHistory) {
            history.append(simulation).append("\n");
        }
        JOptionPane.showMessageDialog(frame, history.toString(), "Historique", JOptionPane.INFORMATION_MESSAGE);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(MainPage::new);
    }
}
