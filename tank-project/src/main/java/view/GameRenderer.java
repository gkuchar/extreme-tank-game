package view;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import model.*;
import model.state.GameState;

public class GameRenderer {

    private final GraphicsContext gc;
    private final GameState state;

    // Sprites
    private final Image tankUpSprite;
    private final Image tankDownSprite;
    private final Image tankRightSprite;
    private final Image tankLeftSprite;

    private final Image missUpSprite;
    private final Image missDownSprite;
    private final Image missRightSprite;
    private final Image missLeftSprite;

    private final Image explosionSprite;
    private final Image healthSprite;

    public GameRenderer(GraphicsContext gc, GameState state) {
        this.gc = gc;
        this.state = state;

        tankUpSprite = new Image(getClass().getResource("/images/tankU.gif").toExternalForm());
        tankDownSprite = new Image(getClass().getResource("/images/tankD.gif").toExternalForm());
        tankRightSprite = new Image(getClass().getResource("/images/tankR.gif").toExternalForm());
        tankLeftSprite = new Image(getClass().getResource("/images/tankL.gif").toExternalForm());

        missUpSprite = new Image(getClass().getResource("/images/missileU.gif").toExternalForm());
        missDownSprite = new Image(getClass().getResource("/images/missileD.gif").toExternalForm());
        missRightSprite = new Image(getClass().getResource("/images/missileR.gif").toExternalForm());
        missLeftSprite = new Image(getClass().getResource("/images/missileL.gif").toExternalForm());

        explosionSprite = new Image(getClass().getResource("/images/0.gif").toExternalForm());
        healthSprite = new Image(getClass().getResource("/images/health-red.png").toExternalForm());
    }

    public void render() {

        gc.setFill(Color.BLACK);
        gc.fillRect(0, 0, state.getWorldWidth(), state.getWorldHeight());

        // Walls first
        gc.setFill(Color.BROWN);
        for (Wall w : state.getWalls()) {
            gc.fillRect(w.getX(), w.getY(), w.getWidth(), w.getHeight());
        }

        // Tanks
        for (Tank t : state.getTanks()) {
                switch (t.getDirection()) {
                    case UP -> gc.drawImage(tankUpSprite, t.getX(), t.getY(), t.getWidth(), t.getHeight());
                    case DOWN -> gc.drawImage(tankDownSprite, t.getX(), t.getY(), t.getWidth(), t.getHeight());
                    case RIGHT -> gc.drawImage(tankRightSprite, t.getX(), t.getY(), t.getWidth(), t.getHeight());
                    case LEFT -> gc.drawImage(tankLeftSprite, t.getX(), t.getY(), t.getWidth(), t.getHeight());
                }

        }

        // Missiles
        for (Missile m : state.getMissiles()) {
            switch (m.getDirection()) {
                case UP -> gc.drawImage(missUpSprite, m.getX(), m.getY(), m.getWidth(), m.getHeight());
                case DOWN -> gc.drawImage(missDownSprite, m.getX(), m.getY(), m.getWidth(), m.getHeight());
                case RIGHT -> gc.drawImage(missRightSprite, m.getX(), m.getY(), m.getWidth(), m.getHeight());
                case LEFT -> gc.drawImage(missLeftSprite, m.getX(), m.getY(), m.getWidth(), m.getHeight());
            }
        }

        // Explosions
        for (Explosion e : state.getExplosions()) {
            gc.drawImage(explosionSprite, e.getX(), e.getY(), e.getWidth(), e.getHeight());
        }

        // health
        for (MedPack m : state.getMedPacks()) {
            gc.drawImage(healthSprite, m.getX(), m.getY(), m.getWidth(), m.getHeight());
        }
    }
}
