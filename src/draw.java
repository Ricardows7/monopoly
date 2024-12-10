import javafx.animation.PauseTransition;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.*;
import javafx.scene.layout.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.scene.paint.*;
import javafx.scene.shape.Rectangle;


public class draw extends Application {
    private double boardSize;
    private static double stepSize;

    private Label[] moneyLabels;

    private static ImageView players[];

    public void start(Stage primaryStage) {
        int numP = 0, op = 0;
        op = menu(primaryStage);
        if (op == 0)
            return;
        else if (op == 1)
            numP = showPlayerSelectionDialog(primaryStage);
        //else if (op == 2)
            //Logica p/ continuar um save
        
        //run game = new run();
        initializer start = new initializer();
        monopoly.board tabuleiro = start.startBoard(numP,40);
        moneyLabels = new Label[tabuleiro.getPlayers()];
        players = new ImageView[tabuleiro.getPlayers()];

        StackPane root = createGameLayout(primaryStage, tabuleiro); 
        Scene scene = new Scene(root);
        
        diceUI(root, tabuleiro.getDie());
        //game.startGameLoop(numP,tabuleiro,scene,primaryStage);   //AQUI TA DANDO ERRO!!!

        // background da tela
        BackgroundFill fill = new BackgroundFill(Color.web("#FFEE8C90"), null, null);
        root.setBackground(new Background(fill));

        // titulos e mostra tela
        primaryStage.setTitle("Monopoly");
        primaryStage.setScene(scene);
        primaryStage.setMaximized(true);
        primaryStage.setResizable(false);
        primaryStage.show();
        
    }

    private int menu(Stage primaryStage) {
        // Criando os botões
        Button startButton = new Button("Iniciar Jogo");
        Button continueButton = new Button("Continuar");
        Button quitButton = new Button("Sair");
    
        // Variável para armazenar o valor selecionado
        final int[] value = {0};
    
        // Configurando a janela modal
        Stage menuStage = new Stage();
        menuStage.initOwner(primaryStage);
        menuStage.setTitle("Menu");
        menuStage.setResizable(false);
    
        // Layout dos botões
        VBox layout = new VBox(10, startButton, continueButton, quitButton);
        layout.setStyle("-fx-padding: 10; -fx-alignment: center;");
    
        // Configurando ações dos botões
        startButton.setOnAction(e -> {
            value[0] = showPlayerSelectionDialog(primaryStage);;
            menuStage.close(); // Fecha o menu
        });
    
        continueButton.setOnAction(e -> {
            value[0] = -1;
            menuStage.close(); // Fecha o menu
        });
    
        quitButton.setOnAction(e -> {
            value[0] = 0;
            menuStage.close(); // Fecha o menu
        });
    
        // Mostrando a janela do menu
        menuStage.setScene(new Scene(layout, 200, 150));
        menuStage.showAndWait(); // Pausa a execução até que o menu seja fechado
    
        // Retornando o valor selecionado
        return value[0];
    }    

    // Carrega imagens e cria setup da janela do jogo
    private StackPane createGameLayout(Stage primaryStage, monopoly.board brd) {
        imageManager.loadEssentialImages();
        ImageView boardViewer = createBoardViewer(primaryStage);
        StackPane root = new StackPane(boardViewer);
        AnchorPane UI = createUI(brd);
        Pane paneAux = new Pane();

        this.boardSize = boardViewer.getImage().getWidth();
        stepSize = boardSize / 23;

        // Pane pra posicionamento de players
        root.getChildren().add(paneAux);

        paneAux.prefWidthProperty().bind(boardViewer.fitWidthProperty());
        paneAux.prefHeightProperty().bind(boardViewer.fitHeightProperty());

        double xStartPercent = 0.225;
        double yStartPercent = 0.85;
        double xSpacingPercent = 0.02;

        for (int i = 0; i < brd.getPlayers(); i++) {
            players[i] = createPlayerViewer(boardViewer, i);

            players[i].layoutXProperty().bind(paneAux.widthProperty().multiply(xStartPercent + (i * xSpacingPercent)));
            players[i].layoutYProperty().bind(paneAux.heightProperty().multiply(yStartPercent));

            paneAux.getChildren().add(players[i]);
        }
        root.getChildren().addAll(UI);
        return root;
    }

    private ImageView createBoardViewer(Stage primaryStage) {
        ImageView boardViewer = new ImageView(imageManager.getImage("board"));
        boardViewer.setPreserveRatio(true);
        boardViewer.fitHeightProperty().bind(primaryStage.heightProperty());
        return boardViewer;
    }

