package com.zsd.voxmania.display.screen.drawables;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.zsd.voxmania.display.RenderedDrawable;
import com.zsd.voxmania.display.screen.sprite.SpriteRenderer;

public class DrawableText implements RenderedDrawable {
    public BitmapFont textObj;
    public String text;
    public float targetWidth;
    public int halign;
    public boolean wrap;
    public float x, y;

    public DrawableText(FileHandle fontFile, float size, Color color, float x, float y, String text, float targetWidth, int halign, boolean wrap)
    {
        this.x = x;
        this.y = y;
        this.text = text;
        this.targetWidth = targetWidth;
        this.halign = halign;
        this.wrap = wrap;
        if (fontFile != null)
            textObj = new BitmapFont(fontFile);
        else
            textObj = new BitmapFont();
        textObj.setColor(color);
        textObj.getData().setScale(size);
    }

    public DrawableText(FileHandle fontFile, float size, Color color, float x, float y, String text)
    {
        this(fontFile, size, color, x, y, text, 0, 0, false);
    }

    private SpriteRenderer renderer;
    @Override
    public SpriteRenderer getRenderer() {
        return renderer;
    }

    @Override
    public void setRenderer(SpriteRenderer renderer) {
        this.renderer = renderer;
    }

    @Override
    public void draw() {
        textObj.draw(renderer.batch, text, x, y, targetWidth, halign, wrap);
    }

    @Override
    public void update(float delta) {}

    @Override
    public void dispose() {}
}
