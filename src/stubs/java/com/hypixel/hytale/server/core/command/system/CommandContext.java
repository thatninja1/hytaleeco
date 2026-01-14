package com.hypixel.hytale.server.core.command.system;

import com.hypixel.hytale.server.core.Message;
public class CommandContext {
    private final CommandSender sender = new CommandSender(null, true);

    public CommandSender sender() {
        return sender;
    }

    public void sendMessage(Message message) {
    }
}
