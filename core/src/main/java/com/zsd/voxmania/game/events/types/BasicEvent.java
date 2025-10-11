package com.zsd.voxmania.game.events.types;

import java.util.ArrayList;

public class BasicEvent extends Event {
    public int type;
    public ArrayList<Integer> params;
    public BasicEvent(int time, int type, ArrayList<Integer> params) {
        this.time = time;
        this.type = type;
        this.params = params;
    };
}
