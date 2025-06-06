import java.util.Stack;

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
    private StackPane root;

    public StackPane getRoot(){
        return root;
    }

    public void start(Stage primaryStage) {
        int numP = 0;
        numP = menu(primaryStage);
        save loadGame = new save();
        if (numP == 0)
            return;
        else if (numP == -1) {
            if (loadGame.fileExists()) {
                numP = loadGame.getPlayers();
                initializer start = new initializer();
                monopoly.board tabuleiro = start.startBoard(numP,40);
                loadGame.getSave(tabuleiro.getGamers(),tabuleiro);
            }
        }
            
        
        run game = new run();
        initializer start = new initializer();
        monopoly.board tabuleiro = start.startBoard(numP,40);
        moneyLabels = new Label[monopoly.board.getPlayers()];
        players = new ImageView[monopoly.board.getPlayers()];

        root = createGameLayout(primaryStage, tabuleiro); 
        Scene scene = new Scene(root);
                
        game.startGameLoop(numP,tabuleiro,scene,primaryStage);   //AQUI TA DANDO ERRO!!!
        // background da tela
        BackgroundFill fill = new BackgroundFill(Color.web("#FFEE8C90"), null, null);
        root.setBackground(new Background(fill));

        // titulos e mostra tela
        primaryStage.setTitle("Monopoly");
        primaryStage.setScene(scene);
        primaryStage.setMaximized(true);
        primaryStage.show();
        
    }

    private int menu(Stage primaryStage) {
        // Criando os botões
        Button startButton = new Button("Start Game");
        Button continueButton = new Button("Continue");
        Button quitButton = new Button("Quit");
    
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
        players = new ImageView[monopoly.board.getPlayers()];

        this.boardSize = boardViewer.getImage().getWidth();
        stepSize = boardSize / 23;

        // Pane pra posicionamento de players
        root.getChildren().add(paneAux);

        paneAux.prefWidthProperty().bind(boardViewer.fitWidthProperty());
        paneAux.prefHeightProperty().bind(boardViewer.fitHeightProperty());

        double xStartPercent = 0.225;
        double yStartPercent = 0.85;
        double xSpacingPercent = 0.02;

        for (int i = 0; i < monopoly.board.getPlayers(); i++) {
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
        int playerCount = monopoly.board.getPlayers();

        for (int i = 0; i < playerCount; i++) {
            moneyLabels[i] = new Label("Player " + (i + 1) + " R$" + monopoly.board.getPlayer(i).getWallet().Check());
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
        button.setPrefSize(200, 150);
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
            PauseTransition pause = new PauseTransition(Duration.seconds(4));
            pause.setOnFinished(event -> root.getChildren().remove(diceBox));
            pause.play();        
        });
    }
    
    public void propertyUI(StackPane root, property prop, player player, bank comp, portfolio receiver, portfolio giver,
            wallet owner, wallet buyer, int buyerId, squares place) 
        {
        
        Label propertyPriceLabel = new Label("Property Cost: $" + prop.getValue());
        propertyPriceLabel.setStyle("-fx-font-size: 16px;");
    
        // Upgrade buttons
        Button[] buyButtons = new Button[4];
        
        HBox buttonBox = new HBox(10);
        buttonBox.setAlignment(Pos.CENTER);
        
        int state = prop.getState();
        
        int upgradeCost = prop.getUpgradeValue();
        int valueCost = prop.getValue();
        int mortgageCost = prop.getMortgageValue();
        
        buyButtons[0].setText("Pass turn!");
        buyButtons[1].setText("Buy land for: " + valueCost + "R$");
        buyButtons[2].setText("Improve property to " + state + " for : " + upgradeCost + "R$");
        buyButtons[3].setText(
                "Mortgage this property now and receive: " + mortgageCost + "R$ and just pay after 5 rounds!");
        
        buttonBox.getChildren().addAll(buyButtons);
        root.getChildren().add(buttonBox);

        // Update button states based on property ownership
        if (comp.getOwner(prop.getPosition()) == player.getId()) { // Player owns the property
            buyButtons[1].setDisable(true); // Disable "Buy land"
            buyButtons[1].setOpacity(0.5);
            if (!prop.isMortgaged()) {
                buyButtons[3].setDisable(true); // Disable "Mortgage"
                buyButtons[3].setOpacity(0.5);
            }
            if (!player.canAfford(upgradeCost) || !prop.isUpgradeValid()) { // Check if upgrade is valid
                buyButtons[2].setDisable(true); // Disable "Improve property"
                buyButtons[2].setOpacity(0.5);
            }
        }else { // Player does not own the property
            buyButtons[2].setDisable(true); // Disable "Improve property"
            buyButtons[2].setOpacity(0.5);
            buyButtons[3].setDisable(true); // Disable "Mortgage"
            buyButtons[3].setOpacity(0.5);
                        
            if (player.Check() < prop.getValue()) { // Check if player can afford the property
                buyButtons[1].setDisable(true); // Disable "Buy land"
            }
        }
                        
        // Event Handlers
        buyButtons[1].setOnAction(e -> {
            if (comp.getOwner(prop.getPosition()) == 0) {
                comp.sellProperties(receiver, buyer, player.getId(), place, true);
            } else {
                comp.sellProperties(receiver, giver, owner, buyer, buyerId, place, true);
            }
        });
                        
        buyButtons[2].setOnAction(e -> prop.improve(buyer));
                        
        buyButtons[3].setOnAction(e -> prop.getMortgage(buyer));
                        
        buyButtons[0].setOnAction(e -> root.getChildren().remove(buttonBox)); // Update quit state when "Pass turn" is clicked
    
    }

    public void stocksUI(StackPane root, stocks stcks, bank comp, player player, portfolio receiver, portfolio giver,
    wallet owner, wallet buyer, squares place){
    
        Label textLabel = new Label("Buy stocks?");
        textLabel.setStyle("-fx-font-size: 18px;");
        textLabel.setWrapText(true);
        textLabel.setMaxWidth(350);
        textLabel.setAlignment(Pos.CENTER);
    
        Button closeButton = new Button("Close");
        closeButton.setStyle("-fx-font-size: 14px;");
        Button buyButton = new Button("Buy");
        closeButton.setStyle("-fx-font-size: 14px;");
    
        VBox contentBox = new VBox(20);  
        contentBox.setAlignment(Pos.CENTER);
        contentBox.getChildren().addAll(textLabel, buyButton, closeButton);
        root.getChildren().add(contentBox);
    
        closeButton.setOnAction(e -> root.getChildren().remove(contentBox));
       
        buyButton.setOnAction(e -> {
            if (comp.getOwner(place.getPosition()) != 0)
                comp.sellProperties(receiver, giver, owner, buyer, player.getId(), place, true);
            else
                comp.sellProperties(receiver, buyer, player.getId(), place, true);
        });
    }

    private int showPlayerSelectionDialog(Stage primaryStage) {
        Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.initOwner(primaryStage);
        dialog.setTitle("Select Players");

        VBox dialogLayout = new VBox(10);
        dialogLayout.setStyle("-fx-padding: 10; -fx-alignment: center;");

        Label promptLabel = new Label("Enter number of players (1-4):");
        TextField playerInput = new TextField();
        Button confirmButton = new Button("Confirm");
        Label errorLabel = new Label();
        errorLabel.setStyle("-fx-text-fill: red;");

        confirmButton.setOnAction(e -> {
            try {
                int players = Integer.parseInt(playerInput.getText());
                if (players >= 1 && players <= 4) {
                    dialog.setUserData(players);
                    dialog.close();
                } else {
                    errorLabel.setText("Please enter a number between 1 and 4.");
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

        backgroundRect.setFill(Color.WHITE);
        backgroundRect.setArcWidth(20); 
        backgroundRect.setArcHeight(20);
        backgroundRect.setStroke(Color.GRAY);
        backgroundRect.setStrokeWidth(2);
        return backgroundRect;
    }

    public Button manageButton(String name, boolean on) {
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
