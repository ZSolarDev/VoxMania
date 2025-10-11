package com.zsd.voxmania.display.screen.drawables;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.zsd.voxmania.display.RenderedDrawable;
import com.zsd.voxmania.display.screen.sprite.SpriteRenderer;

public class DrawableTTFText extends DrawableText implements RenderedDrawable {
    public DrawableTTFText(FileHandle fontFile, int size, Color color, float x, float y, String text, float targetWidth, int halign, boolean wrap, float borderWidth, Color borderColor)
    {
        super(null, size, color, x, y, text, targetWidth, halign, wrap);
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(fontFile);
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = size;
        parameter.color = color;
        parameter.borderWidth = borderWidth;
        parameter.borderColor = borderColor;
        textObj = generator.generateFont(parameter);
        generator.dispose();
    }

    public DrawableTTFText(FileHandle fontFile, int size, Color color, float x, float y, String text, float targetWidth, int halign, boolean wrap)
    {
        this(fontFile, size, color, x, y, text, targetWidth, halign, wrap, 0, Color.BLACK);
    }


    public DrawableTTFText(FileHandle fontFile, int size, Color color, float x, float y, String text, float borderWidth, Color borderColor)
    {
        this(fontFile, size, color, x, y, text, 0, 0, false, borderWidth, borderColor);
    }

    public DrawableTTFText(FileHandle fontFile, int size, Color color, float x, float y, String text)
    {
        this(fontFile, size, color, x, y, text, 0, 0, false, 0, Color.BLACK);
    }
}
