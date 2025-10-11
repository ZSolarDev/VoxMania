package com.zsd.voxmania.display.screen.drawables;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.zsd.voxmania.display.RenderedDrawable;
import com.zsd.voxmania.display.screen.sprite.SpriteRenderer;
import com.zsd.voxmania.states.StateManager;

public class DrawableSprite extends Sprite implements RenderedDrawable {
    private SpriteRenderer renderer;
    @Override
    public SpriteRenderer getRenderer() {
        return renderer;
    }

    @Override
    public void setRenderer(SpriteRenderer renderer) {
        this.renderer = renderer;
    }

    public DrawableSprite(Texture tex, SpriteRenderer renderer)
    {
        super(tex);
        this.renderer = renderer;
        getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
    }

    public DrawableSprite(SpriteRenderer renderer)
    {
        this(null, renderer);
    }

    public DrawableSprite(Texture tex)
    {
        this(tex, StateManager.currentState.defaultRenderer);
    }

    public DrawableSprite()
    {
        this(StateManager.currentState.defaultRenderer);
    }

    @Override
    public void draw() {
        if (!renderer.batch.isDrawing())
            renderer.batch.begin();
        draw(renderer.batch);
    }

    @Override
    public void update(float delta) {}

    @Override
    public void dispose() {}
}
