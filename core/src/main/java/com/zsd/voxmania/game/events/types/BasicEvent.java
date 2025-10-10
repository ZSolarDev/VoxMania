package com.zsd.voxmania.game.events.types;

import java.util.ArrayList;

public class BasicEvent implements Event {
    int type;
    int time;
    ArrayList<Integer> params = new ArrayList<>();
    public BasicEvent(int time, int type, ArrayList<Integer> params) {
        this.time = time;
        this.type = type;
        this.params = params;
    };
}
