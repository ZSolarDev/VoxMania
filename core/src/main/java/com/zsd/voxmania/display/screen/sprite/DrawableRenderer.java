package com.zsd.voxmania.display.screen.sprite;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.zsd.voxmania.Main;
import com.zsd.voxmania.display.RenderedDrawable;

import java.util.ArrayList;

public class DrawableRenderer implements RenderedDrawable {
    public SpriteBatch batch;
    public ArrayList<RenderedDrawable> sprites = new ArrayList<>();
    public ArrayList<RenderedDrawable> disposeQueue = new ArrayList<>();
    public Camera camera = Main.camera;
    public boolean updateChildren = false;

    public DrawableRenderer(boolean updateChildren)
    {
        this.updateChildren = updateChildren;
    }

    public DrawableRenderer()
    {
        this(false);
    }

    @Override
    public void update(float delta)
    {
        for (RenderedDrawable spr : disposeQueue)
            sprites.remove(spr);
        disposeQueue.clear();
        if (updateChildren)
        {
            for (RenderedDrawable spr : sprites)
                spr.update(delta);
        }
    }

    @Override
    public void dispose() {
        batch.dispose();
        batch = null;
        sprites.clear();
        sprites = null;
        camera = null;
    }

    @Override
    public DrawableRenderer getRenderer() {
        return null;
    }

    @Override
    public void setRenderer(DrawableRenderer renderer) {}

    @Override
    public void draw()
    {
        if (batch == null)
            batch = new SpriteBatch();
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        for (RenderedDrawable spr : sprites){
            spr.setRenderer(this);
            spr.draw();
        }
        batch.end();
    }

    public void addDrawable(RenderedDrawable spr)
    {
        sprites.add(spr);
    }

    public void addDrawable(RenderedDrawable spr, int idx)
    {
        sprites.add(idx, spr);
    }

    public void removeSprite(RenderedDrawable spr)
    {
        sprites.remove(spr);
    }

    public void queueDispose(RenderedDrawable spr)
    {
        disposeQueue.add(spr);
    }

    public void removeSprite(int idx)
    {
        sprites.remove(idx);
    }
}
