import controller.GameController;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import model.*;
import model.events.GameEventBus;
import model.factory.MedPackFactory;
import model.factory.TankFactory;
import model.factory.WallFactory;
import model.state.GameState;
import model.strategies.AIMovementStrategy;
import model.strategies.PlayerMovementStrategy;
import view.GameRenderer;
import java.util.Random;

import static java.awt.Color.yellow;
import static model.events.GameEventType.*;

public class TankGameApplication extends Application {


    @Override
    public void start(Stage stage) {

        GameState state = GameState.getInstance();
        Random randomGen = new Random();
        int tileSize = 32;
        // Create player


        // Create walls
        for(int i = 0; i < 20; i++) {
            double[] pos = GameState.getInstance().getRandomFreeLocation();

            int randW = (randomGen.nextInt(3) + 1) * tileSize;
            int randH = (randomGen.nextInt(3) + 1) * tileSize;
            Wall wall = WallFactory.createWall(pos[0], pos[1], randW, randH);
            state.addWall(wall);
        }

        double[] posPlayer = GameState.getInstance().getRandomFreeMiddleLocation();
        Tank player = TankFactory.createTank(TankType.PLAYER, posPlayer[0], posPlayer[1]);
        GameState.getInstance().setPlayer((PlayerTank) player);
        PlayerMovementStrategy movement =
                (PlayerMovementStrategy) player.getMovementStrategy();

        state.addTank(player);

        // Create med packs
        for(int i = 0; i < 5; i++) {
            double[] pos = GameState.getInstance().getRandomFreeLocation();

            MedPack medPack = MedPackFactory.createMedPack(pos[0], pos[1], tileSize, tileSize);
            state.addMedPack(medPack);
        }

        for(int i = 0; i < 6; i++) {
            double[] pos = GameState.getInstance().getRandomFreeLocation();

            Tank tank = TankFactory.createTank(TankType.ENEMY, pos[0], pos[1]);
            AIMovementStrategy aiMovement =
                    (AIMovementStrategy) tank.getMovementStrategy();

            state.addTank(tank);
        }

        // Create canvas & renderer
        Canvas canvas = new Canvas(state.getWorldWidth(), state.getWorldHeight());
        GraphicsContext gc = canvas.getGraphicsContext2D();
        GameRenderer renderer = new GameRenderer(gc, state);
        Label healthLabel = new Label("Health: 100");
        healthLabel.setStyle("-fx-text-fill: yellow;");
        Label scoreLabel = new Label("Enemies left: 6");
        scoreLabel.setStyle("-fx-text-fill: yellow;");
        Button restartButton = new Button("Try Again");
        restartButton.setStyle("-fx-font-size: 24px; -fx-background-color: black; -fx-text-fill: white;");
        restartButton.setVisible(false);

        Label victoryLabel = new Label("");
        victoryLabel.setStyle("-fx-text-fill: yellow; -fx-font-size: 18px;");
        victoryLabel.setVisible(false);

        Button winRestart = new Button("Play Again");
        winRestart.setStyle("-fx-font-size: 18px; -fx-background-color: black; -fx-text-fill: white;");
        winRestart.setVisible(false);

        HBox hud = new HBox(20, healthLabel, scoreLabel, restartButton, victoryLabel, winRestart);
        hud.setStyle("-fx-font-size: 18; -fx-padding: 10; -fx-background-color: #222; -fx-text-fill: white;");


        BorderPane root = new BorderPane(canvas);
        Scene scene = new Scene(root);

        root.setTop(hud);
        root.setCenter(canvas);

        // Controller
        GameController controller =
                new GameController(scene, player, movement, renderer::render);

        stage.setScene(scene);
        stage.setTitle("Tank Game");
        stage.show();
        stage.requestFocus();

        restartButton.setOnAction(e -> {
            controller.stop();       // Stop old game loop
            state.reset();           // Reset model

            healthLabel.setText("Health: 100");
            scoreLabel.setText("Enemies left: 6");
            restartButton.setVisible(false);

            start(stage);            // Build new world + new controller
        });

        winRestart.setOnAction(e -> {
            state.reset();

            healthLabel.setText("Health: 100");
            scoreLabel.setText("Enemies left: 6");

            victoryLabel.setVisible(false);
            winRestart.setVisible(false);

            start(stage);
        });

        GameEventBus bus = GameEventBus.getInstance();

        bus.addListener((eventType, data) -> {
            switch (eventType) {

                case PLAYER_HEALTH_CHANGED -> {
                    Tank playerTank = (Tank) data;
                    healthLabel.setText("Health: " + playerTank.getHealth());
                }

                case ENEMY_DESTROYED -> {
                    int enemiesLeft =
                            (int) state.getTanks().stream()
                                    .filter(t -> t instanceof EnemyTank && t.isAlive())
                                    .count();

                    scoreLabel.setText("Enemies left: " + enemiesLeft);
                }

                case GAME_OVER -> {
                    scoreLabel.setText("Game Over");
                    healthLabel.setText("Health: 0");
                    restartButton.setVisible(true);
                }

                case VICTORY -> {
                    victoryLabel.setText("You Win!");
                    victoryLabel.setVisible(true);

                    winRestart.setVisible(true);
                }
            }
        });



        controller.start();
    }




    public static void main(String[] args) {
        launch(args);
    }
}
