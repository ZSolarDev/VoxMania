package com.zsd.voxmania.display.screen.sprite;


import com.zsd.voxmania.display.RenderedDrawable;

/**
 * A NestableSpriteRenderer which instead of drawing the child sprites on its own sprite batch, it draws it on the childs.
 * WARNING: This is lower performance than the NestableSpriteRenderer due to the constant flushing from all of the sprite batches being used!
 */
public class ComplexNestableSpriteRenderer extends NestableSpriteRenderer {
    @Override
    public void draw()
    {
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        for (RenderedDrawable drawable : sprites) {
            drawable.setRenderer(this);
            drawable.draw();
        }
        batch.end();

        for (NestableSpriteRenderer renderer : renderers)
            renderer.draw();
    }
}