    private ImageView createPlayerViewer(ImageView boardViewer, int id) {
        String c = String.valueOf(id + 1);
        System.out.println("player" + c);
        ImageView playerViewer = new ImageView(imageManager.getImage("player" + c));
        playerViewer.setPreserveRatio(true);
        playerViewer.fitWidthProperty().bind(boardViewer.fitWidthProperty().multiply(0.05));
        playerViewer.fitHeightProperty().bind(boardViewer.fitHeightProperty().multiply(0.05));

        return playerViewer;
    }

    private AnchorPane createUI(monopoly.board brd) {
        AnchorPane uiPane = new AnchorPane();
        int playerCount = brd.getPlayers();

        for (int i = 0; i < playerCount; i++) {
            moneyLabels[i] = new Label("Player " + (i + 1) + "R$" + brd.getPlayer(i).getWallet());
            moneyLabels[i].setStyle("-fx-font-size: 16px; -fx-background-color: #ffffff; -fx-padding: 5px;");
        }

        if (playerCount >= 1) {
            AnchorPane.setTopAnchor(moneyLabels[0], 10.0); // Top-left
            AnchorPane.setLeftAnchor(moneyLabels[0], 10.0);
        }
        if (playerCount >= 2) {
            AnchorPane.setTopAnchor(moneyLabels[1], 10.0); // Top-right
            AnchorPane.setRightAnchor(moneyLabels[1], 10.0);
        }
        if (playerCount >= 3) {
            AnchorPane.setBottomAnchor(moneyLabels[2], 10.0); // Bottom-left
            AnchorPane.setLeftAnchor(moneyLabels[2], 10.0);
        }
        if (playerCount >= 4) {
            AnchorPane.setBottomAnchor(moneyLabels[3], 10.0); // Bottom-right
            AnchorPane.setRightAnchor(moneyLabels[3], 10.0);
        }

        uiPane.getChildren().addAll(moneyLabels);

        return uiPane;

    }
    public void textUI(StackPane root, String message) {
        // Retangulo de bg
        Rectangle backgroundRect = createRectangle(500, 300);
    
        Label textLabel = new Label(message);
        textLabel.setStyle("-fx-font-size: 18px;");
        textLabel.setWrapText(true);
        textLabel.setMaxWidth(350);
        textLabel.setAlignment(Pos.CENTER);
    
        Button closeButton = new Button("Close");
        closeButton.setStyle("-fx-font-size: 14px;");
    
        VBox contentBox = new VBox(20);  
        contentBox.setAlignment(Pos.CENTER);
        contentBox.getChildren().addAll(textLabel, closeButton);
    
        StackPane textBoxPane = new StackPane();
        textBoxPane.getChildren().addAll(backgroundRect, contentBox);
    
        closeButton.setOnAction(e -> root.getChildren().remove(textBoxPane));
    
        root.getChildren().add(textBoxPane);
    }

    public void diceUI(StackPane root, dice die){
        Button rollButton = new Button("Roll dice");
        VBox button = new VBox();
        
        button.setAlignment(Pos.CENTER);
        button.getChildren().add(rollButton);
        root.getChildren().addAll(button);
        rollButton.setOnAction(e -> {
            die.throwDie();
            root.getChildren().remove(button);
            
            String base = "dice_";
            ImageView dice1 = new ImageView(imageManager.getImage(base + String.valueOf(die.checkValue1())));
            ImageView dice2 = new ImageView(imageManager.getImage(base + String.valueOf(die.checkValue2())));
            dice1.setPreserveRatio(true);
            dice2.setPreserveRatio(true);
            dice1.setFitWidth(75); 
            dice2.setFitWidth(75);
    
            HBox diceBox = new HBox(10); // 10 de spacing
            diceBox.getChildren().addAll(dice1, dice2);
            diceBox.setAlignment(Pos.CENTER);
    
            root.getChildren().addAll(diceBox);
            PauseTransition pause = new PauseTransition(Duration.seconds(2));
            pause.setOnFinished(event -> root.getChildren().remove(diceBox));
            pause.play();        
        });
    }
    
