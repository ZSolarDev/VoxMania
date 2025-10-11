package com.zsd.voxmania.states;

import java.lang.reflect.InvocationTargetException;

public class StateManager {
    public static GameStateEntrypoint game;
    public static State currentState;
    public static SubState currentSubState;

    public static void init(GameStateEntrypoint game)
    {
        StateManager.game = game;
        game.onPause = () -> {
            if (currentSubState != null)
                currentSubState.pause();
        };
        game.onResume = () -> {
            if (currentSubState != null)
                currentSubState.resume();
        };
        game.onResize = (Integer width, Integer height) -> {
            if (currentSubState != null)
                currentSubState.resize(width, height);
        };
    }

    public static void switchState(Class<? extends State> state, Object... params)
    {
        if (game == null)
            throw new RuntimeException("The State Manager is still uninitialized!");
        if (SubState.class.isAssignableFrom(state))
            throw new RuntimeException("You can't switch states to a sub state!");
        if (currentState != null)
            currentState.dispose();

        try {
            Class<?>[] paramTypes = new Class<?>[params.length];
            for (int i = 0; i < params.length; i++)
                paramTypes[i] = params[i].getClass();

            State newState = (State) state.getDeclaredConstructor(paramTypes).newInstance(params);

            if (currentState != null)
                currentState.dispose();

            currentState = newState;
            game.setScreen(newState);
            newState.create();
        }
        catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new RuntimeException("Failed to switch states to " + state.getName(), e);
        }
    }

    public static void openSubState(SubState subState)
    {
        if (game == null)
            throw new RuntimeException("The State Manager is still uninitialized!");
        if (currentSubState != null)
            closeSubState();
        currentSubState = subState;
        subState.show();
        subState.create();
        if (currentState != null)
            currentState.pause();
    }

    public static void closeSubState()
    {
        if (game == null)
            throw new RuntimeException("The State Manager is still uninitialized!");
        if (currentSubState != null) {
            currentSubState.dispose();
            currentSubState = null;
        }
        if (currentState != null)
            currentState.resume();
    }

    public static void update(float delta)
    {
        if (currentSubState == null) {
            if (currentState != null)
                currentState.draw(delta);
        }else {
            currentSubState.render(delta);
            currentSubState.draw(delta);
        }
    }
}
