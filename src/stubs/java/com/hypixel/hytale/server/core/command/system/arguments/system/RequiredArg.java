package com.hypixel.hytale.server.core.command.system.arguments.system;

import com.hypixel.hytale.server.core.command.system.arguments.types.ArgType;

public class RequiredArg<T> {
    private final String name;
    private final ArgType<T> type;

    public RequiredArg(String name, ArgType<T> type) {
        this.name = name;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public ArgType<T> getType() {
        return type;
    }
}
