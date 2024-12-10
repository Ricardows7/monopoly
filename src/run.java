import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.animation.AnimationTimer;
import javafx.scene.input.KeyCode;

public class run{  //AQUI TA FALANDO QUE RUN TEM QUE USAR START SE FOR HERDAR APLICATION!

    private int currentPlayer = 0;
    public int currentRound = 0;

    private boolean quit = false;
    private String lastKeyPressed = "";
    private draw make = new draw(); //faco assim?

    private void initializer() {    //ONDE COLOCA ISSO?
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

                    if ("ENTER".equals(lastKeyPressed)) {   //NAO ENTENDI ESSA PARTE!PQ APERTAR ENTER?
                        if (!gamer.checkIfBroke()) {
                            /*Button die = make.manageButton("Lance os dados", true); //BOTAO DE JOGAR DADO! TEM QUE VER COMO CHAMAR FUNCAO DO DRAW AQUI!
                            die.setOnAction(e -> {
                                tabuleiro.getDie().throwDie();
                            });*/
                        }
                        if (gamer.move(tabuleiro.getPlace(gamer.getPosition()), tabuleiro.getDie(),
                                tabuleiro.getSquaresQuantity())) {
                            int stocks = tabuleiro.getBank().getOwner(gamer.getPosition());
                            stocks = tabuleiro.getGamers()[stocks].checkStocks();
                            gamer.update(tabuleiro.getLocation(gamer.getPosition()), tabuleiro.getBank(),
                                    tabuleiro.getSquaresQuantity(), stocks, tabuleiro.getGamers(),
                                    tabuleiro.getPlayers(), gamer.getId());
                        }

                        // Handle game logic (bankruptcy, victory, etc.)
                        if (!gamer.getBankruptcy() && !(tabuleiro.getLocation(gamer.getPosition()) instanceof special)) //se o gamer nao faliu e a posicao nao e especial
                        {
                            squares land = tabuleiro.getLocation(gamer.getPosition()); //separa o terreno e modo capitalista (nao troca)

                            if (!gamer.verifyOwnership(tabuleiro.getBank())) { //se o player NAO e dono
                                /*if (gamer.Check() < land.getValue())
                                //Button buyPropertyButton = make.manageButton("Comprar Propriedade", true);
                                    make.buttonSwitch(buyPropertyButton, false); //se nao tem dinheiro, botao translucido
                                else
                                    make.buttonSwitch(buyPropertyButton, true);
                                
                                if (tabuleiro.getBank().getOwner(gamer.getPosition()) != 0) { //Verificar se a propriedade nao e possuida pelo banco
                                    player rival = monopoly.board.getPlayer(tabuleiro.getBank().getOwner(gamer.getPosition()));
                                    /*buyPropertyButton.setOnAction(e -> {
                                        (tabuleiro.getBank()).sellProperties(gamer.getPortfolio(), rival.getPortfolio(),
                                                rival.getWallet(), gamer.getWallet(), gamer.getId(), land, true);
                                    });*/
                                } else {
                                    buyPropertyButton.setOnAction(e -> {
                                        (tabuleiro.getBank()).sellProperties(gamer.getPortfolio(), gamer.getWallet(),
                                                gamer.getId(), land, false);
                                    });
                                }
                            } else { // se o player é dono
                                     // Criação dos botões principais
                                Button improveButton = make.manageButton("Melhorar Propriedade", true);
                                Button mortgageButton = make.manageButton("Hipotecar Propriedade", true);

                                // Configuração do botão "Melhorar Propriedade"
                                improveButton.setOnAction(e -> {

                                    // Criação das opções "Melhorar" e "Parar"
                                    Button improveOption = new Button("Melhorar");
                                    Button stopOption = new Button("Parar");

                                    // Lógica para o botão "Melhorar"
                                    improveOption.setOnAction(ev -> {
                                        boolean improved = land.improve(gamer.getWallet()); // Tenta melhorar a propriedade
                                        if (improved) {
                                            System.out.println("Propriedade melhorada!");
                                        } else {
                                            System.out.println(
                                                    "Não é possível melhorar a propriedade (saldo insuficiente ou limite alcançado).");
                                        }
                                    });

                                    // Lógica para o botão "Parar"
                                    stopOption.setOnAction(ev -> {
                                        System.out.println("Ação cancelada.");
                                        // Reexibe os botões principais
                                    });
                                });

                                // Configuração do botão "Hipotecar Propriedade"
                                mortgageButton.setOnAction(e -> {
                                    land.getMortgage(gamer.getWallet());
                                    System.out.println("Propriedade hipotecada!");
                                });

                                // Certifique-se de que o layout seja adicionado ao contêiner pai
                                //parentLayout.getChildren().add(gameLayout); //quem seria aqui???
                            }
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