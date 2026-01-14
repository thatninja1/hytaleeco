package com.hypixel.hytale.server.core.command.system.basecommands;

import com.hypixel.hytale.server.core.command.system.CommandContext;

public abstract class CommandBase {

    protected CommandBase(String name, String description) {
    }

    protected void addAliases(String... aliases) {
    }

    protected abstract void executeSync(CommandContext context);
}
