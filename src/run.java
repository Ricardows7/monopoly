import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class run extends Application {

    private boolean quit = false;
    private String lastKeyPressed = "";

    public static void main(String[] args) {
        launch(args); // Inicializa o JavaFX
    }

    @Override
    public void start(Stage primaryStage) {
        // Configurar a janela principal do JavaFX
        VBox layout = new VBox();
        Button startButton = new Button("Iniciar Jogo");
        Button quitButton = new Button("Sair");

        startButton.setOnAction(e -> {
            int playerAmount = menu();
            if (playerAmount == 0) {
                System.out.println("Jogo encerrado.");
                return;
            }
            initializer();
            gameLoop(playerAmount);
        });

        quitButton.setOnAction(e -> {
            quit = true;
            primaryStage.close();
        });

        layout.getChildren().addAll(startButton, quitButton);

        Scene scene = new Scene(layout, 300, 200);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Jogo");
        primaryStage.show();
    }

    private int menu() {
        // Implementar lógica do menu (exemplo: retornar número de jogadores)
        return 2; // Exemplo fixo
    }

    private void initializer() {
        // Implementar lógica de inicialização do jogo
        System.out.println("Inicializando o jogo...");
    }

    private void detectEnterOrEsc(Scene scene) {
        scene.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                lastKeyPressed = "ENTER";
            } else if (event.getCode() == KeyCode.ESCAPE) {
                lastKeyPressed = "ESC";
                quit = true; // If ESC means quitting
            }
        });
    }

    private void gameLoop(int totalPlayers) {
        // Implementar lógica do loop do jogo
        int currentPlayer = 0;
        double FPS = 60;
        double drawInterval = 1000000000 / FPS;
        double nextDrawTime = System.nanoTime() + drawInterval;

        while (!quit) {
            // Desenha o tabuleiro
            System.out.println("Jogador " + (currentPlayer + 1) + " está jogando.");
            detectEnterOrEsc(scene);
            if (lastKeyPressed.equals("ENTER")) {
                board.gamers[currentPlayer].move(board.map.properties.get(board.gamers[currentPlayer].position),board.dado,board.map.properties.size());
                if (board.gamers[currentPlayer].verifyOwnership == false) {
                    board.gamers[currentPlayer].bankruptcy = board.gamers[currentPlayer].update();
                    //Chama metodos pedindo se ele quer comprar a propriedade do banco ou outro player
                }
                else {
                    board.gamers[currentPlayer].improveProperty();
                }
            }
            board.gamers[currentPlayer].checkVictory();
            currentPlayer++;
            if (currentPlayer >= totalPlayers)
                currentPlayer = 0;
            // Temporizador para FPS
            try {
                double remainingTime = nextDrawTime - System.nanoTime();
                remainingTime /= 1000000;
                if (remainingTime < 0)
                    remainingTime = 0;
                Thread.sleep((long) remainingTime);
                nextDrawTime += drawInterval;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

