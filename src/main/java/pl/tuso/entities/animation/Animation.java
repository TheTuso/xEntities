package pl.tuso.entities.animation;

import com.google.gson.Gson;
import pl.tuso.entities.Entities;

import java.util.HashMap;

public class Animation { // I hope you don't want to make the animations by hand. Use xAnimator and Blender!
    private static final Gson gson = new Gson();
    private final int frames;
    private final HashMap<String, Frame[]> entityParts;

    public Animation(int frames, HashMap<String, Frame[]> entityParts) {
        this.frames = frames;
        this.entityParts = entityParts;
    }

    public int getFrames() {
        return this.frames;
    }

    public Frame[] getEntityPartFrames(String name) {
        return this.entityParts.get(name);
    }

    public static Animation load(String name) {
        Animation animation;
        try {
            String serialized = new String(Entities.getPlugin(Entities.class).getResource(name + ".json").readAllBytes());
            animation = gson.fromJson(serialized, Animation.class);
        } catch (Exception exception) {
            animation = null;
        }
        return animation;
    }
}
