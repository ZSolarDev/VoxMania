package com.zsd.voxmania.display.screen.sprite;

import com.zsd.voxmania.display.RenderedDrawable;

import java.util.ArrayList;

public class NestableSpriteRenderer extends SpriteRenderer {
    public ArrayList<NestableSpriteRenderer> renderers = new ArrayList<>();

    public NestableSpriteRenderer(boolean updateChildren)
    {
        super(updateChildren);
    }

    public NestableSpriteRenderer()
    {
        this(false);
    }

    @Override
    public void draw()
    {
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        for (RenderedDrawable drawable : sprites) {
            drawable.setRenderer(this);
            drawable.draw();
        }for (NestableSpriteRenderer renderer : renderers)
            drawRenderer(renderer);
        batch.end();
    }
    @Override
    public void update(float delta)
    {
        super.update(delta);
        if (updateChildren)
            for (NestableSpriteRenderer renderer : renderers)
                renderer.update(delta);
    }

    public void drawRenderer(NestableSpriteRenderer renderer)
    {
        for (RenderedDrawable drawable : renderer.sprites) {
            drawable.setRenderer(this);
            drawable.draw();
        }
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
