package com.zsd.voxmania;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.zsd.voxmania.game.Game;
import com.zsd.voxmania.game.ui.UIManager;
import com.zsd.voxmania.game.ui.objects.game.GameOverlay;
import com.zsd.voxmania.game.ui.objects.game.MobileOverlay;
import com.zsd.voxmania.game.ui.objects.game.TargetHoldOverlay;
import com.zsd.voxmania.game.ui.objects.game.TargetUI;
import com.zsd.voxmania.states.GameStateEntrypoint;
import com.zsd.voxmania.states.StateManager;

public class Main extends GameStateEntrypoint {
    @Override
    public void create() {
        super.create();
        if (Gdx.app.getType() == Application.ApplicationType.Android) {
            FileHandle externalMods = Gdx.files.external("VoxMania/mods");
            if (!externalMods.exists()) externalMods.mkdirs();

            FileHandle internalMods = Gdx.files.internal("mods");
            System.out.println(Gdx.files.internal("mods/TestMod/World Is Mine ExEx.dsc").exists());
            if (internalMods.exists() && internalMods.isDirectory()) {
                for (FileHandle mod : internalMods.list()) {
                    FileHandle target = externalMods.child(mod.name());
                    if (!target.exists()) mod.copyTo(target);
                }
            }
        }
        registerUI();
        StateManager.switchState(Game.class);
    }

    public void registerUI()
    {
        UIManager.registerUILayer("game.TargetUI", TargetUI.class);
        UIManager.registerUILayer("game.GameOverlay", GameOverlay.class);
        UIManager.registerUILayer("game.MobileOverlay", MobileOverlay.class);
        UIManager.registerUILayer("game.TargetHoldOverlay", TargetHoldOverlay.class);
    }
}
