package com.zsd.voxmania.game.ui;

import com.badlogic.gdx.Gdx;

import org.luaj.vm2.Globals;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.jse.JsePlatform;

public class ScriptedUILayer extends UILayer
{
    public LuaValue onCreatedEvent;
    public LuaValue onUpdateEvent;
    public LuaValue script;

    public ScriptedUILayer(String scriptName, String scrID)
    {
        super();
        Globals globals = JsePlatform.standardGlobals();
        script = globals.load(Gdx.files.internal(scriptName).readString(), scrID);
        script.call();
        onCreatedEvent = script.get("onCreated");
        onCreatedEvent.call();
        onUpdateEvent = script.get("onUpdate");
        updateScriptVars();
    }

    @Override
    public void update(float delta)
    {
        super.update(delta);
        updateScriptVars();
        onUpdateEvent.call(LuaValue.valueOf(delta));
    }

    public void updateScriptVars()
    {
        script.set("progress", progress);
        script.set("x", position.x);
        script.set("y", position.y);
        script.set("alpha", alpha);
    }
}
