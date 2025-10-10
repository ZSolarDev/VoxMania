package com.zsd.voxmania.game.events.types;

public class BPMEvent implements Event {
    public int time = 0;
    public int bpm = 0;
    public int timeSig = 0;
    public BPMEvent(int time, int bpm, int timeSig)
    {
        this.time = time;
        this.bpm = bpm;
        this.timeSig = timeSig;
    }
}
