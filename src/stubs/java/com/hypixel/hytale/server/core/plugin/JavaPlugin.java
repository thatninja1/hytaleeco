package com.hypixel.hytale.server.core.plugin;

public class JavaPlugin {

    private final JavaPluginInit init;

    private final CommandRegistry commandRegistry = new CommandRegistry();
    public JavaPlugin(JavaPluginInit init) {
        this.init = init;
    }

    protected void setup() {
    }

    public CommandRegistry getCommandRegistry() {
        return commandRegistry;
    }

}
