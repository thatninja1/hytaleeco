package com.hytaleeco.plugin.util;

import com.hypixel.hytale.server.core.command.system.CommandSender;
import com.hypixel.hytale.server.core.universe.PlayerRef;

public final class PermissionUtil {

    private PermissionUtil() {
    }

    public static boolean hasEconomyAdmin(PlayerRef player) {
        return player.hasPermission("hytaleeco.admin")
                || player.hasPermission("economy.admin")
                || player.isOp();
    }

    public static boolean hasEconomyAdmin(CommandSender sender) {
        if (sender == null) {
            return false;
        }
        if (sender.isConsole()) {
            return true;
        }
        PlayerRef player = sender.getPlayer();
        if (player == null) {
            return false;
        }
        return hasEconomyAdmin(player);
    }
}
