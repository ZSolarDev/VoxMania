package com.zsd.voxmania.states;

import com.zsd.voxmania.display.screen.CanvasScreen;
import com.zsd.voxmania.display.screen.sprite.NestableSpriteRenderer;

public class State extends CanvasScreen {
    public NestableSpriteRenderer defaultRenderer = new NestableSpriteRenderer(true);
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
