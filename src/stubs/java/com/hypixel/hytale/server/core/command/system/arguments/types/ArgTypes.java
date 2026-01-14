package com.hypixel.hytale.server.core.command.system.arguments.types;

import com.hypixel.hytale.server.core.universe.PlayerRef;

public final class ArgTypes {

    public static final ArgType<String> STRING = new ArgType<>();
    public static final ArgType<Long> LONG = new ArgType<>();
    public static final ArgType<PlayerRef> PLAYER_REF = new ArgType<>();

    private ArgTypes() {
    }
}
