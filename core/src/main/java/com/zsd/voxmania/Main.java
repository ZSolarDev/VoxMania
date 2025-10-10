package com.zsd.voxmania;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.zsd.voxmania.game.Game;
import com.zsd.voxmania.states.GameStateEntrypoint;
import com.zsd.voxmania.states.StateManager;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class Main extends GameStateEntrypoint {
    @Override
    public void create() {
        super.create();
        if (Gdx.app.getType() == Application.ApplicationType.Android) {
            FileHandle externalMods = Gdx.files.external("VoxMania/mods");
            if (!externalMods.exists()) externalMods.mkdirs();

            FileHandle internalMods = Gdx.files.internal("mods");
            if (internalMods.exists() && internalMods.isDirectory()) {
                for (FileHandle mod : internalMods.list()) {
                    FileHandle target = externalMods.child(mod.name());
                    if (!target.exists()) mod.copyTo(target);
                }
            }
            Gdx.app.log("ModsPath", Gdx.files.external("VoxMania/mods").path());
        }
        StateManager.switchState(Game.class);
    }
}
