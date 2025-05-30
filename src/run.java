import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.animation.AnimationTimer;
import javafx.scene.input.KeyCode;

public class run { //AQUI TA FALANDO QUE RUN TEM QUE USAR START SE FOR HERDAR APLICATION!

    private int currentPlayer = 0;
    public int currentRound = 0;

    private boolean quit = false;
    private String lastKeyPressed = "";
    private draw make = new draw(); //faco assim?

    /* 
    public static void main(String[] args) {
    
        Application.launch(Draw.class, args);
        int playerAmount = draw.menu();
        if (playerAmount == 0) {
            System.out.println("Jogo encerrado.");
            return;
        } else if (playerAmount > 0) {
            initializer start = new initializer();
            monopoly.board tabuleiro = start.startBoard(playerAmount, 40);
            startGameLoop(playerAmount, tabuleiro);
        }
    }
    
    @Override
    public void start(Stage primaryStage) {
        // Configurar a janela principal do JavaFX
        VBox layout = new VBox();
    
        Scene scene = new Scene(layout, 300, 200);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Jogo");
        primaryStage.show();
    
        //initializer();
        monopoly.board tabuleiro = new monopoly.board(playerAmount); //TEM QUE INICIALIZAR O TABULEIRO COM TUDO PRONTO AQUI E MANDAR PRO LOOP!!!
        property prop = new property(0, 0);
        prop.value[0] = 1000;
        prop.houses[0] = 1500;
        prop.rent[0] = 500;
        tabuleiro.map.addProp(prop);
        startGameLoop(playerAmount, tabuleiro, scene, primaryStage);
    };
    */

    private void initializer() { //ONDE COLOCA ISSO?
        // Implementar lógica de inicialização do jogo
        System.out.println("Inicializando o jogo...");
    }

    private void detectEnterOrEsc(Scene scene) {
        scene.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                lastKeyPressed = "ENTER";
            } else if (event.getCode() == KeyCode.ESCAPE) {
                lastKeyPressed = "ESC";
            }
        });
        scene.setOnKeyReleased(event -> {
            lastKeyPressed = "";
        });
    }

    private void pauseMenu(AnimationTimer gameTimer, Stage primaryStage) {
        // Pause the game timer
        gameTimer.stop();

        // Create a new Stage for the pause menu
        Stage pauseStage = new Stage();
        pauseStage.initOwner(primaryStage); // Set the owner of the pause menu
        pauseStage.setTitle("Pause Menu");

        // Create buttons for "Continue" and "Quit"
        Button continueButton = new Button("Continue");
        Button quitButton = new Button("Quit");

        // Layout for the pause menu
        VBox layout = new VBox(10);
        layout.setStyle("-fx-padding: 10; -fx-alignment: center;");
        layout.getChildren().addAll(continueButton, quitButton);

        Scene pauseScene = new Scene(layout, 200, 150);
        pauseStage.setScene(pauseScene);

        // Set button actions
        continueButton.setOnAction(e -> {
            pauseStage.close(); // Close the pause menu
            gameTimer.start(); // Resume the game
        });

        quitButton.setOnAction(e -> {
            pauseStage.close(); // Close the pause menu
            primaryStage.close(); // Close the main game window
        });

        // Show the pause menu
        pauseStage.showAndWait(); // Blocks interaction with the main game window
    }

    public void startGameLoop(int totalPlayers, monopoly.board tabuleiro, Scene scene, Stage primaryStage) {
        // Track current player using an array for mutability
        int maxRounds = 30;
        double FPS = 60;
        double drawInterval = 1_000_000_000 / FPS; // Frame interval in nanoseconds
        long[] lastUpdateTime = { System.nanoTime() }; // Store last update time

        AnimationTimer gameTimer = new AnimationTimer() {
            @Override
            public void handle(long currentTime) {
                if ((currentTime - lastUpdateTime[0] >= drawInterval)
                        && (!tabuleiro.getGamers()[currentPlayer].getBankruptcy())) {
                    // Update game logic
                    System.out.println("Jogador " + (currentPlayer + 1) + " está jogando.");
                    player gamer = tabuleiro.getGamers()[currentPlayer];

                    if ("ENTER".equals(lastKeyPressed)) { //NAO ENTENDI ESSA PARTE!PQ APERTAR ENTER?
                        if (!gamer.checkIfBroke())
                        {
                            make.diceUI(make.getRoot(), tabuleiro.getDie());
                        }
                        if (gamer.move(tabuleiro.getPlace(gamer.getPosition()), tabuleiro.getDie(),
                                tabuleiro.getSquaresQuantity())) {
                            draw.movePlayer(gamer, tabuleiro.getDie().checkTotalValue());
                            int stocks = tabuleiro.getBank().getOwner(gamer.getPosition());
                            stocks = tabuleiro.getGamers()[stocks].checkStocks();
                            gamer.update(tabuleiro.getLocation(gamer.getPosition()), tabuleiro.getBank(),
                                    tabuleiro.getSquaresQuantity(), stocks, tabuleiro.getGamers(),
                                    monopoly.board.getPlayers(), gamer.getId());
                            draw.movePlayer(gamer, gamer.getSpecialDistance());
                            gamer.zeraSpecialDistance();
                        }

                        // Handle game logic (bankruptcy, victory, etc.)
                        if (!gamer.getBankruptcy() && !(tabuleiro.getLocation(gamer.getPosition()) instanceof special)) //se o gamer nao faliu e a posicao nao e especial
                        {
                            squares land = tabuleiro.getLocation(gamer.getPosition()); //separa o terreno e modo capitalista (nao troca)

                            player rival = monopoly.board.getPlayer(tabuleiro.getBank().getOwner(gamer.getPosition()));

                            if (land instanceof property)
                            {
                                make.propertyUI(make.getRoot(), (property)land, gamer, tabuleiro.getBank(), gamer.getPortfolio(), 
                                            rival.getPortfolio(), rival.getWallet(), gamer.getWallet(), gamer.getId(), land);
                            }
                            else if (land instanceof stocks)
                            {
                                make.stocksUI(make.getRoot(), (stocks)land, tabuleiro.getBank(), gamer, gamer.getPortfolio(), 
                                        rival.getPortfolio(), rival.getWallet(), gamer.getWallet(), land);
                            }
                            else
                            {
                                //EU NAO SEI OQ FAZER MAIS!!!
                            }
                            /**/
                        }
                        if (currentRound >= maxRounds)
                            stop();
                        else if (gamer.checkVictory(tabuleiro.getBank(), tabuleiro.getStocksQuantity()) == 1) {
                            System.out.println("Jogador " + (currentPlayer + 1) + " venceu o jogo!");
                            stop(); // Stop the game loop
                        } else {
                            currentPlayer++; // Move to next player
                            if (currentPlayer >= monopoly.board.getPlayers()) {
                                currentPlayer = 0;
                                currentRound++;
                            }

                        }
                    } else if ("ESC".equals(lastKeyPressed)) {
                        pauseMenu(this, primaryStage); // Pause logic
                    }

                    // Update time
                    lastUpdateTime[0] = currentTime;
                } else if (tabuleiro.getGamers()[currentPlayer].getBankruptcy()) {
                    currentPlayer++; // Move to next player
                    if (currentPlayer >= monopoly.board.getPlayers()) {
                        currentPlayer = 0;
                        currentRound++;
                    }
                }
            }
        };
        gameTimer.start();
    }
}