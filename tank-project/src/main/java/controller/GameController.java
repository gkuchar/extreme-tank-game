package controller;

import javafx.animation.AnimationTimer;
import javafx.scene.Scene;
import model.Tank;
import model.state.GameState;
import model.strategies.PlayerMovementStrategy;

public class GameController {

    private final GameState state = GameState.getInstance();
    private final AnimationTimer timer;
    private final Tank player;
    private final Runnable renderCallback;
    private final PlayerMovementStrategy movement;
    private long lastTime = 0;

    public GameController(Scene scene, Tank player, PlayerMovementStrategy movement, Runnable renderCallback) {
        this.player = player;
        this.movement = movement;
        this.renderCallback = renderCallback;

        setupInput(scene);

        timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                if (lastTime == 0) {
                    lastTime = now;
                    return;
                }

                double dt = (now - lastTime) / 1_000_000_000.0;
                lastTime = now;

                state.update(dt);
                renderCallback.run();

                if (state.isGameOver() || state.isVictory()) {
                    stop();
                }
            }
        };
    }

    private void setupInput(Scene scene) {
        scene.setOnKeyPressed(e -> {
            switch (e.getCode()) {
                case UP -> movement.setUp(true);
                case DOWN -> movement.setDown(true);
                case LEFT -> movement.setLeft(true);
                case RIGHT -> movement.setRight(true);
                case SPACE -> state.addMissile(player.fire());
            }
        });

        scene.setOnKeyReleased(e -> {
            switch (e.getCode()) {
                case UP -> movement.setUp(false);
                case DOWN -> movement.setDown(false);
                case LEFT -> movement.setLeft(false);
                case RIGHT -> movement.setRight(false);
            }
        });
    }

    public void start() {
        timer.start();

    }

    public void stop() {
        timer.stop();
    }
}
