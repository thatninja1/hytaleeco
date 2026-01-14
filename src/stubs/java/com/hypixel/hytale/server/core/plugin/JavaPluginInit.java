package com.hypixel.hytale.server.core.plugin;

public class JavaPluginInit {
    private final java.nio.file.Path dataDirectory = java.nio.file.Paths.get("build/tmp/plugin-data");
    private final com.hypixel.hytale.server.core.command.system.CommandManager commandManager =
            new com.hypixel.hytale.server.core.command.system.CommandManager();

    public java.nio.file.Path getDataDirectory() {
        return dataDirectory;
    }

    public com.hypixel.hytale.server.core.command.system.CommandManager getCommandManager() {
        return commandManager;
    }
}
