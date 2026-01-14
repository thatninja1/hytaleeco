package com.hytaleeco.plugin.util;

import com.hypixel.hytale.server.core.command.system.CommandContext;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public final class CommandUtil {

    private CommandUtil() {
    }

    public static List<String> getArguments(CommandContext context) {
        String[] args = context.getArguments();
        if (args == null || args.length == 0) {
            return Collections.emptyList();
        }
        return Arrays.asList(args);
    }

    public static Long parsePositiveAmount(String raw) {
        return parseAmount(raw, 1);
    }

    public static Long parseNonNegativeAmount(String raw) {
        return parseAmount(raw, 0);
    }

    private static Long parseAmount(String raw, long minimum) {
        if (raw == null || raw.isBlank()) {
            return null;
        }
        try {
            long value = Long.parseLong(raw);
            if (value < minimum) {
                return null;
            }
            return value;
        } catch (NumberFormatException ex) {
            return null;
        }
    }
}
