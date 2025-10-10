package com.zsd.voxmania.display.screen;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.utils.ScreenUtils;
import com.zsd.voxmania.display.DisplayObject;
import com.zsd.voxmania.display.Drawable;

import java.util.ArrayList;

public class ObjectMGRScreen extends BaseScreen {
    ArrayList<DisplayObject> objects = new ArrayList<>();

    public ObjectMGRScreen() {}

    public void addObject(DisplayObject obj, int index) {
        objects.add(index, obj);
    }

    public void addObject(DisplayObject obj) {
        objects.add(obj);
    }

    public void removeObject(int index) {
        objects.remove(index);
    }

    public void removeObject(DisplayObject obj) {
        objects.remove(obj);
    }

    public void clearObjects() {
        objects.clear();
    }

    public void draw(float delta) {
        for (DisplayObject displayObj : objects)
            displayObj.update(delta);
    }
}
