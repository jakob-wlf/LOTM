package de.firecreeper82.handlers.mobs;

import org.bukkit.entity.Entity;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.HashMap;

public class BeyonderMobs {


    private final HashMap<Entity, String> mobs = new HashMap<>();

    public BeyonderMobs() {

    }

    public void addMob(@NonNull Entity mob, @NonNull String id) {
        mobs.put(mob, id);
    }
}
