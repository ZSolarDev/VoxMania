package com.zsd.voxmania.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.zsd.voxmania.states.State;


public class Game extends State {
    GameUI ui;
    @Override
    public void create() {
        backgroundColor = Color.GRAY;
        ui = new GameUI();
        ui.addNote(new Note());
        addObject(ui);
        Music music = Gdx.audio.newMusic(Gdx.files.internal("mods/TestMod/World Is Mine.ogg"));
        music.play();
    }

    @Override
    public void draw(float delta)
    {
        super.draw(delta);
    }
}
