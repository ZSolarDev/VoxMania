package com.zsd.voxmania.game.events.scontainers;

import com.badlogic.gdx.files.FileHandle;
import com.zsd.voxmania.game.events.ESCRunner;
import com.zsd.voxmania.game.events.EventData;
import com.zsd.voxmania.game.events.EventFrame;
import com.zsd.voxmania.game.events.EventSequenceContainer;
import com.zsd.voxmania.game.events.types.*;
import com.zsd.voxmania.dscparser.Command;
import com.zsd.voxmania.dscparser.DivaScript;
import com.zsd.voxmania.dscparser.DivaScriptParser;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class DivaScriptESC extends EventSequenceContainer {
    public ArrayList<TargetEvent> sliderEventsProcessed = new ArrayList<>();

    public DivaScriptESC(DivaScript dds)
    {
        int lastTime = 0;

        for (Command cmd : dds.scriptData)
        {
            switch (cmd.type)
            {
                case 1:
                    lastTime = cmd.args.get(0);
                    break;
                case 6:
                    events.add(new TargetEvent(lastTime, cmd.args.get(0), cmd.args.get(1), cmd.args.get(2), cmd.args.get(3), cmd.args.get(4), cmd.args.get(5), cmd.args.get(6)));
                    break;
                case 28:
                    events.add(new BPMEvent(lastTime, cmd.args.get(0), cmd.args.get(1)));
                    break;
                case 58:
                    events.add(new TargetFlyingTimeEvent(lastTime, cmd.args.get(0)));
                    break;
                default:
                    events.add(new BasicEvent(lastTime, cmd.type, cmd.args));
                    break;
            }
        }
    }

    public DivaScriptESC(String path)
    {
        this(DivaScriptParser.parse(path));
    }

    public DivaScriptESC(FileHandle handle)
    {
        this(DivaScriptParser.parse(handle));
    }

    @Override
    public void procEventFrame(EventFrame frame) {
        ESCRunner runner = null;
        ArrayList<TargetEvent> tEvents = new ArrayList<>();
        for (EventData eventData : frame) {
            if (!Objects.equals(eventData.name, "TargetEvent"))
                procEvent(eventData);
            else {
                tEvents.add((TargetEvent) eventData.event);
                if (runner == null)
                    runner = eventData.runner;
            }
        }
        boolean special = false;
        if (tEvents.size() > 1)
            special = true;
        for (TargetEvent tEvent : tEvents) {
            int noteType = tEvent.noteType;
            if (noteType == 15 || noteType == 16)
                sliderEventsProcessed.add(tEvent);
            if (sliderEventsProcessed.size() == 1)
                noteType = noteType == 15 ? 12 : 13;
            float width = 1280;
            float height = 720;
            float scaleX = (width / 480);
            float scaleY = (height / 270);
            if (runner.onTargetEvent != null) {
                runner.onTargetEvent.accept(new ArrayList<Float>(List.<Float>of(
                        (float) noteType,
                        (tEvent.x / 1000) * scaleX,
                        height - (((float) tEvent.y / 1000) * scaleY),
                        (float) tEvent.angle / 1000,
                        (float) tEvent.distance / 375,
                        (float) tEvent.amplitude,
                        (float) tEvent.frequency,
                        runner.curFlyingTime,
                        (float) tEvent.time / 100000.0f,
                        (special ? 1.0f : 0.0f)
                    )), tEvent
                );
            }
        }
    }

    @Override
    public void procEvent(EventData eData) {
        String eventName = eData.name;
        Event event = eData.event;
        ESCRunner runner = eData.runner;
        switch (eventName)
        {
            case "BasicEvent":
                if (runner.onBasicEvent != null)
                    runner.onBasicEvent.accept((BasicEvent) event);
                break;
            case "BPMEvent":
                BPMEvent bpmEvent = (BPMEvent) event;
                runner.bpms.add(bpmEvent.bpm);
                runner.curFlyingTime = 1000 / ((float) bpmEvent.bpm / ((bpmEvent.timeSig + 1) * 60));
                break;
            case "TargetFlyingTimeEvent":
                runner.curFlyingTime = (float) ((TargetFlyingTimeEvent) event).flyingTime / 1000;
                break;
            case "TargetEvent":
                TargetEvent tEvent = (TargetEvent) event;

                break;
            case "TargetHitEvent":
                TargetHitEvent targetEvent = (TargetHitEvent)event;
                TargetEvent target = targetEvent.target.event;
                sliderEventsProcessed.remove(target);
                break;
        }
    }
}
