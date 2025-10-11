package com.zsd.voxmania.display.screen;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.utils.ScreenUtils;
import com.zsd.voxmania.display.DisplayObject;
import com.zsd.voxmania.display.RenderedDrawable;

public class CanvasScreen extends ObjectMGRScreen {
    public Color backgroundColor = Color.BLACK;

    public CanvasScreen() {}

    @Override
    public void draw(float delta) {
        super.draw(delta);
        ScreenUtils.clear(backgroundColor);
        for (DisplayObject object : objects){
            if (object instanceof RenderedDrawable)
                ((RenderedDrawable) object).draw();
        }
    }
}
