package com.zsd.voxmania.display.screen.sprite;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.zsd.voxmania.display.RenderedDrawable;

import java.util.ArrayList;

public class NestableDrawableRenderer extends DrawableRenderer {
    public ArrayList<NestableDrawableRenderer> renderers = new ArrayList<>();

    public NestableDrawableRenderer(boolean updateChildren)
    {
        super(updateChildren);
    }

    public NestableDrawableRenderer()
    {
        this(false);
    }

    @Override
    public void draw()
    {
        if (batch == null)
            batch = new SpriteBatch();
        batch.setProjectionMatrix(camera.combined);
        if (!batch.isDrawing())
            batch.begin();
        for (NestableDrawableRenderer renderer : renderers)
            drawRenderer(renderer);
        for (RenderedDrawable drawable : sprites) {
            drawable.setRenderer(this);
            drawable.draw();
        }
        batch.end();
    }
    @Override
    public void update(float delta)
    {
        super.update(delta);
        if (updateChildren)
            for (NestableDrawableRenderer renderer : renderers)
                renderer.update(delta);
    }

    public void drawRenderer(NestableDrawableRenderer renderer)
    {
        for (RenderedDrawable drawable : renderer.sprites) {
            drawable.setRenderer(this);
            drawable.draw();
        }
        for (NestableDrawableRenderer csr : renderer.renderers)
            drawRenderer(csr);
    }

    public void addRenderer(NestableDrawableRenderer renderer)
    {
        renderers.add(renderer);
    }

    public void addRenderer(NestableDrawableRenderer renderer, int idx)
    {
        renderers.add(idx, renderer);
    }

    public void removeRenderer(NestableDrawableRenderer renderer)
    {
        renderers.remove(renderer);
    }

    public void removeRenderer(int idx)
    {
        renderers.remove(idx);
    }
}
