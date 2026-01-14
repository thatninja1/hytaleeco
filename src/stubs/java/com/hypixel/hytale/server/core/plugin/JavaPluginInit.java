package com.hypixel.hytale.server.core.plugin;

public class JavaPluginInit {
    private final CommandRegistry commandRegistry = new CommandRegistry();

    public CommandRegistry getCommandRegistry() {
        return commandRegistry;
    }
}
