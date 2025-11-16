package com.zsd.voxmania.game.ui.objects.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.zsd.voxmania.display.screen.drawables.DrawableSprite;
import com.zsd.voxmania.game.ui.UILayer;

import java.util.ArrayList;

public class MobileOverlay extends UILayer {
    public DrawableSprite base;
    public ArrayList<DrawableSprite> pressables = new ArrayList<>();

    public MobileOverlay() {
        base = new DrawableSprite(new Texture(Gdx.files.internal("game/overlay/overlay.png")), this);
        base.setAlpha(0.5f);
        base.setX(position.x);
        base.setY(position.y);
        addDrawable(base);
        String[][] spritePaths = {
            { "game/overlay/targets/", "Triangle.png" },
            { "game/overlay/targets/", "Circle.png" },
            { "game/overlay/targets/", "Cross.png" },
            { "game/overlay/targets/", "Square.png" },
            { "game/overlay/sliders/", "Left.png" },
            { "game/overlay/sliders/", "Right.png" }
        };

        for (String[] path : spritePaths) {
            DrawableSprite sprite = new DrawableSprite(
                new Texture(Gdx.files.internal(path[0] + path[1])),
                this
            );
            sprite.setX(position.x);
            sprite.setY(position.y);
            addDrawable(sprite);
            pressables.add(sprite);
            sprite.setAlpha(0);
        }
    }

    @Override
    public void update(float delta) {
        super.update(delta);
        for (DrawableSprite pressable : pressables) {
            pressable.setAlpha(MathUtils.lerp(pressable.getColor().a, 0, delta * 5));
            pressable.setX(position.x);
            pressable.setY(position.y);
        }
        base.setX(position.x);
        base.setY(position.y);
    }

    public void onPressableHit(int normalizedTargetType)
    {
        int idx = normalizedTargetType == 12 || normalizedTargetType == 15 ? 4 : normalizedTargetType == 13 || normalizedTargetType == 16 ? 5 : normalizedTargetType;
        pressables.get(idx).setAlpha(1);
    }

    public void onPressableGhostHit(int normalizedTargetType)
    {
        int idx = normalizedTargetType == 12 || normalizedTargetType == 15 ? 4 : normalizedTargetType == 13 || normalizedTargetType == 16 ? 5 : normalizedTargetType;
        pressables.get(idx).setAlpha(0.5f);
    }

    @Override
    public void dispose() {
        super.dispose();
        for (DrawableSprite pressable : pressables) {
            removeSprite(pressable);
            pressable.dispose();
        }
        pressables.clear();
        pressables = null;
        removeSprite(base);
        base.dispose();
        base = null;
    }
}
