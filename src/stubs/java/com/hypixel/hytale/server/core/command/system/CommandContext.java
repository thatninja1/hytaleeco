package com.hypixel.hytale.server.core.command.system;

import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.command.system.arguments.system.RequiredArg;

public class CommandContext {
    private final CommandSender sender = new CommandSender(null, true);

    public CommandSender sender() {
        return sender;
    }

    public void sendMessage(Message message) {
    }

    public <T> T get(RequiredArg<T> arg) {
        return null;
    }
}
