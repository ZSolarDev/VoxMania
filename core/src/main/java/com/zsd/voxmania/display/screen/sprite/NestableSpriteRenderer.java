package com.zsd.voxmania.display.screen.sprite;
import com.badlogic.gdx.graphics.g2d.Sprite;
import java.util.ArrayList;

public class NestableSpriteRenderer extends SpriteRenderer {
    public ArrayList<NestableSpriteRenderer> renderers = new ArrayList<>();

    @Override
    public void draw()
    {
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        for (NestableSpriteRenderer renderer : renderers)
            drawRenderer(renderer);
        for (Sprite spr : sprites)
            spr.draw(batch);
        batch.end();
    }

    public void drawRenderer(NestableSpriteRenderer renderer)
    {
        for (Sprite spr : renderer.sprites)
            spr.draw(batch);
        for (NestableSpriteRenderer csr : renderer.renderers)
            drawRenderer(csr);
    }

    public void addRenderer(NestableSpriteRenderer renderer)
    {
        renderers.add(renderer);
    }

    public void addRenderer(NestableSpriteRenderer renderer, int idx)
    {
        renderers.add(idx, renderer);
    }

    public void removeRenderer(NestableSpriteRenderer renderer)
    {
        renderers.remove(renderer);
    }

    public void removeRenderer(int idx)
    {
        renderers.remove(idx);
    }
}
