package com.zsd.voxmania.game.events.types;

public class TargetFlyingTimeEvent implements Event {
    public int time = 0;
    public int flyingTime = 0;
    public TargetFlyingTimeEvent(int time, int flyingTime)
    {
        this.time = time;
        this.flyingTime = flyingTime;
    }
}
