package com.zsd.voxmania.display.screen.sprite;


import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.zsd.voxmania.display.RenderedDrawable;

/**
 * A NestableDrawableRenderer which instead of drawing the child sprites on its own sprite batch, it draws it on the childs.
 * WARNING: This is lower performance than the NestableDrawableRenderer due to the constant flushing from all of the sprite batches being used!
 */
public class ComplexNestableDrawableRenderer extends NestableDrawableRenderer {
    @Override
    public void draw()
    {
        for (NestableDrawableRenderer renderer : renderers)
            renderer.draw();
        if (batch == null)
            batch = new SpriteBatch();
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        for (RenderedDrawable drawable : sprites) {
            drawable.setRenderer(this);
            drawable.draw();
        }
        batch.end();
    }
}
