package com.zsd.voxmania.game.ui.objects.game;

import com.zsd.voxmania.display.screen.sprite.NestableDrawableRenderer;
import com.zsd.voxmania.game.Target;
import com.zsd.voxmania.game.ui.UILayer;

import java.util.ArrayList;

public class TargetUI extends UILayer {
    public ArrayList<Target> targets;
    public NestableDrawableRenderer targetRenderer = new NestableDrawableRenderer(true);
    public ArrayList<Target> targetDisposeQueue = new ArrayList<>();

    public TargetUI(ArrayList<Target> targets)
    {
        super();
        this.targets = targets;
        addRenderer(targetRenderer);
    }

    public TargetUI()
    {
        this(new ArrayList<Target>());
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
}
