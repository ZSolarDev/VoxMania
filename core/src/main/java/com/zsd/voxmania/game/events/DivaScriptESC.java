package com.zsd.voxmania.game.events;

import com.zsd.voxmania.game.events.types.*;
import com.zsd.voxmania.dscparser.Command;
import com.zsd.voxmania.dscparser.DivaScript;
import com.zsd.voxmania.dscparser.DivaScriptParser;

import java.util.ArrayList;

public class DivaScriptESC implements EventSequenceContainer {
    ArrayList<Event> events = new ArrayList<>();
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
}
