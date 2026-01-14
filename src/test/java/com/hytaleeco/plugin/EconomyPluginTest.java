package com.hytaleeco.plugin;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class EconomyPluginTest {

    @Test
    void verifiesSuperclass() {
        Class<?> superClass = EconomyPlugin.class.getSuperclass();
        String name = superClass != null ? superClass.getName() : "null";
        System.out.println("EconomyPlugin superclass: " + name);
        assertEquals("com.hypixel.hytale.server.core.plugin.JavaPlugin", name);
    }
}
