package com.zsd.voxmania.game.ui.objects.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Align;
import com.zsd.voxmania.display.screen.drawables.DrawableSprite;
import com.zsd.voxmania.display.screen.drawables.DrawableTTFText;
import com.zsd.voxmania.game.DivaInputState;
import com.zsd.voxmania.game.ui.UILayer;

import java.util.ArrayList;

public class TargetHoldOverlay extends UILayer {
    public ArrayList<DrawableSprite> curHeld = new ArrayList<>();
    public ArrayList<DivaInputState.TargetInputType> curHeldData = new ArrayList<>();
    public DrawableTTFText holdScoreText;
    public DrawableTTFText bonusHoldScoreText;
    public boolean showText;
    public boolean showBonusText;

    public TargetHoldOverlay()
    {
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

        holdScoreText = new DrawableTTFText(Gdx.files.internal("game/fonts/score.ttf"), 30, Color.WHITE, 0, 160, "+0", 0, Align.left, false, 5, Color.BLACK);
        addDrawable(holdScoreText);
        Color sTxtColor = holdScoreText.textObj.getColor();
        holdScoreText.textObj.setColor(sTxtColor.r, sTxtColor.g, sTxtColor.b, 0);
        bonusHoldScoreText = new DrawableTTFText(Gdx.files.internal("game/fonts/score.ttf"), 20, new Color(0x00FFAAFF), 900, 180, "+0", 0, Align.left, false, 5, new Color(0x008800FF));
        addDrawable(bonusHoldScoreText);
        Color bsTxtColor = bonusHoldScoreText.textObj.getColor();
        bonusHoldScoreText.textObj.setColor(bsTxtColor.r, bsTxtColor.g, bsTxtColor.b, 0);
    }

    float baseTotalWidth = 0;
    @Override
    public void update(float delta) {
        super.update(delta);
        DivaInputState.TargetInputType[] types = {
            DivaInputState.TargetInputType.TRIANGLE,
            DivaInputState.TargetInputType.CIRCLE,
            DivaInputState.TargetInputType.CROSS,
            DivaInputState.TargetInputType.SQUARE
        };

        holdScoreText.x = holdScoreText.x - position.x;
        holdScoreText.y = holdScoreText.y - position.y;
        for (DrawableSprite note : curHeld) {
            note.setX(note.getX() - position.x);
            note.setY(note.getY() - position.y);
        }

        for (int i = 0; i < types.length; i++) {
            int texIndex = (i == 1) ? 3 : (i == 3) ? 1 : i;
            DrawableSprite note = curHeld.get(texIndex);
            boolean held = curHeldData.contains(types[i]);

            float targetAlpha = held ? 1f : 0.15f;
            float targetScale = held ? 1f : 0.5f;

            note.setAlpha(MathUtils.lerp(note.getColor().a, targetAlpha, delta * 5));
            note.setScale(MathUtils.lerp(note.getScaleX(), targetScale, delta * 5));
        }


        float targetAlpha = (!curHeldData.isEmpty() && showText) ? 1f : 0f;
        Color sTxtColor = holdScoreText.textObj.getColor();
        float sTxtAlpha = sTxtColor.a;
        holdScoreText.textObj.setColor(sTxtColor.r, sTxtColor.g, sTxtColor.b, MathUtils.lerp(sTxtAlpha, targetAlpha, delta * 5));

        float bstargetAlpha = (!curHeldData.isEmpty() && showBonusText) ? 1f : 0f;
        Color bsTxtColor = bonusHoldScoreText.textObj.getColor();
        float bsTxtAlpha = bsTxtColor.a;
        bonusHoldScoreText.textObj.setColor(bsTxtColor.r, bsTxtColor.g, bsTxtColor.b, MathUtils.lerp(bsTxtAlpha, bstargetAlpha, delta * 5));

        float comboWidth = holdScoreText.layout.width;
        baseTotalWidth = MathUtils.lerp(baseTotalWidth, showText ? comboWidth : 0, delta * 5);
        float totalWidth = baseTotalWidth;

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
        holdScoreText.x = currentX + 50;


        holdScoreText.x = holdScoreText.x + position.x;
        holdScoreText.y = holdScoreText.y + position.y;
        for (DrawableSprite note : curHeld) {
            note.setX(note.getX() + position.x);
            note.setY(note.getY() + position.y);
        }
    }

    @Override
    public void dispose()
    {
        super.dispose();
        super.dispose();
        for (DrawableSprite held : curHeld) {
            removeSprite(held);
            held.dispose();
        }
        curHeld.clear();
        curHeld = null;
    }
}
