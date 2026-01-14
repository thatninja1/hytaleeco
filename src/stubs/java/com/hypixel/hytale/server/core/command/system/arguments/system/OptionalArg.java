package com.hypixel.hytale.server.core.command.system.arguments.system;

import com.hypixel.hytale.server.core.command.system.arguments.types.ArgType;

public class OptionalArg<T> extends RequiredArg<T> {
    public OptionalArg(String name, ArgType<T> type) {
        super(name, type);
    }
}
