package com.hytaleeco.plugin.util;

import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.World;

import java.util.Optional;

public final class PlayerLookup {

    private PlayerLookup() {
    }

    public static Optional<PlayerRef> findOnlinePlayer(World world, String name) {
        if (name == null || name.isBlank()) {
            return Optional.empty();
        }
        for (PlayerRef player : world.getPlayers()) {
            if (player.getName().equalsIgnoreCase(name)) {
                return Optional.of(player);
            }
        }
        return Optional.empty();
    }
}
