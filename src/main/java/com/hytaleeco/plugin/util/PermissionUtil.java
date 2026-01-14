package com.hytaleeco.plugin.util;

import com.hypixel.hytale.server.core.universe.PlayerRef;

public final class PermissionUtil {

    private PermissionUtil() {
    }

    public static boolean hasEconomyAdmin(PlayerRef player) {
        return player.hasPermission("hytaleeco.admin")
                || player.hasPermission("economy.admin")
                || player.isOp();
    }
}
