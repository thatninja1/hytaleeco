package com.hypixel.hytale.server.core.plugin;

import java.util.logging.Logger;

public class JavaPlugin {

    private final JavaPluginInit init;
    private final Logger logger = Logger.getLogger(JavaPlugin.class.getName());

    public JavaPlugin(JavaPluginInit init) {
        this.init = init;
    }

    protected void setup(JavaPluginInit init) {
    }

    public Logger getLogger() {
        return logger;
    }

}
