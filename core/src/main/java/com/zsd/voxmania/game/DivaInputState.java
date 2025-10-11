package com.zsd.voxmania.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * This was based from Aura39's godot script version of this input system.
 * @author Aura39
 */
public class DivaInputState extends InputAdapter {

    public enum TargetInputType { NONE, TRIANGLE, SQUARE, CROSS, CIRCLE }
    public enum SliderInputType { NONE, LEFT, RIGHT }

    private static class TouchInfo {
        Vector2 pos;
        float timeHeld;
        boolean firedJustPressed;
        boolean isHold;
        TouchInfo(Vector2 pos) { this.pos = pos; this.timeHeld = 0f; }
    }

    private final Map<Integer, TouchInfo> touches = new HashMap<>();
    private final Map<Integer, SliderInputType> sliders = new HashMap<>();

    public interface PressCallback { void onJustPressed(ArrayList<TargetInputType> pressedTargets); }
    public interface HoldCallback { void onHeld(ArrayList<TargetInputType> heldTargets); }
    public interface ReleaseCallback { void onReleased(ArrayList<TargetInputType> releasedTargets); }
    public interface SlideCallback { void onSliding(ArrayList<SliderInputType> activeSliders); }

    private final PressCallback pressCallback;
    private final HoldCallback holdCallback;
    private final ReleaseCallback releaseCallback;
    private final SlideCallback slideCallback;

    private static final float HOLD_THRESHOLD = 0.15f;

    public DivaInputState(
        PressCallback pressCb,
        HoldCallback holdCb,
        ReleaseCallback releaseCb,
        SlideCallback slideCb
    ) {
        this.pressCallback = pressCb;
        this.holdCallback = holdCb;
        this.releaseCallback = releaseCb;
        this.slideCallback = slideCb;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        touches.put(pointer, new TouchInfo(new Vector2(screenX, screenY)));
        return true;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        TouchInfo info = touches.remove(pointer);
        sliders.remove(pointer);

        if (info != null && releaseCallback != null) {
            ArrayList<TargetInputType> released = new ArrayList<>();
            released.add(getTargetType(info.pos.x));
            releaseCallback.onReleased(released);
        }
        return true;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        TouchInfo info = touches.get(pointer);
        if (info != null) {
            float dx = screenX - info.pos.x;
            SliderInputType type = dx < 0 ? SliderInputType.LEFT : SliderInputType.RIGHT;
            sliders.put(pointer, type);

            if (slideCallback != null)
                slideCallback.onSliding(new ArrayList<>(sliders.values()));
        }
        return true;
    }

    public void update(float delta) {
        ArrayList<TargetInputType> justPressed = new ArrayList<>();
        ArrayList<TargetInputType> held = new ArrayList<>();

        for (TouchInfo info : touches.values()) {
            info.timeHeld += delta;
            if (info.timeHeld >= HOLD_THRESHOLD)
                info.isHold = true;

            TargetInputType type = getTargetType(info.pos.x);

            if (info.isHold) {
                held.add(type);
            } else if (!info.firedJustPressed) {
                justPressed.add(type);
                info.firedJustPressed = true;
            }
        }

        if (!justPressed.isEmpty() && pressCallback != null)
            pressCallback.onJustPressed(justPressed);

        if (!held.isEmpty() && holdCallback != null)
            holdCallback.onHeld(held);
    }

    private TargetInputType getTargetType(float x) {
        float width = Gdx.graphics.getWidth();
        float range = x / width;
        if (range < 0.25f) return TargetInputType.TRIANGLE;
        else if (range < 0.5f) return TargetInputType.SQUARE;
        else if (range < 0.75f) return TargetInputType.CROSS;
        else return TargetInputType.CIRCLE;
    }
}
