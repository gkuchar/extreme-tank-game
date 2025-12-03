package model.events;

public interface GameEventListener {
    void onGameEvent(GameEventType type, Object payload);
}
