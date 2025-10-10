package com.zsd.voxmania.display.screen.sprite;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.zsd.voxmania.Main;
import com.zsd.voxmania.display.Drawable;

import java.util.ArrayList;

public class SpriteRenderer extends Drawable {
    public SpriteBatch batch = new SpriteBatch();
    public ArrayList<Sprite> sprites = new ArrayList<>();
    public Camera camera = Main.camera;

    public SpriteRenderer()
    {
        batch = new SpriteBatch();
    }

    @Override
    public void draw()
    {
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        for (Sprite spr : sprites)
            spr.draw(batch);
        batch.end();
    }

    public void addSprite(Sprite spr)
    {
        sprites.add(spr);
    }

    public void addSprite(Sprite spr, int idx)
    {
        sprites.add(idx, spr);
    }

    public void removeSprite(Sprite spr)
    {
        sprites.remove(spr);
    }

    public void removeSprite(int idx)
    {
        sprites.remove(idx);
    }
}
