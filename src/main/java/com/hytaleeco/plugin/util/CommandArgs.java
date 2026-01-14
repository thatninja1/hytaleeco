package com.hytaleeco.plugin.util;

import com.hypixel.hytale.server.core.command.system.CommandContext;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class CommandArgs {

    private CommandArgs() {
    }

    public static List<String> getArgs(CommandContext context, String commandName) {
        Object raw = resolveRawArgs(context);
        if (raw instanceof String[] array) {
            return normalize(Arrays.asList(array), commandName);
        }
        if (raw instanceof List<?> list) {
            List<String> args = new ArrayList<>();
            for (Object item : list) {
                if (item != null) {
                    args.add(item.toString());
                }
            }
            return normalize(args, commandName);
        }
        if (raw instanceof String text) {
            return normalize(split(text), commandName);
        }
        return List.of();
    }

    private static Object resolveRawArgs(CommandContext context) {
        for (Method method : context.getClass().getMethods()) {
            if (method.getParameterCount() != 0) {
                continue;
            }
            Class<?> returnType = method.getReturnType();
            if (returnType.isArray() && returnType.getComponentType().equals(String.class)) {
                return invoke(context, method);
            }
            if (List.class.isAssignableFrom(returnType) || returnType.equals(String.class)) {
                Object result = invoke(context, method);
                if (result != null) {
                    return result;
                }
            }
        }
        return null;
    }

    private static Object invoke(CommandContext context, Method method) {
        try {
            return method.invoke(context);
        } catch (Exception ignored) {
            return null;
        }
    }

    private static List<String> split(String text) {
        if (text == null || text.isBlank()) {
            return List.of();
        }
        return new ArrayList<>(List.of(text.trim().split("\\s+")));
    }

    private static List<String> normalize(List<String> args, String commandName) {
        if (args.isEmpty()) {
            return List.of();
        }
        String first = args.get(0);
        String normalized = commandName == null ? "" : commandName.toLowerCase();
        if (first.startsWith("/")) {
            first = first.substring(1);
        }
        if (!normalized.isEmpty() && first.equalsIgnoreCase(normalized)) {
            return new ArrayList<>(args.subList(1, args.size()));
        }
        return new ArrayList<>(args);
    }
}
