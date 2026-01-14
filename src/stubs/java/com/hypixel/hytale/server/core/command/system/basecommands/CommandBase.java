package com.hypixel.hytale.server.core.command.system.basecommands;

import com.hypixel.hytale.server.core.command.system.CommandContext;
import com.hypixel.hytale.server.core.command.system.arguments.system.OptionalArg;
import com.hypixel.hytale.server.core.command.system.arguments.system.RequiredArg;
import com.hypixel.hytale.server.core.command.system.arguments.types.ArgType;

public abstract class CommandBase {

    protected CommandBase(String name, String description) {
    }

    protected <T> RequiredArg<T> withRequiredArg(String name, String key, ArgType<T> type) {
        return new RequiredArg<>(name, type);
    }

    protected <T> OptionalArg<T> withOptionalArg(String name, String key, ArgType<T> type) {
        return new OptionalArg<>(name, type);
    }

    protected void addAliases(String... aliases) {
    }

    protected abstract void executeSync(CommandContext context);
}
