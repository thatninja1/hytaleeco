package com.hytaleeco.plugin.util;

import com.hypixel.hytale.server.core.universe.PlayerRef;

import java.lang.reflect.Method;
import java.util.Collection;

public final class PlayerResolver {

    private PlayerResolver() {
    }

    public static PlayerRef findOnlinePlayer(String name) {
        if (name == null || name.isBlank()) {
            return null;
        }
        try {
            Class<?> universeClass = Class.forName("com.hypixel.hytale.server.core.universe.Universe");
            Method getMethod = universeClass.getMethod("get");
            Object universe = getMethod.invoke(null);
            if (universe == null) {
                return null;
            }
            Method worldMethod = universe.getClass().getMethod("getDefaultWorld");
            Object world = worldMethod.invoke(universe);
            if (world == null) {
                return null;
            }
            Method playersMethod = world.getClass().getMethod("getPlayerRefs");
            Object players = playersMethod.invoke(world);
            if (players instanceof Collection<?> collection) {
                for (Object entry : collection) {
                    if (entry instanceof PlayerRef player) {
                        if (player.getUsername().equalsIgnoreCase(name)) {
                            return player;
                        }
                    }
                }
            }
        } catch (Exception ignored) {
        }
        return null;
    }
}
