package model.events;

import java.util.ArrayList;
import java.util.List;

public class GameEventBus {

    private static final GameEventBus INSTANCE = new GameEventBus();

    private final List<GameEventListener> listeners = new ArrayList<>();

    private GameEventBus() {}

    public static GameEventBus getInstance() {
        return INSTANCE;
    }

    public void addListener(GameEventListener l) {
        listeners.add(l);
    }

    public void fireEvent(GameEventType type, Object payload) {
        for (GameEventListener l : listeners) {
            l.onGameEvent(type, payload);
        }
    }
}
