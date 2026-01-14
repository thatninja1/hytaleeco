package com.hypixel.hytale.server.core.plugin;

public class JavaPluginInit {
    private final java.nio.file.Path dataDirectory = java.nio.file.Paths.get("build/tmp/plugin-data");

    public java.nio.file.Path getDataDirectory() {
        return dataDirectory;
    }
}
