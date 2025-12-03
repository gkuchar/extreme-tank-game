import model.*;
import model.factory.*;
import model.state.GameState;

public class ModelTest {

    public static void main(String[] args) {

        GameState state = GameState.getInstance();

        System.out.println("=== MODEL TEST START ===");

        // 1) MedPack pickup
        resetState(state);

        Tank player = TankFactory.createTank(TankType.PLAYER, 100, 100);
        Tank enemy = TankFactory.createTank(TankType.ENEMY, 150, 100);
        MedPack pack = MedPackFactory.createMedPack(100, 100, 20, 20);

        state.addTank(player);
        state.addTank(enemy);
        state.addMedPack(pack);

        System.out.println("\n[Phase 1] MedPack pickup:");
        System.out.println("Initial player health: " + player.getHealth());
        System.out.println("Initial medpacks: " + state.getMedPacks().size());

        state.update(0.016);

        System.out.println("After update, player health (should be 100): " + player.getHealth());
        System.out.println("Medpacks (should be 0): " + state.getMedPacks().size());

        // 2) Missile kills enemy in front of player
        resetState(state);

        player = TankFactory.createTank(TankType.PLAYER, 100, 100);
        enemy = TankFactory.createTank(TankType.ENEMY, 150, 100);
        state.addTank(player);
        state.addTank(enemy);

        // face right before firing
        player.setDirection(Direction.RIGHT);
        Missile shot = player.fire();
        state.addMissile(shot);

        for (int i = 0; i < 200; i++) {
            state.update(0.016);
        }

        System.out.println("\n[Phase 2] Missile vs enemy:");
        System.out.println("Enemy alive (should be false): " + enemy.isAlive());
        System.out.println("Explosions count (should be >= 1): " + state.getExplosions().size());
        System.out.println("Victory flag (should be true): " + state.isVictory());

        // 3) Missile hits wall
        resetState(state);

        player = TankFactory.createTank(TankType.PLAYER, 100, 100);
        // put wall directly in front of player
        Wall wall = WallFactory.createWall(140, 100, 40, 40);

        state.addTank(player);
        state.addWall(wall);

        player.setDirection(Direction.RIGHT);
        Missile shot2 = player.fire();
        state.addMissile(shot2);

        for (int i = 0; i < 200; i++) {
            state.update(0.016);
        }

        System.out.println("\n[Phase 3] Missile vs wall:");
        System.out.println("Missiles after wall absorption (should be 0): " + state.getMissiles().size());

        // 4) Bounds enforcement for tank
        resetState(state);

        player = TankFactory.createTank(TankType.PLAYER, -50, -50);
        state.addTank(player);

        // dt 0 so we do not move, only enforce bounds
        state.update(0.0);

        System.out.println("\n[Phase 4] Bounds enforcement:");
        System.out.println("Player x (should be >= 0): " + player.getX());
        System.out.println("Player y (should be >= 0): " + player.getY());

        // 5) Game over flag when player dies
        resetState(state);

        player = TankFactory.createTank(TankType.PLAYER, 100, 100);
        state.addTank(player);

        System.out.println("\n[Phase 5] Game over:");
        System.out.println("Initial gameOver (should be false): " + state.isGameOver());
        player.damage(9999);
        state.update(0.016);
        System.out.println("gameOver after killing player (should be true): " + state.isGameOver());

        System.out.println("\n=== MODEL TEST END ===");
    }

    private static void resetState(GameState state) {
        state.getTanks().clear();
        state.getMissiles().clear();
        state.getWalls().clear();
        state.getMedPacks().clear();
        state.getExplosions().clear();
        // if you want, you can reflect-reset gameOver and victory, or add a reset() method to GameState
    }
}
