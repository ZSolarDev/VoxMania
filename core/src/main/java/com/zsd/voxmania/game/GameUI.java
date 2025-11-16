package com.zsd.voxmania.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.utils.Align;
import com.zsd.voxmania.display.screen.drawables.DrawableTTFText;
import com.zsd.voxmania.display.screen.sprite.NestableDrawableRenderer;
import com.zsd.voxmania.game.ui.UIManager;
import com.zsd.voxmania.game.ui.objects.game.GameOverlay;
import com.zsd.voxmania.game.ui.objects.game.TargetUI;

import java.util.ArrayList;

public class GameUI extends NestableDrawableRenderer {
    public TargetUI targetRenderer;
    public GameOverlay overlay;
    public DrawableTTFText fpsDisplay;

    public GameUI(ArrayList<Target> targets)
    {
        super(true);
        targetRenderer = (TargetUI) UIManager.createUILayer("game.TargetUI", TargetUI.class, targets);
        addRenderer(targetRenderer);
        overlay = (GameOverlay) UIManager.createUILayer("game.GameOverlay", GameOverlay.class);
        addRenderer(overlay);
        fpsDisplay = new DrawableTTFText(Gdx.files.internal("game/fonts/normal_filled.ttf"), 40, Color.WHITE, 40, 700, "FPS: ???", 0, Align.left, false);
        addDrawable(fpsDisplay);
    }

    public GameUI()
    {
        this(new ArrayList<Target>());
    }

    @Override
    public void update(float delta) {
        super.update(delta);
        fpsDisplay.text = "FPS: " + Gdx.graphics.getFramesPerSecond();
    }
}
