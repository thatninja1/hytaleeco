package com.hypixel.hytale.server.core.plugin;

public class JavaPluginInit {
    private final CommandRegistry commandRegistry = new CommandRegistry();
    private final java.nio.file.Path dataDirectory = java.nio.file.Paths.get("build/tmp/plugin-data");

    public CommandRegistry getCommandRegistry() {
        return commandRegistry;
    }

    public java.nio.file.Path getDataDirectory() {
        return dataDirectory;
    }
}
