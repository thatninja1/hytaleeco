package com.hypixel.hytale.server.core.plugin;

public class JavaPluginInit {
    private final java.nio.file.Path dataDirectory = java.nio.file.Paths.get("build/tmp/plugin-data");
    private final CommandRegistry commandRegistry = new CommandRegistry();

    public java.nio.file.Path getDataDirectory() {
        return dataDirectory;
    }

    public CommandRegistry commandRegistry() {
        return commandRegistry;
    }
}
