package com.zsd.voxmania.display;

public interface Drawable extends DisplayObject{
    public void draw();
    public void update(float delta);
    public void dispose();
}
