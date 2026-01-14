package com.hypixel.hytale.server.core.command.system;

import com.hypixel.hytale.server.core.universe.PlayerRef;

public class CommandSender {
    private final PlayerRef player;
    private final boolean console;

    public CommandSender(PlayerRef player, boolean console) {
        this.player = player;
        this.console = console;
    }

    public PlayerRef getPlayer() {
        return player;
    }

    public boolean isConsole() {
        return console;
    }

    public String getDisplayName() {
        return player != null ? player.getUsername() : "Console";
    }
}
