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

        
