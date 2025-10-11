package com.zsd.voxmania.game.events;

import com.zsd.voxmania.game.events.types.Event;
import java.util.ArrayList;

public class EventSequenceContainer {
    public ArrayList<Event> events = new ArrayList<>();
    public void procEventFrame(EventFrame eFrame) {}
    public void procEvent(EventData eData) {}
}
