package com.zsd.voxmania.display.screen.sprite;

import com.badlogic.gdx.graphics.g2d.Sprite;

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
        for (Sprite spr : sprites)
            spr.draw(batch);
        batch.end();

        for (NestableSpriteRenderer renderer : renderers)
            renderer.draw();
    }
}
