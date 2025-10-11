package com.zsd.voxmania.game.events;

import com.badlogic.gdx.audio.Music;
import com.zsd.voxmania.display.DisplayObject;
import com.zsd.voxmania.game.events.types.*;

import java.util.ArrayList;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class ESCRunner implements DisplayObject {
    public Music music;
    public EventSequenceContainer esc;
    public boolean running = false;
    public boolean paused = false;
    public ArrayList<Integer> bpms = new ArrayList<>();
    public float curFlyingTime = 1000;
    public BiConsumer<ArrayList<Float>, TargetEvent> onTargetEvent;
    public Consumer<BasicEvent> onBasicEvent;
    public ArrayList<ArrayList<EventData>> eventFramesProcessed = new ArrayList<>();

    public ESCRunner(EventSequenceContainer esc, Music music) {
        this.esc = esc;
        this.music = music;
    }

    public void run()
    {
        if (!running) {
            music.play();
            running = true;
        }
    }

    public void stop()
    {
        if (running) {
            music.stop();
            running = false;
        }
    }

    public void pause()
    {
        if (!paused) {
            music.pause();
            running = false;
            paused = true;
        }
    }

    public void resume()
    {
        if (paused) {
            music.play();
            running = true;
            paused = false;
        }
    }

    @Override
    public void update(float delta) {
        if (running && music.isPlaying())
        {
            EventFrame eventFrame = new EventFrame();
            for (Event event : esc.events)
            {
                float time = (float) event.time / 100000;
                if (time <= music.getPosition())
                    eventFrame.add(new EventData(event.getClass().getSimpleName(), event, this));
            }
            esc.procEventFrame(eventFrame);
            eventFramesProcessed.add(eventFrame);
            esc.events.removeAll(eventFrame.stream().map((eventData -> {return eventData.event;})).toList());
        }
    }

    public void processEvent(EventData eventData)
    {
        esc.procEvent(eventData);
    }

    @Override
    public void dispose() {
        music.stop();
        music.dispose();
        music = null;
        running = false;
        paused = false;
        bpms.clear();
        bpms = null;
        curFlyingTime = 0;
        esc = null;
        onTargetEvent = null;
        onBasicEvent = null;
    }
}

