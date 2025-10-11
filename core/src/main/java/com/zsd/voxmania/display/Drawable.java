package com.zsd.voxmania.display;

import com.zsd.voxmania.display.screen.sprite.SpriteRenderer;

public interface Drawable extends DisplayObject{
    public void draw();
    public void update(float delta);
    public void dispose();
}
