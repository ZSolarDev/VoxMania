package com.zsd.voxmania.game.ui;

import com.badlogic.gdx.math.Vector2;
import com.zsd.voxmania.display.screen.sprite.NestableDrawableRenderer;

public class UILayer extends NestableDrawableRenderer
{
    public Vector2 position = new Vector2();
    public float alpha;
    public float progress;
    public boolean animated;

    public UILayer()
    {
        super(true);
    }
}
