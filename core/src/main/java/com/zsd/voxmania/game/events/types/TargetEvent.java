package com.zsd.voxmania.game.events.types;

public class TargetEvent implements Event {
    public int time = 0;
    public int noteType = 0;
    public int x = 0;
    public int y = 0;
    public int angle = 0;
    public int distance = 0;
    public int amplitude = 0;
    public int frequency = 0;

    public TargetEvent(int time, int noteType, int x, int y, int angle, int distance, int amplitude, int frequency)
    {
        this.time = time;
        this.noteType = noteType;
        this.x = x;
        this.y = y;
        this.angle = angle;
        this.distance = distance;
        this.amplitude = amplitude;
        this.frequency = frequency;
    }
}
