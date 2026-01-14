package com.hytaleeco.plugin.util;

import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.universe.PlayerRef;

public final class MessageUtil {

    private MessageUtil() {
    }

    public static void send(PlayerRef player, String text) {
        player.sendMessage(Message.raw(text));
    }

    public static Message raw(String text) {
        return Message.raw(text);
    }
}
