package com.hytaleeco.plugin.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Locale;
import java.util.Optional;
import java.util.logging.Logger;

public final class CommandRegistrar {

    private final Object init;
    private final Logger logger;
    private Object registry;
    private Method registerMethod;

    public CommandRegistrar(Object init, Logger logger) {
        this.init = init;
        this.logger = logger;
    }

    public boolean registerCommand(Object command) {
        if (!ensureRegistry()) {
            logger.warning("Failed to locate a command registry/manager.");
            return false;
        }
        try {
            registerMethod.invoke(registry, command);
            return true;
        } catch (Exception ex) {
            logger.warning("Failed to register command: " + ex.getMessage());
            return false;
        }
    }

    public String getMechanismDescription() {
        if (!ensureRegistry()) {
            return "<unknown>";
        }
        return registry.getClass().getName() + "#" + registerMethod.getName();
    }

    private boolean ensureRegistry() {
        if (registry != null) {
            return true;
        }
        Optional<RegistryHandle> handle = findRegistry(init);
        if (handle.isEmpty()) {
            return false;
        }
        RegistryHandle found = handle.get();
        this.registry = found.registry();
        this.registerMethod = found.method();
        return true;
    }

    private Optional<RegistryHandle> findRegistry(Object source) {
        if (source == null) {
            return Optional.empty();
        }
        for (Method method : source.getClass().getMethods()) {
            if (method.getParameterCount() != 0) {
                continue;
            }
            try {
                Object candidate = method.invoke(source);
                Optional<Method> register = findRegisterMethod(candidate);
                if (register.isPresent()) {
                    return Optional.of(new RegistryHandle(candidate, register.get()));
                }
            } catch (Exception ignored) {
            }
        }
        for (Field field : source.getClass().getDeclaredFields()) {
            field.setAccessible(true);
            try {
                Object candidate = field.get(source);
                Optional<Method> register = findRegisterMethod(candidate);
                if (register.isPresent()) {
                    return Optional.of(new RegistryHandle(candidate, register.get()));
                }
            } catch (Exception ignored) {
            }
        }
        return Optional.empty();
    }

    private Optional<Method> findRegisterMethod(Object candidate) {
        if (candidate == null) {
            return Optional.empty();
        }
        for (Method method : candidate.getClass().getMethods()) {
            if (method.getParameterCount() != 1) {
                continue;
            }
            String name = method.getName().toLowerCase(Locale.ROOT);
            if ((name.contains("register") || name.contains("add")) && name.contains("command")) {
                return Optional.of(method);
            }
        }
        return Optional.empty();
    }

    private record RegistryHandle(Object registry, Method method) {
    }
}
