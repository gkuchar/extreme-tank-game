# 🚀 Extreme Tank Game

A JavaFX-based 2D action game showcasing clean OOP architecture, real game-loop engineering, and advanced design patterns.

## 🎯 Overview

**Extreme Tank Game** is a fully interactive 2D combat game built in Java and JavaFX, designed to demonstrate strong software engineering fundamentals. The project features real-time movement, AI-driven enemy behavior, collision detection, event-driven UI updates, and a clean separation of concerns across the model-view-controller structure.

This project is intentionally built to resemble the architecture of production game engines and real-world interactive systems.

## 🧠 What This Project Demonstrates

This codebase highlights skills that matter to engineering teams:

### ✔ Strong Object-Oriented Design
- Abstract base classes for shared behavior
- Clear encapsulation of game logic
- Extensible model layer with zero UI dependencies

### ✔ Clean Architectural Patterns
- **Factory Pattern** to cleanly construct Tanks, Missiles, Walls, MedPacks, and Explosions
- **Strategy Pattern** powering both player movement and AI-driven enemy tanks
- **Singleton Pattern** for centralized, globally accessible game state
- **Observer Pattern** enabling instant HUD updates (health, score, game over)

### ✔ Real Game Engineering Concepts
- Continuous update loop with JavaFX `AnimationTimer`
- Bounding-box collision handling and blocked-tile grid logic
- AI behavior management (direction changes, timed firing, movement updates)
- Deterministic world generation with dynamic obstacle placement
- Scalable asset rendering on JavaFX Canvas

## 🎮 Gameplay Features

- Player-controlled tank with full movement and shooting
- Six enemy tanks with autonomous movement and missile firing
- Randomized world layout with walls and medpacks
- Damage, health system, explosions, and destruction effects
- Victory screen and restartable sessions
- Smooth rendering and constant game loop updates

## 🏛 Project Structure
```
src/
  main/
    java/
      model/          # GameObjects, Tanks, Missiles, Walls, MedPacks, Explosion
      model/state/    # GameState (singleton), world grid, collision logic
      model/factory/  # Factories for building game entities
      model/strategies/# AI strategy + player movement strategy
      model/events/   # Observer pattern for UI updates
      view/           # JavaFX renderer (Canvas)
      controller/     # Input handling + game loop
```

## 🛠 Technologies Used

- **Java 17**
- **JavaFX** (Canvas API)
- OOP design and software architecture
- Real-time game loop engineering
- Event-driven programming

## ▶️ Running the Game

### IntelliJ
1. Import the project
2. Add JavaFX SDK to the module path
3. Run `TankGameApplication`

### Terminal
```bash
java --module-path /path/to/javafx-sdk/lib \
     --add-modules javafx.controls,javafx.fxml \
     -cp out/production/TankProject \
     TankGameApplication
```

## 🧩 Future Extensions

- Smarter AI (pathfinding, tracking behavior)
- Multiple levels or maps
- Sound effects and animations
- Power-ups and destructible walls

## 📬 Contact

If you're reviewing this project and would like to discuss the architecture, design decisions, or implementation, feel free to reach out.