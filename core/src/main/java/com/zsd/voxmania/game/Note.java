package com.zsd.voxmania.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.zsd.voxmania.display.screen.sprite.NestableSpriteRenderer;

public class Note extends NestableSpriteRenderer {
    public Sprite note;
    public Note(String type, boolean isTarget)
    {
        note = new Sprite(new Texture(Gdx.files.internal("game/notes/" + (isTarget ? "target/" : "normal/") + type + ".png")));
        addSprite(note);
        note.getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);

    }

    public Note(String type)
    {
        this(type, false);
    }

    public Note()
    {
        this("Circle", false);
    }
}
