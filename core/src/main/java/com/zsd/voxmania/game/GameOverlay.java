package com.zsd.voxmania.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Align;
import com.zsd.voxmania.display.screen.drawables.DrawableSprite;
import com.zsd.voxmania.display.screen.drawables.DrawableTTFText;
import com.zsd.voxmania.display.screen.sprite.NestableDrawableRenderer;

import java.util.ArrayList;

public class GameOverlay extends NestableDrawableRenderer
{
    public DrawableSprite base;
    public ArrayList<DrawableSprite> pressables = new ArrayList<>();
    public ArrayList<DrawableSprite> curHeld = new ArrayList<>();
    public ArrayList<DivaInputState.TargetInputType> curHeldData = new ArrayList<>();
    public DrawableTTFText comboText;

    public GameOverlay()
    {
        super(true);
        base = new DrawableSprite(new Texture(Gdx.files.internal("game/overlay/overlay.png")), this);
        base.setAlpha(0.5f);
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
            addDrawable(sprite);
            pressables.add(sprite);
            sprite.setAlpha(0);
        }

        for (int i = 0; i < 4; i++) {
            int texIndex = (i == 1) ? 3 : (i == 3) ? 1 : i;

            DrawableSprite note = new DrawableSprite(
                new Texture(Gdx.files.internal("game/notes/normal/" + texIndex + ".png")),
                this
            );
            note.setY(100);
            note.setAlpha(0);
            note.setOriginCenter();
            curHeld.add(note);
            addDrawable(note);
        }

        comboText = new DrawableTTFText(Gdx.files.internal("game/fonts/score.ttf"), 30, Color.WHITE, 0, 160, "+1", 0, Align.left, false, 5, Color.BLACK);
        addDrawable(comboText);
        Color cTxtColor = comboText.textObj.getColor();
        comboText.textObj.setColor(cTxtColor.r, cTxtColor.g, cTxtColor.b, 0);
    }

    @Override
    public void update(float delta)
    {
        super.update(delta);
        for (DrawableSprite pressable : pressables)
            pressable.setAlpha(MathUtils.lerp(pressable.getColor().a, 0, delta * 5));
        DivaInputState.TargetInputType[] types = {
            DivaInputState.TargetInputType.TRIANGLE,
            DivaInputState.TargetInputType.CIRCLE,
            DivaInputState.TargetInputType.CROSS,
            DivaInputState.TargetInputType.SQUARE
        };

        for (int i = 0; i < types.length; i++) {
            int texIndex = (i == 1) ? 3 : (i == 3) ? 1 : i;
            DrawableSprite note = curHeld.get(texIndex);
            boolean held = curHeldData.contains(types[i]);

            float targetAlpha = held ? 1f : 0.15f;
            float targetScale = held ? 1f : 0.5f;

            note.setAlpha(MathUtils.lerp(note.getColor().a, targetAlpha, delta * 5));
            note.setScale(MathUtils.lerp(note.getScaleX(), targetScale, delta * 5));
        }

        float targetAlpha = (!curHeldData.isEmpty()) ? 1f : 0f;
        Color cTxtColor = comboText.textObj.getColor();
        float cTxtAlpha = cTxtColor.a;
        comboText.textObj.setColor(cTxtColor.r, cTxtColor.g, cTxtColor.b, MathUtils.lerp(cTxtAlpha, targetAlpha, delta * 5));

        float comboWidth = comboText.layout.width;
        float totalWidth = comboWidth;

        for (DrawableSprite note : curHeld) {
            totalWidth += note.getWidth() * note.getScaleX();
        }

        float centerX = 1280 / 2f;
        float currentX = centerX - totalWidth / 2f;

        for (DrawableSprite note : curHeld) {
            float noteWidth = note.getWidth() * note.getScaleX();
            note.setX(currentX + noteWidth / 2f);
            currentX += noteWidth;
        }
        comboText.x = currentX + 50;
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
    public void dispose()
    {
        super.dispose();
        for (DrawableSprite pressable : pressables) {
            removeSprite(pressable);
            pressable.dispose();
        }
        pressables.clear();
        pressables = null;
        for (DrawableSprite held : curHeld) {
            removeSprite(held);
            held.dispose();
        }
        curHeld.clear();
        curHeld = null;
        removeSprite(base);
        base.dispose();
        base = null;
    }
}
