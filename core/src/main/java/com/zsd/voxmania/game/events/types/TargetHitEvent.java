package com.zsd.voxmania.game.events.types;

import com.zsd.voxmania.game.Target;

public class TargetHitEvent extends Event {
    public int time = 0;
    public Target target;

    public TargetHitEvent(Target target)
    {
        this.target = target;
    }
}
