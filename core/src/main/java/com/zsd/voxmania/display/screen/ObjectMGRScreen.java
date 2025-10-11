package com.zsd.voxmania.display.screen;

import com.zsd.voxmania.display.DisplayObject;

import java.util.ArrayList;

public class ObjectMGRScreen extends BaseScreen {
    public ArrayList<DisplayObject> objects = new ArrayList<>();

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
