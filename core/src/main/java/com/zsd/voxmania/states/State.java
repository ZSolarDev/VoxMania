package com.zsd.voxmania.states;

import com.zsd.voxmania.display.screen.CanvasScreen;
import com.zsd.voxmania.display.screen.sprite.NestableDrawableRenderer;

public class State extends CanvasScreen {
    public NestableDrawableRenderer defaultRenderer = new NestableDrawableRenderer(true);
    @Override
    public void draw(float delta)
    {
        update(delta);
        super.draw(delta);
    }

    public void update(float delta) {}

    public void create() {
        addObject(defaultRenderer);
    }
}
