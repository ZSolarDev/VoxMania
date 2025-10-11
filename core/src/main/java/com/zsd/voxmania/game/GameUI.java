package com.zsd.voxmania.game;

import com.zsd.voxmania.display.screen.sprite.NestableSpriteRenderer;

import java.util.ArrayList;

public class GameUI extends NestableSpriteRenderer {
    public ArrayList<Target> targets;
    public NestableSpriteRenderer targetRenderer = new NestableSpriteRenderer(true);
    public ArrayList<Target> targetDisposeQueue = new ArrayList<>();
    public GameOverlay overlay = new GameOverlay();

    public GameUI(ArrayList<Target> targets)
    {
        super(true);
        this.targets = targets;
        addRenderer(targetRenderer);
        addRenderer(overlay);
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
    }

    public GameUI()
    {
        this(new ArrayList<Target>());
    }
}
