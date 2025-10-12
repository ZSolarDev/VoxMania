package com.zsd.voxmania.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.utils.Align;
import com.zsd.voxmania.display.screen.drawables.DrawableTTFText;
import com.zsd.voxmania.display.screen.sprite.NestableDrawableRenderer;

import java.util.ArrayList;

public class GameUI extends NestableDrawableRenderer {
    public ArrayList<Target> targets;
    public NestableDrawableRenderer targetRenderer = new NestableDrawableRenderer(true);
    public ArrayList<Target> targetDisposeQueue = new ArrayList<>();
    public GameOverlay overlay = new GameOverlay();
    public DrawableTTFText fpsDisplay;

    public GameUI(ArrayList<Target> targets)
    {
        super(true);
        this.targets = targets;
        addRenderer(targetRenderer);
        addRenderer(overlay);
        fpsDisplay = new DrawableTTFText(Gdx.files.internal("game/fonts/normal_filled.ttf"), 40, Color.WHITE, 40, 700, "FPS: ???", 0, Align.left, false);
        addDrawable(fpsDisplay);
    }

    public void addNote(Target target)
    {
        targets.add(target);
    }

    public void addNote(Target target, int idx)
    {
        targets.add(idx, target);
    }

    public void removeTarget(Target target)
    {
        targets.remove(target);
        try{
            target.dispose();
        }catch (Exception e)
        {
            System.out.println("Failed to dispose target!");
        }
    }

    public void queueRemoveTarget(Target target)
    {
        targetDisposeQueue.add(target);
    }

    public void removeTarget(int idx)
    {
        targets.get(idx).dispose();
        targets.remove(idx);
    }

    @Override
    public void update(float delta) {
        super.update(delta);
        for (Target target : targetDisposeQueue)
            targets.remove(target);
        targetDisposeQueue.clear();
        fpsDisplay.text = "FPS: " + Gdx.graphics.getFramesPerSecond();
    }

    public GameUI()
    {
        this(new ArrayList<Target>());
    }
}
