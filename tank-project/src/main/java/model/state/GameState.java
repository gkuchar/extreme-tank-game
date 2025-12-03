package model.state;

import model.*;
import model.factory.ExplosionFactory;
import model.events.*;
import model.strategies.AIMovementStrategy;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class GameState {

    // Eager singleton instance
    private static final GameState INSTANCE = new GameState();

    // Lists of game objects
    private final List<Tank> tanks = new ArrayList<>();
    private final List<Missile> missiles = new ArrayList<>();
    private final List<Wall> walls = new ArrayList<>();
    private final List<MedPack> medPacks = new ArrayList<>();
    private final List<Explosion> explosions = new ArrayList<>();

    private boolean gameOver = false;
    private PlayerTank playerTank;
    private boolean victory = false;
    private final double worldWidth = 800;
    private final double worldHeight = 600;
    private final int tileSize = 32;
    private final int gridWidth = (int)(worldWidth / tileSize);
    private final int gridHeight = (int)(worldHeight / tileSize);
    private final boolean[][] blocked = new boolean[gridWidth][gridHeight];

    // Private constructor
    private GameState() {}

    // Global access point
    public static GameState getInstance() {
        return INSTANCE;
    }

    // Adders
    public void addTank(Tank t) {
        tanks.add(t);
        markBlocked(t);
    }

    public void addMissile(Missile m) {
        missiles.add(m);
    }

    public void addWall(Wall w) {
        walls.add(w);
        markWallBlocked(w);
    }

    public void addMedPack(MedPack m) {
        medPacks.add(m);
        markBlocked(m);
    }

    public void addExplosion(Explosion e) {
        explosions.add(e);
    }

    // The main update loop for the model
    public void update(double dt) {
        // update all objects, keep in bounds
        tanks.forEach(t ->  {
            double oldX = t.getX();
            double oldY = t.getY();

            // let the tank attempt to move
            t.update(dt);

            // resolve horizontal collision
            boolean collidedX = false;
            for (Wall w : walls) {
                if (t.intersects(w)) {
                    collidedX = true;
                    break;
                }
            }
            if (collidedX) {
                t.setX(oldX);  // revert X movement
            }

            // resolve vertical collision
            boolean collidedY = false;
            for (Wall w : walls) {
                if (t.intersects(w)) {
                    collidedY = true;
                    break;
                }
            }
            if (collidedY) {
                t.setY(oldY);  // revert Y movement
            }

            enforceBounds(t);

            if (t instanceof EnemyTank enemy) {
                AIMovementStrategy ai = (AIMovementStrategy) enemy.getMovementStrategy();

                if (ai.shouldFire()) {
                    Missile m = enemy.fire();
                    addMissile(m);
                    ai.resetFire();
                }
            }

        });
        missiles.forEach(m -> {
            m.update(dt);
            enforceBounds(m);
        });
        medPacks.forEach(m -> m.update(dt));
        explosions.forEach(e -> e.update(dt));

        // handle collisions
        checkCollisions();

        // remove destroyed objects
        cleanup();
    }

    public void markWallBlocked(Wall w) {
        int x0 = (int)(w.getX() / tileSize);
        int y0 = (int)(w.getY() / tileSize);

        int x1 = (int)((w.getX() + w.getWidth()) / tileSize);
        int y1 = (int)((w.getY() + w.getHeight()) / tileSize);

        for (int gx = x0; gx <= x1; gx++) {
            for (int gy = y0; gy <= y1; gy++) {
                if (gx >= 0 && gx < gridWidth && gy >= 0 && gy < gridHeight) {
                    blocked[gx][gy] = true;
                }
            }
        }
    }

    private void markBlocked(GameObject obj) {
        int x0 = (int)(obj.getX() / tileSize);
        int y0 = (int)(obj.getY() / tileSize);
        int x1 = (int)((obj.getX() + obj.getWidth()) / tileSize);
        int y1 = (int)((obj.getY() + obj.getHeight()) / tileSize);

        for (int gx = x0; gx <= x1; gx++) {
            for (int gy = y0; gy <= y1; gy++) {
                if (gx >= 0 && gx < gridWidth && gy >= 0 && gy < gridHeight) {
                    blocked[gx][gy] = true;
                }
            }
        }
    }

    public double[] getRandomFreeLocation() {
        while (true) {
            int gx = (int)(Math.random() * gridWidth);
            int gy = (int)(Math.random() * gridHeight);

            if (!blocked[gx][gy]) {
                double x = gx * tileSize;
                double y = gy * tileSize;
                return new double[]{x, y};
            }
        }
    }


    private void enforceBounds(Tank t) {
        if (t.getX() < 0) t.setX(0);
        if (t.getY() < 0) t.setY(0);

        if (t.getX() + t.getWidth() > worldWidth ) t.setX(worldWidth - t.getWidth());
        if (t.getY() + t.getHeight() > worldHeight ) t.setY(worldHeight - t.getHeight());
    }

    private void enforceBounds(Missile m) {
        if (m.getX() < 0 || m.getX() > worldWidth ||
                m.getY() < 0 || m.getY() > worldHeight) {
            m.destroy();
        }
    }

    private void cleanup() {
        tanks.removeIf(t -> !t.isAlive());
        missiles.removeIf(m -> !m.isAlive());
        medPacks.removeIf(m -> !m.isAlive());
        explosions.removeIf(e -> !e.isAlive());
    }

    private void checkCollisions() {
        // missile hitting tank
        for (Missile m : missiles) {
            for (Tank t : tanks) {
                if (t != m.getOwner() && m.intersects(t)) {

                    if(m.getOwner() instanceof EnemyTank && t instanceof EnemyTank) {
                        continue;
                    }
                    t.damage(25);
                    m.destroy();

                    if (!t.isAlive()) {
                        Explosion e = ExplosionFactory.createExplosion(
                                t.getX(),
                                t.getY(),
                                32,
                                32
                        );
                        addExplosion(e);
                        if (t instanceof EnemyTank) {
                            GameEventBus.getInstance().fireEvent(GameEventType.ENEMY_DESTROYED, t);
                            checkVictoryCondition();
                        }
                        if (t instanceof PlayerTank) {
                            gameOver = true;
                            GameEventBus.getInstance().fireEvent(GameEventType.GAME_OVER, null);
                        }
                    }
                }
            }
        }

        // missile absorbed by wall
        for (Missile m : missiles) {
            for (Wall w : walls) {
                if (m.intersects(w)) {
                    m.destroy();
                }
            }
        }

        PlayerTank player = getPlayer();

        // tank running into tank
        for (Tank t : tanks) {
            if (t != player && t.intersects(player)) {
                player.damage(50);

                Explosion e = ExplosionFactory.createExplosion(
                        t.getX(),
                        t.getY(),
                        32,
                        32
                );
                addExplosion(e);
                gameOver = true;
                GameEventBus.getInstance().fireEvent(GameEventType.GAME_OVER, null);
            }
        }

        // tank pick up medpack
        for (Tank t : tanks) {
            for (MedPack m : medPacks) {
                if (t.intersects(m)) {
                    t.setHealth(100);
                    m.destroy();
                    if (t instanceof PlayerTank) GameEventBus.getInstance().fireEvent(GameEventType.PLAYER_HEALTH_CHANGED, t);
                }
            }
        }
    }

    private void checkVictoryCondition() {
        long enemiesLeft = tanks.stream().filter(t -> t instanceof EnemyTank && t.isAlive()).count();
        if (enemiesLeft == 0) {
            victory = true;
            GameEventBus.getInstance().fireEvent(GameEventType.VICTORY, null);
        }
    }

    // Getters
    public List<Tank> getTanks() { return tanks; }
    public List<Missile> getMissiles() { return missiles; }
    public List<Wall> getWalls() { return walls; }
    public List<MedPack> getMedPacks() { return medPacks; }
    public List<Explosion> getExplosions() { return explosions; }

    public boolean isGameOver() { return gameOver; }
    public boolean isVictory() { return victory; }

    public double getWorldWidth() { return worldWidth; }
    public double getWorldHeight() { return worldHeight; }

    public void setPlayer(PlayerTank p) {
        this.playerTank = p;
    }

    public PlayerTank getPlayer() {
        return playerTank;
    }

    public void reset() {
        tanks.clear();
        missiles.clear();
        walls.clear();
        medPacks.clear();
        explosions.clear();

        gameOver = false;
        victory = false;

        // also reset blocked[][] grid
        for (int i = 0; i < gridWidth; i++) {
            for (int j = 0; j < gridHeight; j++) {
                blocked[i][j] = false;
            }
        }
    }

    public double[] getRandomFreeMiddleLocation() {
        double centerX = (gridWidth  / 2.0);
        double centerY = (gridHeight / 2.0);

        // build list of all grid cells
        List<int[]> cells = new ArrayList<>();
        for (int gx = 0; gx < gridWidth; gx++) {
            for (int gy = 0; gy < gridHeight; gy++) {
                cells.add(new int[]{gx, gy});
            }
        }

        // sort by distance to center
        cells.sort((a, b) -> {
            double da = Math.hypot(a[0] - centerX, a[1] - centerY);
            double db = Math.hypot(b[0] - centerX, b[1] - centerY);
            return Double.compare(da, db);
        });

        // return the first unblocked cell
        for (int[] cell : cells) {
            int gx = cell[0];
            int gy = cell[1];

            if (!blocked[gx][gy]) {
                return new double[]{
                        gx * tileSize,
                        gy * tileSize
                };
            }
        }

        // fallback (should never happen)
        return getRandomFreeLocation();
    }
}

