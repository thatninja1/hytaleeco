package com.hypixel.hytale.server.core.universe;

import com.hypixel.hytale.server.core.Message;

import java.util.UUID;

public class PlayerRef {
    private final UUID uuid = UUID.randomUUID();
    private final String name = "Player";

    public UUID getUuid() {
        return uuid;
    }

    public String getName() {
        return name;
    }

    public void sendMessage(Message message) {
    }

    public boolean hasPermission(String permission) {
        return false;
    }

    public boolean isOp() {
        return false;
    }
}
