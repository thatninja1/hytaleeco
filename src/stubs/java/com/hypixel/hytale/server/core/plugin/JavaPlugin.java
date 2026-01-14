package com.hypixel.hytale.server.core.plugin;

import java.util.logging.Logger;

public class JavaPlugin {

    private final JavaPluginInit init;
    private final CommandRegistry commandRegistry = new CommandRegistry();
    private final Logger logger = Logger.getLogger(JavaPlugin.class.getName());

    public JavaPlugin(JavaPluginInit init) {
        this.init = init;
    }

    protected void setup() {
    }

    public CommandRegistry getCommandRegistry() {
        return commandRegistry;
    }

    public Logger getLogger() {
        return logger;
    }

}
