package com.zsd.voxmania.display;

import com.zsd.voxmania.display.screen.sprite.SpriteRenderer;

public interface RenderedDrawable extends Drawable{
    SpriteRenderer getRenderer();
    void setRenderer(SpriteRenderer renderer);
}
