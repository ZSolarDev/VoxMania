package com.zsd.voxmania.display;

import com.zsd.voxmania.display.screen.sprite.DrawableRenderer;

public interface RenderedDrawable extends Drawable{
    DrawableRenderer getRenderer();
    void setRenderer(DrawableRenderer renderer);
}
