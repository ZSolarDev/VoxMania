package com.zsd.voxmania.game.events.types;

public class TargetFlyingTimeEvent extends Event {
    public int flyingTime = 0;
    public TargetFlyingTimeEvent(int time, int flyingTime)
    {
        this.time = time;
        this.flyingTime = flyingTime;
    }
}
