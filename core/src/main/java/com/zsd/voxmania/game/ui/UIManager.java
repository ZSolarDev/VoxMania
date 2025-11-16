package com.zsd.voxmania.game.ui;

import com.zsd.voxmania.game.Target;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class UIManager {
    private static Map<String, Class<? extends UILayer>> uiLayers = new HashMap<>();

    public static boolean UILayerIsRegistered(String id) {
        return uiLayers.containsKey(id);
    }
    public static void registerUILayer(String id, Class<? extends UILayer> uiLayer) {
        uiLayers.put(id, uiLayer);
    }

    public static void unregisterUILayer(String id, Class<? extends UILayer> uiLayer) {
        uiLayers.put(id, uiLayer);
    }

    public static Object createUILayer(String id) {
        return createUILayer(id, new Object[]{});
    }

    public static Object createUILayer(String id, Object... params) {
        return createUILayer(id, UILayer.class, params);
    }

    public static Object createUILayer(String id, Class<? extends UILayer> expectedLayer, Object... params) {
        try {
            if (!UILayerIsRegistered(id)) {
                throw new RuntimeException("UILayer with id " + id + " was not found, was it registered?");
            }
            Class<? extends UILayer> uiLayer = uiLayers.get(id);

            Constructor<?>[] constructors = uiLayer.getDeclaredConstructors();
            for (Constructor<?> c : constructors) {
                Class<?>[] types = c.getParameterTypes();
                if (types.length != params.length) continue;

                boolean match = true;
                for (int i = 0; i < types.length; i++) {
                    if (!types[i].isAssignableFrom(params[i].getClass())) {
                        match = false;
                        break;
                    }
                }

                if (match) {
                    return expectedLayer.cast(c.newInstance(params));
                }
            }

            throw new RuntimeException("No matching constructor found for UILayer with id " + id + "! Were your parameters correct?");
        } catch (InstantiationException | IllegalAccessException |
                 InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }
}
