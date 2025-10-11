package com.zsd.voxmania.display.screen.drawables;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.zsd.voxmania.display.RenderedDrawable;
import com.zsd.voxmania.display.screen.sprite.SpriteRenderer;

public class DrawableText implements RenderedDrawable {
    public BitmapFont font;
    float x, y;

    // 36, 2
    public DrawableText(FileHandle fontFile, int size, Color color, float x, float y, int borderWidth, Color borderColor, SpriteRenderer renderer)
    {
        this.renderer = renderer;
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(fontFile);
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = size;
        parameter.color = color;
        parameter.borderWidth = borderWidth;
        parameter.borderColor = borderColor;
        font = generator.generateFont(parameter);
        generator.dispose();
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
        //font.draw(renderer.batch);
    }

    @Override
    public void update(float delta) {}

    @Override
    public void dispose() {}
}