    public void propertyUI(StackPane root, property prop, player player) {
        ImageView[] icons = new ImageView[5];
        String base = "level_";
        for(int i = 0; i < 5; i++)
            icons[i] = new ImageView(imageManager.getImage(base + String.valueOf(i)));

        Rectangle backgroundRect = createRectangle(500, 300);
        
        Label propertyPriceLabel = new Label("Property Cost: $" + prop.getValue());
        propertyPriceLabel.setStyle("-fx-font-size: 16px;");
    
        // Upgrade buttons
        Button[] upgradeButtons = new Button[5];
        String[] upgradeLevels = {"Don't Buy", "Level 1", "Level 2", "Level 3", "Level 4"};
        
        VBox buttonBox = new VBox(10);
        buttonBox.setAlignment(Pos.CENTER);
    
        for (int i = 0; i < upgradeLevels.length; i++) {
            Button button = new Button(upgradeLevels[i]);
            button.setStyle("-fx-font-size: 14px; -fx-min-width: 200px;");
            button.setGraphic(icons[i]);
            // Disable button logic based on property state and player's money
            if (i > 0) {
                int upgradeCost = prop.getUpgradeValue();
                button.setText(upgradeLevels[i] + " (Cost: R$" + upgradeCost + ")");
                
                // Precisa chechar se player pode comprar
                if (!player.canAfford(upgradeCost) || !prop.isUpgradeValid()) {   //PRA QUE ESSA LOGICA DO UPGRADE VALID? O IMPROVE JA VERIFICA SE PODE MELHORAR!
                    button.setDisable(true);
                    button.setStyle("-fx-font-size: 14px; -fx-min-width: 200px; -fx-opacity: 0.5;");
                    icons[i].setOpacity(0.5);
                }
            }
    
            int finalI = i;
            button.setOnAction(e -> {
                if (finalI == 0) {
                    root.getChildren().remove(buttonBox);
                } else {
                    prop.improve(player.getWallet());
                    root.getChildren().remove(buttonBox);
                }
            });
    
            upgradeButtons[i] = button;
            buttonBox.getChildren().add(button);
        }
    
        VBox contentBox = new VBox(15);
        contentBox.setAlignment(Pos.CENTER);
        contentBox.getChildren().addAll(propertyPriceLabel, buttonBox);
    
        StackPane textBoxPane = new StackPane();
        textBoxPane.getChildren().addAll(backgroundRect, contentBox);
    
        root.getChildren().add(textBoxPane);
    }

    private int showPlayerSelectionDialog(Stage primaryStage) {
        Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.initOwner(primaryStage);
        dialog.setTitle("Select Players");

        VBox dialogLayout = new VBox(10);
        dialogLayout.setStyle("-fx-padding: 10; -fx-alignment: center;");

        Label promptLabel = new Label("Enter number of players (1-6):");
        TextField playerInput = new TextField();
        Button confirmButton = new Button("Confirm");
        Label errorLabel = new Label();
        errorLabel.setStyle("-fx-text-fill: red;");

        confirmButton.setOnAction(e -> {
            try {
                int players = Integer.parseInt(playerInput.getText());
                if (players >= 1 && players <= 6) {
                    dialog.setUserData(players);
                    dialog.close();
                } else {
                    errorLabel.setText("Please enter a number between 1 and 6.");
                }
            } catch (NumberFormatException ex) {
                errorLabel.setText("Invalid input. Please enter a number.");
            }
        });

        dialogLayout.getChildren().addAll(promptLabel, playerInput, confirmButton, errorLabel);

        Scene dialogScene = new Scene(dialogLayout, 250, 150);
        dialog.setScene(dialogScene);
        dialog.showAndWait();

        Object userData = dialog.getUserData();
        return userData != null ? (int) userData : 0;
    }

    // Move player movement vezes
    public static void movePlayer(player player, int movement) {
        while (movement > 0) {
            int pos = player.getPosition();

            if (pos < 10)
                players[player.getId()].setTranslateY(players[player.getId()].getTranslateY() - stepSize);
            else if (pos < 20)
                players[player.getId()].setTranslateX(players[player.getId()].getTranslateX() + stepSize);
            else if (pos < 30)
                players[player.getId()].setTranslateY(players[player.getId()].getTranslateY() + stepSize);
            else if (pos < 40)
                players[player.getId()].setTranslateX(players[player.getId()].getTranslateX() - stepSize);

            movement--;
        }
        return;
    }

    private Rectangle createRectangle(double X, double Y){
        Rectangle backgroundRect = new Rectangle(X,Y);

        backgroundRect.setFill(Color.BLACK);
        backgroundRect.setArcWidth(20); 
        backgroundRect.setArcHeight(20);
        backgroundRect.setStroke(Color.GRAY);
        backgroundRect.setStrokeWidth(2);
        return backgroundRect;
    }

    private Button manageButton(String name, boolean on) {
        Button specialButton = new Button(name);
        buttonSwitch(specialButton, on);

        HBox gameLayout = new HBox();
        gameLayout.getChildren().add(specialButton);

        return specialButton;
    }

    public void buttonSwitch(Button specialButton, boolean on) {
        if (on) {
            specialButton.setDisable(false);
            specialButton.setOpacity(1);
        } else {
            specialButton.setDisable(true);
            specialButton.setOpacity(0.5);
        }
    }
    public static void main(String[] args) {
        launch(args);
    }
}
