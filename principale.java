public class MainPage extends JFrame {
    private JComboBox<String> symbolComboBox;
    private JComboBox<String> strategyComboBox;
    private JButton simulateButton, historyButton, exitButton;
    private JTextArea resultArea;

    public MainPage() {
        setTitle("Simulation de trading");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(5, 1));

        symbolComboBox = new JComboBox<>(new String[]{"AAPL", "GOOGL", "MSFT"}); // Ajouter plus de symboles
        add(symbolComboBox);

        strategyComboBox = new JComboBox<>(new String[]{"RSI", "MACD", "Moyennes Mobiles", "Tendance", "Bollinger"});
        add(strategyComboBox);

        simulateButton = new JButton("Lancer la simulation");
        add(simulateButton);

        resultArea = new JTextArea();
        resultArea.setEditable(false);
        add(new JScrollPane(resultArea));

        JPanel buttonPanel = new JPanel();
        historyButton = new JButton("Historique");
        exitButton = new JButton("Quitter");
        buttonPanel.add(historyButton);
        buttonPanel.add(exitButton);
        add(buttonPanel);

        simulateButton.addActionListener(e -> simulate());
        historyButton.addActionListener(e -> showHistory());
        exitButton.addActionListener(e -> System.exit(0));

        setVisible(true);
    }

    private void simulate() {
        String symbol = (String) symbolComboBox.getSelectedItem();
        String strategy = (String) strategyComboBox.getSelectedItem();
        // Lancer la simulation et afficher le résultat
        String result = "ACHAT"; // ou "VENTE" ou "ATTENTE"
        resultArea.setText("Résultat pour " + symbol + " avec " + strategy + ": " + result);
    }

    private void showHistory() {
        // Afficher l'historique des simulations
    }
}
