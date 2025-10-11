package com.zsd.voxmania.game.events;

import com.zsd.voxmania.game.events.types.Event;

public class EventData {
    public String name;
    public Event event;
    public ESCRunner runner;

    public EventData(String name, Event event, ESCRunner runner) {
        this.name = name;
        this.event = event;
        this.runner = runner;
    }
}
