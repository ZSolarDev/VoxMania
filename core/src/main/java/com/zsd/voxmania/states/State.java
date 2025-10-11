package com.zsd.voxmania.states;

import com.zsd.voxmania.display.screen.CanvasScreen;
import com.zsd.voxmania.display.screen.sprite.SpriteRenderer;

public class State extends CanvasScreen {
    public SpriteRenderer defaultRenderer = new SpriteRenderer(true);
    @Override
    public void draw(float delta)
    {
        update(delta);
        super.draw(delta);
    }

    public void update(float delta) {}

    public void create() {}
}
