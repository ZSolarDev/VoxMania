package com.zsd.voxmania.states;

import com.zsd.voxmania.display.screen.CanvasScreen;

public class State extends CanvasScreen {
    @Override
    public void draw(float delta)
    {
        update(delta);
        super.draw(delta);
    }

    public void update(float delta) {}

    public void create() {}
}
